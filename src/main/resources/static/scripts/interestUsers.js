let suggestionsData = [];

window.addEventListener('load', async () => {
    try {
        const interestId = document.getElementById("interest-id").value
        const response = await fetch(`/api/v1/interests/${interestId}/users`);
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

        const kickForm = document.createElement('form');
        kickForm.method = 'post';
        const delInput = document.createElement('input');
        delInput.type = 'hidden';
        delInput.name = '_method';
        delInput.value = 'delete';

        kickForm.action = `/interests/${suggestion.interestId}/admin/users/${suggestion.userId}`;
        kickForm.appendChild(delInput);

        const kickButton = document.createElement('button');
        kickButton.textContent = 'KICK';
        kickButton.classList.add('kick-button');
        kickButton.type = 'submit';
        kickForm.appendChild(kickButton)

        const kickDiv = document.createElement('div');
        kickDiv.classList.add('kick-section');
        // kickDiv.classList.add('col-3');

        kickDiv.appendChild(kickForm);

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
        record.appendChild(kickDiv);

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

            const kickForm = document.createElement('form');
            kickForm.method = 'post';
            const delInput = document.createElement('input');
            delInput.type = 'hidden';
            delInput.name = '_method';
            delInput.value = 'delete';

            kickForm.action = `/interests/${suggestion.interestId}/admin/users/${suggestion.userId}`;
            kickForm.appendChild(delInput);

            const kickButton = document.createElement('button');
            kickButton.textContent = 'KICK';
            kickButton.classList.add('kick-button');
            kickButton.type = 'submit';
            kickForm.appendChild(kickButton)

            const kickDiv = document.createElement('div');
            kickDiv.classList.add('kick-section');
            // kickDiv.classList.add('col-3');

            kickDiv.appendChild(kickForm);

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
            record.appendChild(kickDiv);

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