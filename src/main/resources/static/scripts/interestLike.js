const id = extractInterestIdFromUrl();

document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.like-button');
    buttons.forEach(function (button) {
        button.addEventListener('click', function () {
            const isLiked = button.classList.contains('liked');
            updateLikeButton(button, !isLiked);
        });
    });
});

function likeInterest(button, value) {
    fetch(`/api/v1/interests/${id}/like`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({liked: value})
    })
    .then(response => response.json())
    .then(data => {updateLikeButton(button, data.liked)})
    .catch(error => {'Error:', error})
}

function updateLikeButton(button, isLiked) {
    if(isLiked) {
        button.classList.add('liked');
        button.classList.remove('disliked');
    } else {
        button.classList.add('disliked')
        button.classList.remove('liked')
    }
}

function extractInterestIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.indexOf('interests') + 1];
}