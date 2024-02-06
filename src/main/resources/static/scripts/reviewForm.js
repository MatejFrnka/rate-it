const TIMEOUT_DURATION = 2000;
let previousReviewText = null;
if (review) {
    previousReviewText = review.text;
}
document.addEventListener('DOMContentLoaded', async function () {
    updateCharCountAndResize(document.querySelector("#review-text-field").getAttribute("maxlength"));

    const rateButton = document.querySelector("#rate");
    if (!review) {
        rateButton.setAttribute('data-bs-toggle', 'modal');
        rateButton.setAttribute('data-bs-target', '#review-modal');
    }
    const reviewTextField = document.querySelector("#review-text-field");
    reviewTextField.addEventListener('input', function () {
        updateCharCountAndResize(reviewTextField.getAttribute("maxlength"));
    });

    if (review) {
        displayReview(review);
    } else {
        displayReview();
    }

    async function handleSubmit(event) {
        try {
            event.preventDefault();

            const reviewText = reviewTextField.value;

            if (!reviewText.trim()) {
                displayMessage('Please enter a new review before submitting.', 'secondary');
                return;
            }

            // Check if the new review text is the same as the previous review
            if (reviewText === previousReviewText) {
                displayMessage('Review text must be different from the previous review.', 'warning');
                return;
            }

            const response = await fetch(`/api/v1/places/${placeId}/review`, {
                method: 'POST',
                body: reviewText,
                headers: {
                    'Content-Type': 'text/plain'
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            let newReview = await response.json();


            // Display the new review instantly
            displayReview(newReview);

            displayMessage('Review sent successfully!', 'success');

            rateButton.removeAttribute('data-bs-toggle');
            rateButton.removeAttribute('data-bs-target');

            setTimeout(function () {
                $('#review-modal').modal('hide');
            }, TIMEOUT_DURATION);

            previousReviewText = newReview.text;

        } catch (error) {
            console.error(error);

            displayMessage('Error sending review.', 'danger');
        }
    }

    const form = document.getElementById("review-form");
    form.addEventListener('submit', handleSubmit);
});

function displayReview(newReview) {
    const reviewSection = document.querySelector('.place-review');

    updateReviewText(reviewSection, newReview);
    updateReviewTimestamp(reviewSection, newReview);
    updateReviewButtons(reviewSection, newReview);
    const textarea = document.querySelector("#review");
    resizeTextArea(textarea);
}

function updateReviewText(reviewSection, newReview) {
    const reviewTextElement = reviewSection.querySelector('h2');
    const reviewContentElement = reviewSection.querySelector('.review-text-container textarea'); // Updated selector
    const textarea = document.querySelector("#review-text-field");

    reviewTextElement.textContent = newReview ? 'Your Review' : 'Review';
    reviewContentElement.textContent = newReview ? newReview.text : 'Rate to add your review!';
    textarea.value = newReview ? newReview.text : '';
}

function updateReviewTimestamp(reviewSection, newReview) {
    const reviewContainer = reviewSection.querySelector('.review-text-container');
    let timestampElement = reviewContainer.querySelector('.timestamp');

    if (newReview) {
        if (!timestampElement) {
            timestampElement = document.createElement('p');
            timestampElement.classList.add('timestamp');
            reviewContainer.appendChild(timestampElement);
        }

        timestampElement.textContent = new Intl.DateTimeFormat('en-US', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false // Use 24-hour format
        }).format(new Date(newReview.timestamp));
    } else {
        if (timestampElement) {
            reviewContainer.removeChild(timestampElement);
        }
    }
}

function updateReviewButtons(reviewSection, newReview) {
    const reviewContainer = reviewSection.querySelector('.review-container');
    let buttonsContainer = reviewContainer.querySelector('.review-buttons-container');

    // If the container doesn't exist, create it
    if (!buttonsContainer) {
        buttonsContainer = document.createElement('div');
        buttonsContainer.classList.add('review-buttons-container');
        reviewContainer.appendChild(buttonsContainer);
    }

    let editButton = buttonsContainer.querySelector('.edit-button');
    let deleteButton = buttonsContainer.querySelector('.delete-button');

    if (newReview) {
        updateButton(buttonsContainer, editButton, 'fa-pencil-alt', function () {
            $('#review-modal').modal('show');
        }, ['button', 'edit-button']);

        updateButton(buttonsContainer, deleteButton, 'fa-times', function () {
            deleteReview();
        }, ['button', 'delete-button']);
    } else {
        removeElementIfExist(editButton);
        removeElementIfExist(deleteButton);
        removeElementIfExist(buttonsContainer);
    }
}

function updateButton(buttonsContainer, button, iconClass, clickHandler, classes = []) {
    if (!button) {
        button = document.createElement('button');
        button.classList.add('btn', ...classes); // Adding the specified classes

        // Create an icon element and append it to the button
        const icon = document.createElement('i');
        icon.classList.add('fas', iconClass); // Bootstrap Icon classes
        button.appendChild(icon);

        button.addEventListener('click', clickHandler);
        buttonsContainer.appendChild(button);
    }
}

function removeElementIfExist(element) {
    if (element) {
        element.parentNode.removeChild(element);
    }
}

// Function to handle deleting the review (replace with your actual endpoint)
// Function to handle deleting the review
async function deleteReview() {
    try {
        const response = await fetch(`/api/v1/places/${placeId}/delete-review`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        displayReview();
        const rateButton = document.querySelector("#rate");
        rateButton.setAttribute('data-bs-toggle', 'modal');
        rateButton.setAttribute('data-bs-target', '#review-modal');
        const textarea = document.querySelector("#review-text-field");
        textarea.value = '';
        previousReviewText = '';
    } catch (error) {
        console.error(error);
    }
}

function displayMessage(message, type) {
    const charCountElement = document.querySelector('#charCount');

    charCountElement.innerHTML = `<div class="alert alert-${type}" role="alert">${message}</div>`;

    setTimeout(function () {
        charCountElement.innerHTML = '';
    }, TIMEOUT_DURATION);
}

function updateCharCountAndResize(maxLength) {
    const textarea = document.querySelector("#review-text-field");
    const charCount = document.querySelector("#charCount");
    const remainingChars = maxLength - textarea.value.length;
    charCount.textContent = "Characters remaining: " + remainingChars;
    textarea.style.height = textarea.scrollHeight + "px";
}

function resizeTextArea(textarea) {
    textarea.style.height = textarea.scrollHeight + "px";
}

