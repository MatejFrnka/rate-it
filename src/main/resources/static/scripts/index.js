let suggestionsData = [];

window.addEventListener('load', async () => {
    try {
        const response = await fetch('/getAllSuggestions');
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
    const suggestionList = document.getElementById('suggestionList');
    suggestionList.innerHTML = '';

    suggestionsData.forEach(suggestion => {
        const li = document.createElement('li');
        li.textContent = `${suggestion.name} (Rating: ${suggestion.rating})`;
        li.addEventListener('click', () => {
            window.location.href = `/interests/${suggestion.id}`;
        });
        suggestionList.appendChild(li);
    });

    document.getElementById('suggestions').style.display = 'block';
}

function getSuggestions(query) {
    const suggestionList = document.getElementById('suggestionList');
    suggestionList.innerHTML = '';

    if (!isEmptyOrSpaces(query)) {

        const filteredSuggestions = suggestionsData.filter(suggestion =>
            suggestion.name.toLowerCase().includes(query.toLowerCase())
        );

        filteredSuggestions.forEach(suggestion => {
            const li = document.createElement('li');
            li.textContent = `${suggestion.name} (Rating: ${suggestion.rating})`;
            li.addEventListener('click', () => {
                window.location.href = `/interests/${suggestion.id}`;
            });
            suggestionList.appendChild(li);
        });

    } else {
        loadAllSuggestions();
    }


    document.getElementById('suggestions').style.display = 'block';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}