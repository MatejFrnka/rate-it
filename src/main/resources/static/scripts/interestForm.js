function addCriteria() {
    var criteriaContainer = document.getElementById('criteriaContainer');
    var div = document.createElement('div');
    var label = document.createElement('label')
    var input = document.createElement('input');
    var removeButton = document.createElement('button');

    label.classList.add('criterion')

    input.type = 'text';
    input.name = 'criteriaNames';
    input.required = true;
    input.classList.add('input-text-field');

    removeButton.type = 'button';
    removeButton.textContent = '';
    removeButton.classList.add('delete-button')
    removeButton.onclick = function () {
        removeCriteria(div);
    };

    if (criteriaContainer.children.length === 0) {
        removeButton.style.display = 'none';
    } else {
        removeButton.style.display = 'inline-block';
    }

    label.appendChild(input)
    label.appendChild(removeButton);
    div.appendChild(label);
    criteriaContainer.appendChild(div);

    var criteriaForm = document.getElementById('criteriaForm');
    criteriaForm.addEventListener('submit', function (event) {
        var criteriaInputs = document.querySelectorAll('input[name="criteriaNames"]');
        var isValid = true;

        criteriaInputs.forEach(function (input) {
            if (input.value.trim() === '') {
                isValid = false;
                input.classList.add('invalid-field');
            }
        });

        if (!isValid) {
            event.preventDefault();
            alert('Please fill in all criteria fields.');
        }
    });
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
