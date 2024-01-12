let data = [];

window.addEventListener('load', async () => {
    try {
        const interestId = extractInterestIdFromUrl();
        const fetchUrl = `/api/v1/interests/${interestId}/places`;
        const response = await fetch(fetchUrl);
        const jsonData = await response.json();
        data = jsonData;
        loadPlaces()
    } catch (error) {
        console.error('Error fetching places info:', error);
    }
})

document.addEventListener('DOMContentLoaded', () => {
    loadPlaces()
});

function loadPlaces(query) {
    const container = document.querySelector('#suggestionList');
    container.innerHTML = '';
    const template = document.getElementsByTagName('template')[0];
    let dataSet = data;

    if (typeof query !== 'undefined' && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(record => record.name.toLowerCase().includes(query.toLowerCase()));
    }

    dataSet.forEach(place => {
        const clone = template.content.cloneNode(true);
        clone.querySelector('.interest-place').href = `/interests/${place.interest.id}/places/${place.id}`;
        clone.querySelector('h3').textContent = `${place.name}`;
        clone.querySelector('h4').textContent = `${place.address}`;

        const averageRating = place.place().getAverageRating() / 2;
        const formattedRating = new Intl.NumberFormat('en', { minimumFractionDigits: 1, maximumFractionDigits: 1 }).format(averageRating);
        clone.querySelector('.rating').textContent = formattedRating;

        // Create and append the dynamic rating structure
        const ratingContainer = document.createElement('ul');
        ratingContainer.classList.add('interest-place-ratings');

        // Function to create a rating list item
        function createRatingItem(iconClass, ratingValue, criterionName) {
            const li = document.createElement('li');

            const icon = document.createElement('i');
            icon.classList.add(iconClass);
            li.appendChild(icon);

            const ratingSpan = document.createElement('span');
            ratingSpan.classList.add('rating');
            ratingSpan.textContent = ratingValue;
            li.appendChild(ratingSpan);

            const criterionSpan = document.createElement('span');
            criterionSpan.classList.add('criterion');
            criterionSpan.textContent = criterionName;
            li.appendChild(criterionSpan);

            return li;
        }

        // Add your actual logic to get criterion data
        const bestCriterionRating = place.bestRatedCriterion().avgRating() / 2;
        const worstCriterionRating = place.worstRatedCriterion().avgRating() / 2;

        // Create and append the rating list items
        ratingContainer.appendChild(createRatingItem('fas fa-star overall yellow', formattedRating, 'Overall'));
        ratingContainer.appendChild(createRatingItem('fas fa-star yellow', bestCriterionRating, place.bestRatedCriterion().criterion().name));
        ratingContainer.appendChild(createRatingItem('fas fa-star', worstCriterionRating, place.worstRatedCriterion().criterion().name));

        // Append the rating container to the clone
        clone.querySelector('.interest-place-content').appendChild(ratingContainer);

        container.appendChild(clone);
    })
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}

function extractInterestIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.indexOf('interests') + 1];
}