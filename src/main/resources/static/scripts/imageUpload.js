import {Uppy, Dashboard, XHRUpload, Webcam} from "https://releases.transloadit.com/uppy/v3.21.0/uppy.min.mjs"

let dynamicEndpoint = window.location.pathname;

const uppy = new Uppy({
    restrictions: {
        allowedFileTypes: ["image/*"],
        maxFileSize:  10 * 1024 * 1024
    }
})
    .use(Dashboard, {target: '#uppy', inline: true})
    .use(XHRUpload, {
        endpoint: dynamicEndpoint,
        fieldName: 'picture',
        formData: true
    })
    .use(Webcam, {
        target: Dashboard,
        showVideoSourceDropdown: true,
        modes: 'picture'
    })
