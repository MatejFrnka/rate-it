function addCriteria(maxLength) {
    const criteriaContainer = document.getElementById('criteriaContainer');
    const div = document.createElement('div');
    const label = document.createElement('label')
    const input = document.createElement('input');
    const removeButton = document.createElement('button');
    const img = document.createElement('img');
    img.src = '/icons/cross.svg';

    label.classList.add('list-record')

    input.type = 'text';
    input.name = 'criteriaNames';
    input.required = true;
    input.classList.add('input-text-field');
    input.maxLength = maxLength;

    removeButton.type = 'button';
    removeButton.textContent = '';
    removeButton.classList.add('role-option-button', 'kick-button')
    removeButton.appendChild(img)
    removeButton.onclick = function () {
        removeCriteria(div);
    };

    if (criteriaContainer.children.length === 0) {
        removeButton.style.display = 'none';
    } else {
        removeButton.style.display = 'flex';
    }

    label.appendChild(input)
    label.appendChild(removeButton);
    div.appendChild(label);
    criteriaContainer.appendChild(div);

    //this currently does not work because criteriaForm id does not exist in the form

    // const criteriaForm = document.getElementById('criteriaForm');
    // criteriaForm.addEventListener('submit', function (event) {
    //     const criteriaInputs = document.querySelectorAll('input[name="criteriaNames"]');
    //     let isValid = true;
    //
    //     criteriaInputs.forEach(function (input) {
    //         if (input.value.trim() === '') {
    //             isValid = false;
    //             input.classList.add('invalid-field');
    //         }
    //     });
    //
    //     if (!isValid) {
    //         event.preventDefault();
    //         alert('Please fill in all criteria fields.');
    //     }
    // });
}

function removeCriteria(div) {
    const criteriaContainer = document.getElementById('criteriaContainer');
    const parentDiv = div || event.currentTarget.closest('div');
    if (parentDiv) {
        if (parentDiv !== criteriaContainer.firstElementChild || criteriaContainer.children.length > 1) {
            parentDiv.remove();
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const removeButtons = document.querySelectorAll('#criteriaContainer button');
    removeButtons.forEach(function (button) {
        button.onclick = function () {
            removeCriteria(null, button.closest('div'));
        };
    });

    disableCategoryIfMax();

    const categoryBoxes = document.querySelectorAll(".sort-checkbox");
    categoryBoxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            disableCategoryIfMax();
        })
    })
});


function disableCategoryIfMax() {
    const checkedBoxes = document.querySelectorAll(".sort-checkbox:checked");
    const uncheckedBoxes = document.querySelectorAll(".sort-checkbox:not(:checked)");

    if (checkedBoxes.length >= MAX_CATEGORIES) {
        uncheckedBoxes.forEach(checkbox => {
            checkbox.disabled = true;
        });
    } else {
        uncheckedBoxes.forEach(checkbox => {
            checkbox.disabled = false;
        });
    }
}

function setRateAccess() {
    const checkbox = document.getElementById('toggle')
    const exclusive = document.getElementById('exclusive')
    //easter egg
    exclusive.value = !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!checkbox.checked;
}
