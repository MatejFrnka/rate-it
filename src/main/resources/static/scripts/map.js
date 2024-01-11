let latlng = L.latLng([49.8037633, 15.4749126]);
let map = L.map('map').setView(latlng, 7);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

let marker;
map.on("click", function (e) {
    if (marker != null) {
        marker.remove();
    }
    marker = new L.marker([e.latlng.lat, e.latlng.lng], {draggable: true, autoPan: true}).addTo(map);


})