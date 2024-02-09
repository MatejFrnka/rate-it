function updateUserIcon(username) {
    fetch(`/api/v1/images/users/${username}`)
        .then(response => {
            if (response.ok) {
                return response.blob();
            } else {
                return null;
            }
        })
        .then(blob => {
            if (blob) {
                const url = URL.createObjectURL(blob);
                const imgElements = document.querySelectorAll(`.review-box[data-username="${username}"] .user-icon-rating img`);
                imgElements.forEach(imgElement => {
                    imgElement.src = url;
                });
            }
        })
        .catch(error => {
            console.error('Error fetching profile picture:', error);
        });
}

document.addEventListener("DOMContentLoaded", function() {
    const ratingBoxes = document.querySelectorAll('.review-box');

    ratingBoxes.forEach(function(ratingBox) {
        const username = ratingBox.getAttribute('data-username');
        updateUserIcon(username);
    });
});
