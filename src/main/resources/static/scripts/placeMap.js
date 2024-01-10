let latlng = L.latLng([latitude, longitude]);
let map = L.map('map').setView(latlng, 14);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

let marker = L.marker(latlng).addTo(map);

marker.bindPopup("<b>" + name + "</b><br>" + "Rating: " + rating / 2 + "/5");

let popup = L.popup();

// function onMapClick(e) {
//     popup
//         .setLatLng(e.latlng)
//         .setContent("You clicked the map at " + e.latlng.toString())
//         .openOn(map);
// }
//
// map.on('click', onMapClick);