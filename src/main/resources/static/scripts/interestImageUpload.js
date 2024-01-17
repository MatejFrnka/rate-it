import {Uppy, Dashboard, XHRUpload, Webcam} from "https://releases.transloadit.com/uppy/v3.21.0/uppy.min.mjs"

let dynamicEndpoint;
let httpMethod;
const uploadedImageIdField = document.getElementById('uploadedImageId');


if (window.location.pathname.includes('/interests/create')) {
    dynamicEndpoint = '/api/v1/images/new-interest-image';
    httpMethod = 'post'
} else {
    const match = window.location.pathname.match(/\/interests\/(\d+)\/admin\/edit/);
    const interestId = match ? match[1] : null;
    dynamicEndpoint = `/api/v1/images/interests/${interestId}/edit`;
    httpMethod = 'put'
}

const uppy = new Uppy({
    restrictions: {
        allowedFileTypes: ["image/*"],
        maxFileSize: 10 * 1024 * 1024,
        maxNumberOfFiles: 1

    },
    allowMultipleUploadBatches: false
})
    .use(Dashboard, {
        target: '#uppy',
        inline: false,
        proudlyDisplayPoweredByUppy: false,
        trigger: '#uppy-modal',
        closeModalOnClickOutside: true,
        closeAfterFinish: true
    })
    .use(XHRUpload, {
        endpoint: dynamicEndpoint,
        fieldName: 'picture',
        method: httpMethod,
        formData: true,
    })
    .use(Webcam, {
        target: Dashboard,
        showVideoSourceDropdown: true,
        modes: 'picture'
    })

uppy.on('upload-success', (file, response) => {
    const uploadedImageIdField = document.getElementById('uploadedImageId');
    uploadedImageIdField.value = response.body.id;
});


const uppyAddFilesList = document.querySelector('.uppy-Dashboard--modal .uppy-Dashboard-inner');

if (uppyAddFilesList) {
    uppyAddFilesList.style.top = '30%';
    uppyAddFilesList.style.bottom = 'auto';
}

const customStyles = document.createElement('style');
customStyles.innerHTML = '.uppy-Dashboard-AddFiles-title { display: none !important; }'
document.head.appendChild(customStyles);