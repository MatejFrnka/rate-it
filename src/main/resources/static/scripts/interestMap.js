let latlng = L.latLng([places[0].latitude, places[0].longitude]);
let map = L.map('map').setView(latlng, 14);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

//for each place, create a marker
places.forEach(place => {
    let latlng = L.latLng([place.latitude, place.longitude]);
    let marker = L.marker(latlng).addTo(map);
    marker.bindPopup("<b><a href='/interests/1/places/" + place.id + "'>" + place.name + "</a></b><br>" + "Rating: " + place.avgRating / 2 + "/5");
});