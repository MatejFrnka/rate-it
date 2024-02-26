const defaultPlaceZoom = 14;
const mapUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png';
const tileLayerOptions = {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
};

let latlng = L.latLng([placeLat, placeLng]);
let map = L.map('map').setView(latlng, defaultPlaceZoom);

L.tileLayer(mapUrl, tileLayerOptions).addTo(map);

let marker = L.marker(latlng).addTo(map);
marker.bindPopup("<b>" + placeName + "</b><br>" +
    "<a href='https://www.google.com/maps/place/" + placeLat + ',' + placeLng + "' target='_blank'>See on Google Maps</a>");

