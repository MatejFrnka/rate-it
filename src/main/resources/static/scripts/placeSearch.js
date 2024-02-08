let data = [];
let usersCoords;

document.addEventListener('DOMContentLoaded', async () => {
    document.querySelector('.loader').style.display = 'block';
    try {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(success, error);
            document.querySelector('.loader').style.display = 'none';
        } else {
            console.log("Geolocation is not supported");
            await fetchData();
            document.querySelector('.loader').style.display = 'none';
        }
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
});

async function success(position) {
    usersCoords = [position.coords.latitude, position.coords.longitude];
    try {
        await fetchData();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
}

async function error() {
    console.log("Unable to retrieve your location");
    await fetchData();
}

async function fetchData() {
    try {
        const fetchInterest = `/api/v1/interests/${interestId}`;
        const interestResponse = await fetch(fetchInterest);
        const interest = await interestResponse.json();

        const fetchUrl = `/api/v1/interests/${interestId}/places`;
        const response = await fetch(fetchUrl);
        const jsonData = await response.json();
        data = jsonData;

        data = await Promise.all(jsonData.map(async (place) => {
            place.imageUrl = await fetchImageUrl(place, interest);
            return place;
        }))

        loadSortButtons();
        loadPlaces();
    } catch (error) {
        console.error('Error fetching places info:', error);
    }

}

function loadSortButtons() {
    if (data.length < 1) {
        return;
    }

    const container = document.querySelector(".splide__list");
    const template = document.getElementById('sort-button-template');

    if (usersCoords !== undefined) {
        createSortingButton(container, template, 'Nearest');
    }

    createSortingButton(container, template, 'Top Rated');

    if (data.length > 0) {
        let interestCriteria = data[0].criteria;
        interestCriteria.forEach(criterion => {
            createSortingButton(container, template, criterion.name);
        });
    }
    initializeCheckboxesSplide()
}

function createSortingButton(container, template, sortBy) {
    const clone = document.importNode(template.content, true);
    const checkbox = clone.querySelector('input');
    const title = clone.querySelector('span');
    title.textContent = sortBy;

    checkbox.addEventListener('input', () => {
        filterPlaces(sortBy);
    });

    container.appendChild(clone);
}

function filterPlaces(sortBy) {
    let searchBar = document.querySelector('.search');
    if (typeof sortBy === 'undefined' || sortBy === null) {
        sortBy = getCheckedSorting();
    }
    if (sortBy !== null) {
        uncheckOtherCheckboxes(sortBy)
        loadPlaces(searchBar.value, sortBy);
    } else {
        loadPlaces(searchBar.value, null);
    }
}

function uncheckOtherCheckboxes(sortBy) {
    let checkboxes = document.querySelectorAll('.sort-checkbox');
    checkboxes.forEach(checkbox => {
        const title = checkbox.parentNode.querySelector('span').textContent;
        if (title !== sortBy) {
            checkbox.checked = false;
        }
    });
}

function getCheckedSorting() {
    let checkboxes = document.querySelectorAll('.sort-checkbox');
    let checked = null;
    checkboxes.forEach(c => {
        if (c.checked && checked === null) {
            checked = c;
        }
    });
    if (checked === null) {
        return null;
    }
    return checked.parentNode.querySelector('span').textContent;
}

function loadPlaces(query, sortBy) {
    if (data.length < 1) {
        return;
    }
    const container = document.querySelector('#suggestionList');
    container.innerHTML = '';
    const template = document.getElementById('place-template');
    let dataSet = data;

    if (typeof query !== 'undefined' && query !== null && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(place => place.name.toLowerCase().includes(query.toLowerCase()));
    }

    if (sortBy === 'Nearest') {
        dataSet = dataSet.sort((a, b) => distance(usersCoords[0], usersCoords[1], a.latitude, a.longitude) -
            distance(usersCoords[0], usersCoords[1], b.latitude, b.longitude));
    } else if (sortBy === 'Top Rated') {
        dataSet = dataSet.sort((a, b) => (b.avgRating || 0) - (a.avgRating || 0));
    } else if (sortBy !== '') {
        dataSet = dataSet.sort((a, b) => {
            const ratingA = a.criteria.find(criterion => criterion.name === sortBy)?.avgRating || NaN;
            const ratingB = b.criteria.find(criterion => criterion.name === sortBy)?.avgRating || NaN;
            return ratingB - ratingA;
        });
    } else {
        // If no specific sortBy is selected, default to sorting by place ID
        dataSet = dataSet.sort((a, b) => a.id - b.id);
    }

    dataSet.forEach((place) => {

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
        elements.placeImg.src = place.imageUrl;
        elements.titleH3.textContent = place.name;
        elements.titleH4.textContent = place.address;

        if (usersCoords !== undefined) {
            elements.distanceSpan.textContent = distance(usersCoords[0], usersCoords[1], place.latitude, place.longitude).toFixed(1) + ' km';
        }

        let averageRating;
        if (isNaN(place.avgRating)) {
            averageRating = 'No ratings yet';
        } else {
            averageRating = (place.avgRating / 2).toFixed(1);
        }

        elements.rating.textContent = averageRating;

        const {bestCriterion, worstCriterion} = getBestAndWorstCriteria(place.criteria);

        elements.ratingContainer.innerHTML = '';

        if ((bestCriterion === null && worstCriterion === null)) {
            elements.ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', '---', averageRating));
        } else if (place.criteria.length === 1){
            elements.ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', averageRating, place.criteria[0].name));
        } else {
            let bestCriterionAvgRating = (bestCriterion.avgRating / 2).toFixed(1);
            let worstCriterionAvgRating = (worstCriterion.avgRating / 2).toFixed(1);
            elements.ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', averageRating, 'Overall'));
            elements.ratingContainer.appendChild(createRatingItem('fas fa-star yellow', (bestCriterionAvgRating), bestCriterion.name));
            elements.ratingContainer.appendChild(createRatingItem('fas fa-star', (worstCriterionAvgRating), worstCriterion.name));
        }

        container.appendChild(clone);
    });
}

function getBestAndWorstCriteria(criteria) {
    const validCriteria = criteria.filter(cr => !isNaN(cr.avgRating));
    const bestCriterion = validCriteria.reduce((max, cr) => (!max || cr.avgRating > max.avgRating ? cr : max), null);
    const worstCriterion = validCriteria.reduce((min, cr) => (!min || cr.avgRating < min.avgRating ? cr : min), null);
    return {bestCriterion, worstCriterion};
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
