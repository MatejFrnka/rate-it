let suggestionsData = [];

window.addEventListener('load', async () => {
    try {
        const interestId = document.getElementById("interest-id").value
        const response = await fetch(`/api/v1/interests/${interestId}/applications`);
        const data = await response.json();
        suggestionsData = data;
        loadAllSuggestions();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadAllSuggestions();
});

function loadAllSuggestions() {
    const suggestionContainer = document.querySelector('#interest-users-container');
    suggestionContainer.innerHTML = '';

    let lastLetter = null;
    let section = null;

    suggestionsData.forEach(suggestion => {
        const currentLetter = suggestion.userName.charAt(0).toUpperCase();

        if (currentLetter !== lastLetter) {
            const letter = document.createElement('h3');
            letter.textContent = currentLetter;
            suggestionContainer.appendChild(letter);

            section = document.createElement('section');
            section.classList.add('interest-list-section');
            suggestionContainer.appendChild(section);

            lastLetter = currentLetter;
        }

        const acceptForm = document.createElement('form');
        acceptForm.method = 'post';
        const putInput = document.createElement('input');
        putInput.type = 'hidden';
        putInput.name = '_method';
        putInput.value = 'put';

        acceptForm.action = `/interests/${suggestion.interestId}/admin/users/${suggestion.userId}`;
        acceptForm.appendChild(putInput);

        const buttonAccept = document.createElement('img');
        buttonAccept.src = "/icons/accept.svg";

        const acceptButton = document.createElement('button');
        acceptButton.id = 'accept-button';
        acceptButton.appendChild(buttonAccept);
        acceptButton.classList.add('role-option-button');
        acceptButton.type = 'submit';
        acceptForm.appendChild(acceptButton)

        const roleDiv = document.createElement('div');
        roleDiv.classList.add('role-section');

        roleDiv.appendChild(acceptForm);



        const rejectForm = document.createElement('form');
        rejectForm.method = 'post';
        const delInput = document.createElement('input');
        delInput.type = 'hidden';
        delInput.name = '_method';
        delInput.value = 'delete';

        rejectForm.action = `/interests/${suggestion.interestId}/admin/users/${suggestion.userId}`;
        rejectForm.appendChild(delInput);

        const buttonCross = document.createElement('img');
        buttonCross.src = "/icons/cross.svg";

        const rejectButton = document.createElement('button');
        rejectButton.id = 'kick-button';
        rejectButton.appendChild(buttonCross);
        rejectButton.classList.add('role-option-button');
        rejectButton.type = 'submit';
        rejectForm.appendChild(rejectButton)

        roleDiv.appendChild(rejectForm);


        const entry = document.createElement('div');
        entry.classList.add('user-list-entry');
        entry.classList.add('col-8');

        const userIcon = document.createElement('img');
        userIcon.src = "/icons/list-user.svg";
        userIcon.alt = "User";

        const userName = document.createElement('h5');
        userName.textContent = suggestion.userName;

        entry.appendChild(userIcon);
        entry.appendChild(userName);

        const record = document.createElement('div');
        record.classList.add('list-record');

        record.appendChild(entry);
        record.appendChild(roleDiv);

        section.appendChild(record);
    });

    document.getElementById('suggestions').style.display = 'block';
}

function getSuggestions(query) {
    const suggestionContainer = document.querySelector('#interest-users-container');
    suggestionContainer.innerHTML = '';

    let lastLetter = null;
    let section = null;

    if (!isEmptyOrSpaces(query)) {
        const filteredSuggestions = suggestionsData.filter(suggestion =>
            suggestion.userName.toLowerCase().includes(query.toLowerCase())
        );

        filteredSuggestions.forEach(suggestion => {
            const currentLetter = suggestion.userName.charAt(0).toUpperCase();

            if (currentLetter !== lastLetter) {
                const letter = document.createElement('h3');
                letter.textContent = currentLetter;
                suggestionContainer.appendChild(letter);

                section = document.createElement('section');
                section.classList.add('interest-list-section');
                suggestionContainer.appendChild(section);

                lastLetter = currentLetter;
            }

            const acceptForm = document.createElement('form');
            acceptForm.method = 'post';
            const putInput = document.createElement('input');
            putInput.type = 'hidden';
            putInput.name = '_method';
            putInput.value = 'put';

            acceptForm.action = `/interests/${suggestion.interestId}/admin/users/${suggestion.userId}`;
            acceptForm.appendChild(putInput);

            const buttonAccept = document.createElement('img');
            buttonAccept.src = "/icons/accept.svg";

            const acceptButton = document.createElement('button');
            acceptButton.id = 'accept-button';
            acceptButton.appendChild(buttonAccept);
            acceptButton.classList.add('role-option-button');
            acceptButton.type = 'submit';
            acceptForm.appendChild(acceptButton)

            const roleDiv = document.createElement('div');
            roleDiv.classList.add('role-section');

            roleDiv.appendChild(acceptForm);



            const rejectForm = document.createElement('form');
            rejectForm.method = 'post';
            const delInput = document.createElement('input');
            delInput.type = 'hidden';
            delInput.name = '_method';
            delInput.value = 'delete';

            rejectForm.action = `/interests/${suggestion.interestId}/admin/users/${suggestion.userId}`;
            rejectForm.appendChild(delInput);

            const buttonCross = document.createElement('img');
            buttonCross.src = "/icons/cross.svg";

            const rejectButton = document.createElement('button');
            rejectButton.id = 'kick-button';
            rejectButton.appendChild(buttonCross);
            rejectButton.classList.add('role-option-button');
            rejectButton.type = 'submit';
            rejectForm.appendChild(rejectButton)

            roleDiv.appendChild(rejectForm);


            const entry = document.createElement('div');
            entry.classList.add('user-list-entry');
            entry.classList.add('col-8');

            const userIcon = document.createElement('img');
            userIcon.src = "/icons/list-user.svg";
            userIcon.alt = "User";

            const userName = document.createElement('h5');
            userName.textContent = suggestion.userName;

            entry.appendChild(userIcon);
            entry.appendChild(userName);

            const record = document.createElement('div');
            record.classList.add('list-record');

            record.appendChild(entry);
            record.appendChild(roleDiv);

            section.appendChild(record);
        });
    } else {
        loadAllSuggestions();
    }

    document.getElementById('suggestions').style.display = 'block';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}