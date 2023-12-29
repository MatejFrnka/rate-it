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

// function loadAllSuggestions() {
//     const suggestionList = document.getElementById('suggestionList');
//     suggestionList.innerHTML = '';
//
//     suggestionsData.forEach(suggestion => {
//         const li = document.createElement('li');
//         li.textContent = `${suggestion.name} (Likes: ${suggestion.rating})`;
//         li.addEventListener('click', () => {
//             window.location.href = `/interests/${suggestion.id}`;
//         });
//         suggestionList.appendChild(li);
//     });
//
//     document.getElementById('suggestions').style.display = 'block';
// }

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
        paragraph.classList.add('interest-name');
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

// function getSuggestions(query) {
//     const suggestionList = document.getElementById('suggestionList');
//     suggestionList.innerHTML = '';
//
//     if (!isEmptyOrSpaces(query)) {
//
//         const filteredSuggestions = suggestionsData.filter(suggestion =>
//             suggestion.name.toLowerCase().includes(query.toLowerCase())
//         );
//
//         filteredSuggestions.forEach(suggestion => {
//             const li = document.createElement('li');
//             li.textContent = `${suggestion.name} (Rating: ${suggestion.rating})`;
//             li.addEventListener('click', () => {
//                 window.location.href = `/interests/${suggestion.id}`;
//             });
//             suggestionList.appendChild(li);
//         });
//
//     } else {
//         loadAllSuggestions();
//     }
//
//
//     document.getElementById('suggestions').style.display = 'block';
// }

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
            paragraph.classList.add('interest-name');
            paragraph.textContent = `${suggestion.name} (${suggestion.rating})`;

            interestTitle.appendChild(paragraph);
            innerDiv.appendChild(imgWrapper);
            innerDiv.appendChild(interestTitle);
            link.appendChild(innerDiv);
            div.appendChild(link);

            suggestionContainer.appendChild(div);

            // Click event listener for redirection
            div.addEventListener('click', () => {
                window.location.href = `/interests/${suggestion.id}`;
            });
        });
    } else {
        loadAllSuggestions();
    }

    document.getElementById('suggestions').style.display = 'block';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}