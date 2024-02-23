//const isliked comes from thymeleaf fragment like-button

document.addEventListener('DOMContentLoaded', function () {
    const button = document.querySelector('.like-button');
    updateLikeButton(button, isliked);
    button.addEventListener('click', () => likeInterest(button));
});

async function likeInterest(button) {
    const isLiked = button.classList.contains('liked');

    try {
        const response = await fetch(`/api/v1/interests/${interestId}/like`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ liked: !isLiked })
        });

        const data = await response.json();
        updateLikeButton(button, data.liked);
    } catch (error) {
        console.error('Error:', error);
    }
}

function updateLikeButton(button, isLiked) {
    const iconClass = isLiked ? 'fa-solid fa-heart' : 'fa-regular fa-heart';
    const titleText = isLiked ? 'Liked' : 'Not Liked';

    button.classList.toggle('liked', isLiked);
    button.innerHTML = `<i class="${iconClass}"></i>`;
    button.title = titleText;
}