let data = [];
const interestId = extractInterestIdFromUrl();
let imageUrls = [];

window.addEventListener('load', async () => {
    try {
        const fetchUrl = `/api/v1/interests/${interestId}/places`;
        const response = await fetch(fetchUrl);
        const jsonData = await response.json();
        data = jsonData;

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

    dataSet.forEach((place, index) => {
        const imageUrl = imageUrls[index];
        const clone = document.importNode(template.content, true);
        clone.querySelector('.interest-place').href = `/interests/${interestId}/places/${place.id}`;
        clone.querySelector('.interest-place-img img').src = imageUrl;
        clone.querySelector('.interest-place-title h3').textContent = place.name;
        clone.querySelector('.interest-place-title h4').textContent = place.address;

        const averageRating = place.avgRating / 2;
        const formattedRating = averageRating.toFixed(1);
        clone.querySelector('.rating').textContent = formattedRating;

        const bestCriterionRating = (place.bestRatedCriterionRating / 2).toFixed(1);
        const worstCriterionRating = (place.worstRatedCriterionRating / 2).toFixed(1);

        const ratingContainer = clone.querySelector('.interest-place-ratings');

        ratingContainer.innerHTML = '';

        ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', formattedRating, 'Overall'));
        ratingContainer.appendChild(createRatingItem('fas fa-star yellow', bestCriterionRating, place.bestRatedCriterionName));
        ratingContainer.appendChild(createRatingItem('fas fa-star', worstCriterionRating, place.worstRatedCriterionName));

        container.appendChild(clone);
    });
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}

function extractInterestIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.indexOf('interests') + 1];
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