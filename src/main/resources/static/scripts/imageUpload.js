import {Uppy, Dashboard, XHRUpload, Webcam} from "https://releases.transloadit.com/uppy/v3.21.0/uppy.min.mjs"

let dynamicEndpoint = window.location.pathname;

const uppy = new Uppy({
    restrictions: {
        allowedFileTypes: ["image/*"],
        maxFileSize: 10 * 1024 * 1024,

    }
})
    .use(Dashboard, {
        target: '#uppy',
        inline: false,
        proudlyDisplayPoweredByUppy: false,
        trigger: '#uppy-modal',
        closeModalOnClickOutside: true,
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


const uppyAddFilesList = document.querySelector('.uppy-Dashboard--modal .uppy-Dashboard-inner');

if (uppyAddFilesList) {
    uppyAddFilesList.style.top = '30%';
    uppyAddFilesList.style.bottom = 'auto';
}

const customStyles = document.createElement('style');
customStyles.innerHTML = '.uppy-Dashboard-AddFiles-title { display: none !important; }'
document.head.appendChild(customStyles);