let latlng = L.latLng([49.8037633, 15.4749126]);
let map = L.map('map').setView(latlng, 7);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

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
