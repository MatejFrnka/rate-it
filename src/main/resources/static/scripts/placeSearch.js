let data = [];
let imageUrls = [];
let usersCoords;
let sortByNearest = false;
navigator.geolocation.getCurrentPosition(success, error);

window.addEventListener('load', async () => {
    try {
        const fetchUrl = `/api/v1/interests/${interestId}/places`;
        const response = await fetch(fetchUrl);
        data = await response.json();

        imageUrls = await Promise.all(data.map(place => fetchImageUrl(place)));

        loadPlaces();
    } catch (error) {
        console.error('Error fetching places info:', error);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadPlaces();
});

function loadPlaces(query) {
    const container = document.querySelector('#suggestionList');
    container.innerHTML = '';
    const template = document.getElementsByTagName('template')[0];
    let dataSet = data;

    if (typeof query !== 'undefined' && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(place => place.name.toLowerCase().includes(query.toLowerCase()));
    }

    if (sortByNearest) {
        dataSet = dataSet.sort((a, b) => distance(usersCoords[0], usersCoords[1], a.latitude, a.longitude) - distance(usersCoords[0], usersCoords[1], b.latitude, b.longitude));
    } else {
        dataSet = dataSet.sort((a, b) => a.id - b.id);
    }

    dataSet.forEach((place, index) => {
        const imageUrl = imageUrls[index];
        const clone = document.importNode(template.content, true);

        const elements = {
            placeLink: clone.querySelector('.interest-place'),
            placeImg: clone.querySelector('.interest-place-img img'),
            titleH3: clone.querySelector('.interest-place-title h3'),
            titleH4: clone.querySelector('.interest-place-title h4'),
            distanceSpan: clone.querySelector('.interest-place-distance span'),
            rating: clone.querySelector('.rating'),
            ratingContainer: clone.querySelector('.interest-place-ratings')
        };

        elements.placeLink.href = `/interests/${interestId}/places/${place.id}`;
        elements.placeImg.src = imageUrl;
        elements.titleH3.textContent = place.name;
        elements.titleH4.textContent = place.address;

        if (usersCoords !== undefined) {
            elements.distanceSpan.textContent = distance(usersCoords[0], usersCoords[1], place.latitude, place.longitude).toFixed(1) + ' km';
        }

        const averageRating = (place.avgRating / 2).toFixed(1);
        elements.rating.textContent = averageRating;

        const { bestCriterion, worstCriterion } = getBestAndWorstCriteria(place.criteria);

        elements.ratingContainer.innerHTML = '';
        elements.ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', averageRating, 'Overall'));
        elements.ratingContainer.appendChild(createRatingItem('fas fa-star yellow', (bestCriterion.avgRating / 2).toFixed(1), bestCriterion.name));
        elements.ratingContainer.appendChild(createRatingItem('fas fa-star', (worstCriterion.avgRating / 2).toFixed(1), worstCriterion.name));

        container.appendChild(clone);
    });
}

function getBestAndWorstCriteria(criteria) {
    const bestCriterion = criteria.reduce((max, cr) => (!max || cr.avgRating > max.avgRating ? cr : max), null);
    const worstCriterion = criteria.reduce((min, cr) => (!min || cr.avgRating < min.avgRating ? cr : min), null);
    return { bestCriterion, worstCriterion };
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}

function createRatingItem(iconClass, ratingValue, criterionName) {
    const li = document.createElement('li');

    const icon = document.createElement('i');
    icon.className = iconClass;
    li.appendChild(icon);

    const ratingSpan = document.createElement('span');
    ratingSpan.className = 'rating';
    ratingSpan.textContent = ratingValue;
    li.appendChild(ratingSpan);

    const criterionSpan = document.createElement('span');
    criterionSpan.className = 'criterion';
    criterionSpan.textContent = criterionName;
    li.appendChild(criterionSpan);

    return li;
}

function success(position) {
    usersCoords = [position.coords.latitude, position.coords.longitude];
    loadPlaces();
}

function error() {
    console.log("Unable to retrieve your location");
}

function distance(lat1, lon1, lat2, lon2) {
    let radlat1 = Math.PI * lat1 / 180;
    let radlat2 = Math.PI * lat2 / 180;
    let theta = lon1 - lon2;
    let radtheta = Math.PI * theta / 180;
    let dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
    if (dist > 1) {
        dist = 1;
    }
    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    dist = dist * 60 * 1.85316;
    return dist;
}

function toggleNearest() {
    sortByNearest = !sortByNearest;
    navigator.geolocation.getCurrentPosition(success, error);
    loadPlaces();
}
