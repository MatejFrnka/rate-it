let suggestionsData = [];

window.addEventListener('load', async () => {
    try {
        const response = await fetch('/api/v1/interests/my');
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

    let lastLetter = null;
    let section = null;

    suggestionsData.forEach(suggestion => {
        const currentLetter = suggestion.name.charAt(0).toUpperCase();

        if (currentLetter !== lastLetter) {
            const letter = document.createElement('h3');
            letter.textContent = currentLetter;
            suggestionContainer.appendChild(letter);

            section = document.createElement('section');
            section.classList.add('interest-list-section');
            suggestionContainer.appendChild(section);

            lastLetter = currentLetter;
        }

        const starIcon = document.createElement("img");
        starIcon.src = "/icons/star.svg";
        starIcon.alt = "following";

        const creatorIcon = document.createElement("img");
        creatorIcon.src = "/icons/interest-user.svg";
        creatorIcon.alt = "Interest creator";

        const creatorName = document.createTextNode(suggestion.creator);
        const followingNumber = document.createTextNode(suggestion.followers);

        const entry = document.createElement('div');
        entry.classList.add('interest-list-entry');

        const interestName = document.createElement('h5');
        interestName.textContent = suggestion.name;

        const interestCreatorLikes = document.createElement('div');
        interestCreatorLikes.classList.add('interest-creator-likes');

        const followers = document.createElement('h6');
        followers.appendChild(followingNumber);
        followers.appendChild(starIcon);

        const creator = document.createElement('h6');
        creator.appendChild(creatorName);
        creator.appendChild(creatorIcon);

        interestCreatorLikes.appendChild(followers);
        interestCreatorLikes.appendChild(creator);

        entry.appendChild(interestName);
        entry.appendChild(interestCreatorLikes);

        entry.addEventListener('click', () => {
            window.location.href = `/interests/${suggestion.id}`;
        });

        section.appendChild(entry);
    });

    document.getElementById('suggestions').style.display = 'block';
}

function getSuggestions(query) {
    const suggestionContainer = document.querySelector('#my-interests-container');
    suggestionContainer.innerHTML = '';

    let lastLetter = null;
    let section = null;

    if (!isEmptyOrSpaces(query)) {
        const filteredSuggestions = suggestionsData.filter(suggestion =>
            suggestion.name.toLowerCase().includes(query.toLowerCase())
        );

        filteredSuggestions.forEach(suggestion => {
            const currentLetter = suggestion.name.charAt(0).toUpperCase();

            if (currentLetter !== lastLetter) {
                const letter = document.createElement('h3');
                letter.textContent = currentLetter;
                suggestionContainer.appendChild(letter);

                section = document.createElement('section');
                section.classList.add('interest-list-section');
                suggestionContainer.appendChild(section);

                lastLetter = currentLetter;
            }

            const starIcon = document.createElement("img");
            starIcon.src = "/icons/star.svg";
            starIcon.alt = "following";

            const creatorIcon = document.createElement("img");
            creatorIcon.src = "/icons/interest-user.svg";
            creatorIcon.alt = "Interest creator";

            const creatorName = document.createTextNode(suggestion.creator);
            const followingNumber = document.createTextNode(suggestion.followers);

            const entry = document.createElement('div');
            entry.classList.add('interest-list-entry');

            const interestName = document.createElement('h5');
            interestName.textContent = suggestion.name;

            const interestCreatorLikes = document.createElement('div');
            interestCreatorLikes.classList.add('interest-creator-likes');

            const followers = document.createElement('h6');
            followers.appendChild(followingNumber);
            followers.appendChild(starIcon);

            const creator = document.createElement('h6');
            creator.appendChild(creatorName);
            creator.appendChild(creatorIcon);

            interestCreatorLikes.appendChild(followers);
            interestCreatorLikes.appendChild(creator);

            entry.appendChild(interestName);
            entry.appendChild(interestCreatorLikes);

            entry.addEventListener('click', () => {
                window.location.href = `/interests/${suggestion.id}`;
            });

            section.appendChild(entry);
        });
    } else {
        loadAllSuggestions();
    }

    document.getElementById('suggestions').style.display = 'block';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}