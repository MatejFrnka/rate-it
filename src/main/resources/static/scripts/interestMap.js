const defaultView = [49.8037633, 15.4749126];
const defaultZoom = 7;
const mapUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png';
const tileLayerOptions = {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
};

let map = L.map('map');

if (places.length === 0) {
    map.setView(defaultView, defaultZoom);
} else {
    let markers = new L.featureGroup();

    L.tileLayer(mapUrl, tileLayerOptions).addTo(map);

    places.forEach(place => {
        const popup = "<b><a href='/interests/" + interestId + "/places/" + place.id + "'>" + place.name + "</a></b>" +
            "<br>" + "Rating: " + (place.avgRating / 2).toFixed(1) + "/5<br>Lat: " + place.latitude + "<br>Lng: " + place.longitude

        let latlng = L.latLng([place.latitude, place.longitude]);
        let marker = L.marker(latlng).addTo(map);
        marker.bindPopup(popup);
        markers.addLayer(marker);
    });

    map.addLayer(markers);
    map.fitBounds(markers.getBounds());
}

L.tileLayer(mapUrl, tileLayerOptions).addTo(map);


