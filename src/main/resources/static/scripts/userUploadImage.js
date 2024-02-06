import {Uppy, Dashboard, XHRUpload, Webcam} from "https://releases.transloadit.com/uppy/v3.21.0/uppy.min.mjs"
const usernameMatch = window.location.href.match(/\/users\/(.*)$/);
let dynamicEndpoint = '/api/v1/images/users/'.concat(usernameMatch[1]).concat('/new-profile-image');


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