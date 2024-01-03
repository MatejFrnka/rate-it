let suggestionsData = [];

window.addEventListener('load', async () => {
    try {
        const response = await fetch('/api/v1/interests/suggestions/my');
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
    const suggestionContainer = document.querySelector('#my-interests-container');
    suggestionContainer.innerHTML = '';

    suggestionsData.forEach(suggestion => {
        const starIcon = document.createElement("img");
        starIcon.src = "/icons/star.svg";
        starIcon.alt = "following";

        const creatorIcon = document.createElement("img");
        creatorIcon.src = "/icons/interest-user.svg";
        creatorIcon.alt = "Interest creator";

        const creatorName = document.createTextNode(suggestion.creator);
        const followingNumber = document.createTextNode(suggestion.followers);

        const letter = document.createElement("h3");
        letter.textContent = suggestion.name.charAt(0).toUpperCase();

        const section = document.createElement('section');
        section.classList.add('interest-list-section');

        const entry = document.createElement('div');
        entry.classList.add('interest-list-entry');

        const interestName = document.createElement('h5');
        interestName.textContent = suggestion.name;

        const interestCreatorLikes = document.createElement('div');
        interestCreatorLikes.classList.add('interest-creator-likes');

        const followers = document.createElement('h6');

        const creator = document.createElement('h6');

        followers.appendChild(followingNumber);
        followers.appendChild(starIcon);

        creator.appendChild(creatorName);
        creator.appendChild(creatorIcon);

        suggestionContainer.appendChild(letter);
        interestCreatorLikes.appendChild(followers);
        interestCreatorLikes.appendChild(creator);
        entry.appendChild(interestName);
        entry.appendChild(interestCreatorLikes);
        section.appendChild(entry);
        suggestionContainer.appendChild(section)

        entry.addEventListener('click', () => {
            window.location.href = `/interests/${suggestion.id}`
        })
    });

    document.getElementById('suggestions').style.display = 'block';
}

function getSuggestions(query) {
    const suggestionContainer = document.querySelector('#my-interests-container');
    suggestionContainer.innerHTML = '';

    if (!isEmptyOrSpaces(query)) {
        const filteredSuggestions = suggestionsData.filter(suggestion =>
            suggestion.name.toLowerCase().includes(query.toLowerCase())
        );

        filteredSuggestions.forEach(suggestion => {
            const starIcon = document.createElement("img");
            starIcon.src = "/icons/star.svg";
            starIcon.alt = "following";

            const creatorIcon = document.createElement("img");
            creatorIcon.src = "/icons/interest-user.svg";
            creatorIcon.alt = "Interest creator";

            const creatorName = document.createTextNode(suggestion.creator);
            const followingNumber = document.createTextNode(suggestion.followers);

            const letter = document.createElement("h3");
            letter.textContent = suggestion.name.charAt(0).toUpperCase();

            const section = document.createElement('section');
            section.classList.add('interest-list-section');

            const entry = document.createElement('div');
            entry.classList.add('interest-list-entry');

            const interestName = document.createElement('h5');
            interestName.textContent = suggestion.name;

            const interestCreatorLikes = document.createElement('div');
            interestCreatorLikes.classList.add('interest-creator-likes');

            const followers = document.createElement('h6');

            const creator = document.createElement('h6');

            followers.appendChild(followingNumber);
            followers.appendChild(starIcon);

            creator.appendChild(creatorName);
            creator.appendChild(creatorIcon);

            suggestionContainer.appendChild(letter);
            interestCreatorLikes.appendChild(followers);
            interestCreatorLikes.appendChild(creator);
            entry.appendChild(interestName);
            entry.appendChild(interestCreatorLikes);
            section.appendChild(entry);
            suggestionContainer.appendChild(section)

            entry.addEventListener('click', () => {
                window.location.href = `/interests/${suggestion.id}`
            })
        });
    } else {
        loadAllSuggestions();
    }

    document.getElementById('suggestions').style.display = 'block';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}