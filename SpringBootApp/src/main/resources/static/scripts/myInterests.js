let data = [];

window.addEventListener('load', async () => {
    try {
        const response = await fetch(`/api/v1/interests/my`);
        const jsonData = await response.json();
        data = jsonData;
        loadResults();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
})

document.addEventListener('DOMContentLoaded', () => {
    loadResults();
});

function loadResults(query) {
    const container = document.querySelector('#my-interests-container');
    container.innerHTML = '';
    const template = document.getElementsByTagName('template')[0];
    let lastLetter = null;
    let section = null;
    let dataSet = data;

    if (typeof query !== 'undefined' && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(interest => interest.name.toLowerCase().includes(query.toLowerCase()));
    }

    dataSet.forEach(interest => {
        let currentLetter = interest.name.charAt(0).toUpperCase();
        const clone= template.content.cloneNode(true);
        if (currentLetter !== lastLetter) {
            const h3 = document.createElement('h3');
            h3.textContent = currentLetter;
            container.appendChild(h3);

            section = document.createElement('section');
            section.classList.add('interest-list-section');
            container.appendChild(section);
            lastLetter = currentLetter;
        }
        clone.querySelector('h5').textContent = interest.name;
        const followers = document.createTextNode(`${interest.followers}`);
        const creator = document.createTextNode(`${interest.creator}`);
        const h6s = clone.querySelector('.interest-creator-likes').querySelectorAll('h6');
        clone.querySelector('.interest-list-entry').addEventListener('click', () => {
            window.location.href = `/interests/${interest.id}`
        })
        h6s[0].insertBefore(creator, h6s[0].firstChild);
        h6s[1].insertBefore(followers, h6s[1].firstChild);
        section.appendChild(clone);
    })
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}