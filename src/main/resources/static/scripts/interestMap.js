let map = L.map('map');
const tileLayerOptions = {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
};

if (places.length === 0) {
    map.setView([49.8037633, 15.4749126], 7);
} else {
    let markers = new L.featureGroup();

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', tileLayerOptions).addTo(map);

    places.forEach(place => {
        let latlng = L.latLng([place.latitude, place.longitude]);
        let marker = L.marker(latlng).addTo(map);
        marker.bindPopup("<b><a href='/interests/1/places/" + place.id + "'>" + place.name + "</a></b><br>" + "Rating: " + place.avgRating / 2 + "/5");
        markers.addLayer(marker);
    });

    map.addLayer(markers);
    map.fitBounds(markers.getBounds());
}

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', tileLayerOptions).addTo(map);


