let data = [];

window.addEventListener('load', async () => {
    try {
        const interestId = document.getElementById("interest-id").value
        const endpoint = document.getElementById('api').value
        const response = await fetch(`/api/v1/interests/${interestId}/${endpoint}`);
        const jsonData = await response.json();
        data = jsonData;
        loadResults();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
})

document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.split('/').pop() === 'invite') getInviteType();
    loadResults();
});

function loadResults(query) {
    const container = document.querySelector('#interest-users-container');
    container.innerHTML = '';
    const template = document.getElementsByTagName('template')[0];
    let lastLetter = null;
    let section = null;
    let dataSet = data;

    if (typeof query !== 'undefined' && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(record => record.userName.toLowerCase().includes(query.toLowerCase()));
    }

    dataSet.forEach(record => {
        let currentLetter = record.userName.charAt(0).toUpperCase();
        const clone = template.content.cloneNode(true);

        if (currentLetter !== lastLetter) {
            const h3 = document.createElement('h3');
            h3.textContent = currentLetter;
            container.appendChild(h3);

            section = document.createElement('section');
            section.classList.add('interest-list-section');
            container.appendChild(section);
            lastLetter = currentLetter;
        }
        let listRecords = clone.querySelectorAll('.list-record');
        let forms = listRecords[listRecords.length - 1].querySelectorAll('form');
        forms.forEach(form => form.action = `/interests/${record.interestId}/admin/users/${record.userId}`);

        clone.querySelector('h5').textContent = record.userName;
        section.appendChild(clone);
    })
}

function getInviteType() {
    const inviteForm = document.getElementById('toggle')
    const inviteType = document.getElementById('type')
    if (inviteForm.checked) {
        inviteType.value = 'username'
    } else {
        inviteType.value = 'email'
    }
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}