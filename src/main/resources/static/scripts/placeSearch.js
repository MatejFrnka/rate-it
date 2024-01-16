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
    }

    dataSet.forEach((place, index) => {
        const imageUrl = imageUrls[index];
        const clone = document.importNode(template.content, true);
        clone.querySelector('.interest-place').href = `/interests/${interestId}/places/${place.id}`;
        clone.querySelector('.interest-place-img img').src = imageUrl;
        clone.querySelector('.interest-place-title h3').textContent = place.name;
        clone.querySelector('.interest-place-title h4').textContent = place.address;
        if (usersCoords !== undefined) {
            clone.querySelector('.interest-place-distance span')
                .textContent = distance(usersCoords[0], usersCoords[1], place.latitude, place.longitude).toFixed(1) + ' km';
        }

        const averageRating = place.avgRating / 2;
        const formattedRating = averageRating.toFixed(1);
        clone.querySelector('.rating').textContent = formattedRating;

        const bestCriterion = place.criteria.reduce((max, cr) => (!max || cr.avgRating > max.avgRating ? cr : max), null);
        const worstCriterion = place.criteria.reduce((min, cr) => (!min || cr.avgRating < min.avgRating ? cr : min), null);
        const bestCriterionRating = (bestCriterion.avgRating / 2).toFixed(1);
        const worstCriterionRating = (worstCriterion.avgRating / 2).toFixed(1);

        const ratingContainer = clone.querySelector('.interest-place-ratings');

        ratingContainer.innerHTML = '';

        ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', formattedRating, 'Overall'));
        ratingContainer.appendChild(createRatingItem('fas fa-star yellow', bestCriterionRating, bestCriterion.name));
        ratingContainer.appendChild(createRatingItem('fas fa-star', worstCriterionRating, worstCriterion.name));

        container.appendChild(clone);
    });
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
    console.log(sortByNearest);
    loadPlaces();
}
