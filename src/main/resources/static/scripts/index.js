let suggestionsData = [];

window.addEventListener('load', () => {
    fetch('/getAllSuggestions')
        .then(response => response.json())
        .then(data => {
            suggestionsData = data;
        })
        .catch(error => {
            console.error('Error fetching suggestions:', error);
        });
});

function getSuggestions(query) {
    const suggestionList = document.getElementById('suggestionList');
    suggestionList.innerHTML = '';

    // Check if the query is empty
    if (!query.trim()) {
        document.getElementById('suggestions').style.display = 'none';
        return;
    }

    const filteredSuggestions = suggestionsData.filter(suggestion =>
        suggestion.toLowerCase().includes(query.toLowerCase())
    );

    filteredSuggestions.forEach(suggestion => {
        const li = document.createElement('li');
        li.textContent = suggestion;
        li.addEventListener('click', () => {
            document.getElementById('search').value = suggestion;
            document.getElementById('suggestions').style.display = 'none';
        });
        suggestionList.appendChild(li);
    });

    document.getElementById('suggestions').style.display = 'block';
}

document.addEventListener('click', function (e) {
    const suggestions = document.getElementById('suggestions');
    if (!e.target.matches('#search') && !e.target.matches('#suggestions *')) {
        suggestions.style.display = 'none';
    }
});


