let suggestionsData = [];

window.addEventListener('load', async () => {
    try {
        const response = await fetch('/api/v1/interests/suggestions');
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
    const suggestionContainer = document.querySelector('#suggestionList');
    suggestionContainer.innerHTML = '';

    suggestionsData.forEach(suggestion => {
        const div = document.createElement('div');
        div.classList.add('interest-card');

        const link = document.createElement('a');
        link.classList.add('interest-card-link');
        link.href = `/interests/${suggestion.id}`;

        const innerDiv = document.createElement('div');

        const imgWrapper = document.createElement('div');
        imgWrapper.classList.add('img-wrapper');

        const interestTitle = document.createElement('div');
        interestTitle.classList.add('interest-title');

        const paragraph = document.createElement('p');
        paragraph.textContent = `${suggestion.name} (${suggestion.rating})`;

        interestTitle.appendChild(paragraph);
        innerDiv.appendChild(imgWrapper);
        innerDiv.appendChild(interestTitle);
        link.appendChild(innerDiv);
        div.appendChild(link);

        suggestionContainer.appendChild(div);
    });

    document.getElementById('suggestions').style.display = 'block';
}

function getSuggestions(query) {
    const suggestionContainer = document.querySelector('#suggestionList');
    suggestionContainer.innerHTML = '';

    if (!isEmptyOrSpaces(query)) {
        const filteredSuggestions = suggestionsData.filter(suggestion =>
            suggestion.name.toLowerCase().includes(query.toLowerCase())
        );

        filteredSuggestions.forEach(suggestion => {
            const div = document.createElement('div');
            div.classList.add('interest-card');

            const link = document.createElement('a');
            link.classList.add('interest-card-link');
            link.href = `/interests/${suggestion.id}`;

            const innerDiv = document.createElement('div');

            const imgWrapper = document.createElement('div');
            imgWrapper.classList.add('img-wrapper');

            const interestTitle = document.createElement('div');
            interestTitle.classList.add('interest-title');

            const paragraph = document.createElement('p');
            paragraph.textContent = `${suggestion.name} (${suggestion.rating})`;

            interestTitle.appendChild(paragraph);
            innerDiv.appendChild(imgWrapper);
            innerDiv.appendChild(interestTitle);
            link.appendChild(innerDiv);
            div.appendChild(link);
            suggestionContainer.appendChild(div);
        });
    } else {
        loadAllSuggestions();
    }

    document.getElementById('suggestions').style.display = 'block';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}