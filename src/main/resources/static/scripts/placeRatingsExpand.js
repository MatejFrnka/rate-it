document.addEventListener('DOMContentLoaded', function () {
    const expandButtons = document.getElementsByClassName('expand-button');

    for (let i = 0; i < expandButtons.length; i++) {
        expandButtons[i].addEventListener('click', function () {
            let userName = this.id.replace('button-show-ratings-', '');
            toggleRatings(userName);

            const textarea = document.querySelector('#textarea_' + userName);
            if (textarea != null) {
                resizeTextArea(textarea);
            }
        });
    }
});

function toggleRatings(userName) {
    const ratingsList = document.querySelector('#review_' + userName);
    const expandButton = document.querySelector('#button-show-ratings-' + userName);

    ratingsList.style.display = (ratingsList.style.display === 'none') ? 'flex' : 'none';

    if (ratingsList.style.display === 'none') {
        expandButton.querySelector('img').src = '/icons/right-arrow.svg';
    } else {
        expandButton.querySelector('img').src = '/icons/down-arrow.svg';
    }
}

function resizeTextArea(textarea) {
    textarea.style.height = textarea.scrollHeight + "px";
}
