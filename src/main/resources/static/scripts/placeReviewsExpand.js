document.addEventListener('DOMContentLoaded', function () {
    const expandButtons = document.getElementsByClassName('expand-button');

    for (let i = 0; i < expandButtons.length; i++) {
        expandButtons[i].addEventListener('click', function () {
            let name = this.id.replace('button-show-ratings-', '');
            toggleRatings(name);

            const textarea = document.querySelector('#textarea_' + name);
            if (textarea != null) {
                resizeTextArea(textarea);
            }
        });
    }
});

function toggleRatings(name) {
    const ratingsList = document.querySelector('#review_' + name);
    const expandButton = document.querySelector('#button-show-ratings-' + name);

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
