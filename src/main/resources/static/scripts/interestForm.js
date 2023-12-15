function addCriteria() {
    var criteriaContainer = document.getElementById('criteriaContainer');
    var div = document.createElement('div');
    var input = document.createElement('input');
    var removeButton = document.createElement('button');

    input.type = 'text';
    input.name = 'criteriaNames';

    removeButton.type = 'button';
    removeButton.textContent = 'Remove';
    removeButton.onclick = function () {
        removeCriteria(div);
    };

    if (criteriaContainer.children.length === 0) {
        removeButton.style.display = 'none';
    } else {
        removeButton.style.display = 'inline-block';
    }

    div.appendChild(input);
    div.appendChild(removeButton);
    criteriaContainer.appendChild(div);
}

function removeCriteria(div) {
    var criteriaContainer = document.getElementById('criteriaContainer');
    var parentDiv = div || event.currentTarget.closest('div');
    if (parentDiv) {
        if (parentDiv !== criteriaContainer.firstElementChild || criteriaContainer.children.length > 1) {
            parentDiv.remove();
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    var removeButtons = document.querySelectorAll('#criteriaContainer button');
    removeButtons.forEach(function (button) {
        button.onclick = function () {
            removeCriteria(null, button.closest('div'));
        };
    });
});
