// https://medium.com/geekculture/how-to-build-a-simple-star-rating-system-abcbb5117365

const STARS = 10;
const RATE_CONFIRMATION = document.getElementById('rate-confirmation');


function addClassToStars(stars, rating, className) {
    //loop through and set the active class on preceding stars
    for (let i = 0; i <= STARS; i++) {
        if (i <= rating) {
            //check if the classlist contains the active class, if not, add the class
            if (!stars.querySelector('.star-' + i).classList.contains(className)) {
                stars.querySelector('.star-' + i).classList.add(className);
            }
        } else {
            //check if the classlist contains the active class, if yes, remove the class
            if (stars.querySelector('.star-' + i).classList.contains(className)) {
                stars.querySelector('.star-' + i).classList.remove(className);
            }
        }
    }
}

function initializeRating() {
    let placeRatingStars = document.querySelectorAll('.place-rating-star');

    placeRatingStars.forEach(function (star) {
        function handleStarEvent(event) {
            event.preventDefault(); // Prevent default behavior for touch events

            let stars = event.currentTarget.parentElement;
            let rating = parseInt(event.currentTarget.getAttribute("data-star"));
            let criterionId = event.currentTarget.closest('.place-rating-stars').getAttribute('data-criterion-id');
            let hoverStarCount = document.getElementById('hover-count-' + criterionId);
            let activeStars = stars.querySelectorAll('.is-active');
            let activeStar = Math.max(...Array.from(activeStars).map(s => parseInt(s.getAttribute('data-star'))), 0);

            switch (event.type) {
                case 'mouseover':
                    stars.classList.add('hover');
                    addClassToStars(stars, rating, 'hover-active');

                    let hoverActiveStars = stars.querySelectorAll('.hover-active');
                    activeStar = Math.max(...Array.from(hoverActiveStars).map(s => parseInt(s.getAttribute('data-star'))));
                    hoverStarCount.textContent = activeStar !== -Infinity ? (activeStar !== 0 ? `${(activeStar / 2).toFixed(1)}` : '---') : '---';
                    break;

                case 'mouseout':
                    stars.classList.remove('hover');
                    for (let j = 1; j <= STARS; j++) {
                        stars.querySelector('.star-' + j).classList.remove('hover-active');
                    }
                    if (activeStar === 0) {
                        hoverStarCount.textContent = '---';
                    } else {
                        hoverStarCount.textContent = (activeStar / 2).toFixed(1);
                    }
                    break;

                case 'click':
                    RATE_CONFIRMATION.className = '';
                    RATE_CONFIRMATION.textContent = '';
                    // Set the input value and add class based on the click rating
                    let inputClick = stars.parentElement.querySelector('input');

                    // If the clicked star is the first one (position 0), set the value to null
                    let newClickRating = (rating === 0) ? null : rating;

                    inputClick.value = newClickRating;
                    addClassToStars(stars, newClickRating, 'is-active');
                    break;

                case 'touchmove':
                    stars.classList.add('hover');

                    // Get touch position
                    let touchX = (event.type === 'touchmove') ? event.touches[0].clientX : event.clientX;

                    // Calculate the relative position within the stars container
                    let starsRect = stars.getBoundingClientRect();
                    let relativeX = touchX - starsRect.left;

                    // Calculate the star rating based on the relative position
                    let starWidth = starsRect.width / STARS;
                    let newRating = Math.ceil(relativeX / starWidth);

                    // Ensure the newRating is within the range of 0 to 10
                    newRating = Math.max(0, Math.min(newRating, 10));

                    // If newRating is 0, set it to null
                    newRating = (newRating === 0) ? null : newRating;

                    let touchActiveStars = stars.querySelectorAll('.hover-active');
                    activeStar = Math.max(...Array.from(touchActiveStars).map(s => parseInt(s.getAttribute('data-star'))));
                    hoverStarCount.textContent = activeStar !== 0 ? `${(activeStar / 2).toFixed(1)}` : '---';

                    // Set the input value and add class based on the touchmove rating
                    let inputMove = stars.parentElement.querySelector('input');
                    inputMove.value = newRating;
                    addClassToStars(stars, newRating, 'hover-active');
                    break;
            }
        }

        star.addEventListener('mouseover', handleStarEvent);
        star.addEventListener('mouseout', handleStarEvent);
        star.addEventListener('click', handleStarEvent);
        star.addEventListener('touchmove', handleStarEvent);
    });
}

document.addEventListener('DOMContentLoaded', initializeRating);
document.addEventListener('DOMContentLoaded', function () {

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            const formData = new FormData(event.target);
            const ratings = {};

            for (const rating of formData) {
                let id = rating[0];
                const value = rating[1];
                const idMatch = id.match(/ratings\[(\d+)\]/);

                if (idMatch) {
                    id = idMatch[1];
                }

                ratings[id] = value;
            }

            const response = await fetch(`/api/v1/places/${placeId}/rate`, {
                method: 'POST',
                body: JSON.stringify({ratings}),
                headers: {
                    'Content-Type': 'application/json'
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const responseData = await response.json();

            const updateElement = (elementId, value) => {
                const element = document.getElementById(elementId);
                if (isNaN(value)) {
                    element.textContent = 'N/A';
                } else {
                    element.textContent = (value / 2).toFixed(1);
                }
            };

            responseData.criteria.forEach(criteria => {
                updateElement(criteria.id, criteria.avgRating);
            });

            updateElement('place-rating', responseData.avgRating);
            RATE_CONFIRMATION.classList.add('success');
            RATE_CONFIRMATION.textContent = 'Rating saved.';

        } catch (error) {
            console.error(error);
            RATE_CONFIRMATION.classList.add('fail');
            RATE_CONFIRMATION.textContent = 'Something went wrong. Try again later.';
        }
    }

    const form = document.getElementById("rating-form");
    form.addEventListener('submit', handleSubmit);
});
