const defaultView = [49.8037633, 15.4749126];
const defaultPlaceZoom = 14;
const defaultZoom = 7;
const mapUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png';
const tileLayerOptions = {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
};

let latlng;
let map;

if (latitude && longitude) {
    latlng = L.latLng([latitude, longitude]);
    map = L.map('map').setView(latlng, defaultPlaceZoom);
} else {
    latlng = L.latLng(defaultView);
    map = L.map('map').setView(latlng, defaultZoom);
}

L.tileLayer(mapUrl, tileLayerOptions).addTo(map);

let marker;
let inputLat = document.getElementById("place-latitude");
let inputLng = document.getElementById("place-longitude");

if (latitude && longitude) {
    createMarker(latitude, longitude);
}

map.on("click", function (e) {
    let latitude = e.latlng.lat;
    let longitude = e.latlng.lng;
    createMarker(latitude, longitude);
});

map.on('locationfound', onLocationFound);
map.on('locationerror', onLocationError);

function createMarker(latitude, longitude) {
    if (marker) {
        marker.remove();
    }
    fillInputFields(latitude, longitude);
    marker = new L.marker([latitude, longitude], {draggable: true, autoPan: true}).addTo(map);
    marker.on("dragend", function (event) {
        let updatedLatLng = event.target.getLatLng();
        fillInputFields(updatedLatLng.lat, updatedLatLng.lng);
    });
}

function fillInputFields(latitude, longitude) {
    inputLat.value = latitude.toFixed(6);
    inputLng.value = longitude.toFixed(6);
}

function locate() {
    map.locate({setView: true, maxZoom: 16});
}

function onLocationFound(e) {
    createMarker(e.latlng.lat, e.latlng.lng);
}

function onLocationError(e) {
    alert(e.message);
}
