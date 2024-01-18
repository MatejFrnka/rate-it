let data = [];

window.addEventListener('load', async () => {
    try {
        const response = await fetch(`/api/v1/interests/suggestions`);
        const jsonData = await response.json();
        data = jsonData;

        data = await Promise.all(jsonData.map(async (interest) => {
            interest.imageUrl = await fetchInterestImageUrl(interest);
            return interest;
        }));

        loadInterests()
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
})

document.addEventListener('DOMContentLoaded', () => {
    loadInterests()
});

function loadInterests(query) {
    const container = document.querySelector('#suggestionList');
    container.innerHTML = '';
    const template = document.getElementsByTagName('template')[0];
    let dataSet = data;

    if (typeof query !== 'undefined' && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(record => record.name.toLowerCase().includes(query.toLowerCase()));
    }

    dataSet.forEach(record => {
        const clone = template.content.cloneNode(true);
        clone.querySelector('.interest-card-link').href = `/interests/${record.id}`;
        clone.querySelector('p').textContent = `${record.name} (${record.likes})`;
        clone.querySelector('.img-wrapper img').src = record.imageUrl;
        container.appendChild(clone);
    })
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}

