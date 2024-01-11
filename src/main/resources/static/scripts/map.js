let latlng = L.latLng([49.8037633, 15.4749126]);
let map = L.map('map').setView(latlng, 7);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

console.log(latitude, longitude);

let marker;


map.on("click", function (e) {
    marker ? marker.remove() : null;
    let latitude = e.latlng.lat;
    let longitude = e.latlng.lng;

    marker = new L.marker([latitude, longitude], {draggable: true, autoPan: true}).addTo(map);

    let inputLat = document.getElementById("place-latitude");
    let inputLng = document.getElementById("place-longitude");

    inputLat.value = latitude;
    inputLng.value = longitude;

    marker.on("dragend", function (event) {
        let updatedLatLng = event.target.getLatLng();
        inputLat.value = updatedLatLng.lat;
        inputLng.value = updatedLatLng.lng;
    });
})

