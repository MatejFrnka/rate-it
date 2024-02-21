import {Uppy, Dashboard, XHRUpload, Webcam} from "https://releases.transloadit.com/uppy/v3.21.0/uppy.min.mjs"

const placeIdMatch = window.location.href.match(/\/(\d+)(#)?$/);
let dynamicEndpoint = "/api/v1/images/".concat(placeId).concat("/new-place-image");

const uppy = new Uppy({
    restrictions: {
        allowedFileTypes: ["image/*"],
        maxFileSize: 10 * 1024 * 1024,

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
        formData: true,
    })
    .use(Webcam, {
        target: Dashboard,
        showVideoSourceDropdown: true,
        modes: 'picture'
    })

uppy.on('complete', (result) => {

    console.log('Upload status:', result);

    if (result.failed.length > 0) {
        return;
    } else {
        window.location.reload();
    }
});

uppy.on('upload-error', (file, error, response) => {
    console.log('error with file:', file.id);
    console.log('error message:', error);
});


const uppyAddFilesList = document.querySelector('.uppy-Dashboard--modal .uppy-Dashboard-inner');

if (uppyAddFilesList) {
    uppyAddFilesList.style.top = '30%';
    uppyAddFilesList.style.bottom = 'auto';
}

const customStyles = document.createElement('style');
customStyles.innerHTML = '.uppy-Dashboard-AddFiles-title { display: none !important; }'
document.head.appendChild(customStyles);