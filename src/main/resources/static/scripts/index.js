let data = [];
let latitude = null;
let longitude = null;

document.addEventListener('DOMContentLoaded', async () => {
    try {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(success, error);
        } else {
            console.log("Geolocation is not supported");
            await fetchData();
        }
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
});

async function success(position) {
    latitude = position.coords.latitude;
    longitude = position.coords.longitude;

    try {
        await fetchData();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
}

async function error() {
    console.log("Unable to retrieve your location");
    await fetchData();
}

async function fetchData() {
    try {
        let url = '/api/v1/interests/suggestions';

        // Append query parameters if latitude and longitude are provided
        if (latitude !== null && longitude !== null) {
            url += `?latitude=${latitude}&longitude=${longitude}`;
            console.log(url);
        }

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${response.statusText}`);
        }

        const jsonData = await response.json();

        const data = await Promise.all(jsonData.map(async (interest) => {
            interest.imageUrl = await fetchInterestImageUrl(interest);
            return interest;
        }));

        loadInterests(data);
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
}

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