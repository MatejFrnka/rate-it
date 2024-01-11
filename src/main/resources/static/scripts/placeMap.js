const defaultPlaceZoom = 14;
const mapUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png';
const tileLayerOptions = {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
};

let latlng = L.latLng([latitude, longitude]);
let map = L.map('map').setView(latlng, defaultPlaceZoom);

L.tileLayer(mapUrl, tileLayerOptions).addTo(map);

let marker = L.marker(latlng).addTo(map);
marker.bindPopup("<b>" + name + "</b><br>" + "Rating: " + rating / 2 + "/5<br>Lat: " + latitude + "<br>Lng: " + longitude);
