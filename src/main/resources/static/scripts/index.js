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

        data = await Promise.all(jsonData.map(async (interest) => {
            interest.imageUrl = await fetchInterestImageUrl(interest);
            return interest;
        }));

        loadInterests();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
}

function loadInterests(query, category) {
    const container = document.querySelector('#suggestionList');
    container.innerHTML = '';
    const template = document.getElementsByTagName('template')[0];
    let dataSet = data;

    if (typeof query !== 'undefined' && query !== null && !isEmptyOrSpaces(query)) {
        dataSet = data.filter(record => record.name.toLowerCase().includes(query.toLowerCase()));
    }
    if (typeof category !== 'undefined' && category !== null) {
        dataSet = dataSet.filter(record => record.categoryIds.includes(category));
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

function filterInterests(checkbox) {
    let searchBar = document.querySelector('.search');
    if (typeof checkbox === 'undefined' || checkbox === null) {
        checkbox = getCheckedFilter();
    }
    if (checkbox !== null && checkbox.checked) {
        uncheckOtherCheckboxes(checkbox)
        loadInterests(searchBar.value, Number(checkbox.value));
    } else {
        loadInterests(searchBar.value, null);
    }
}

function uncheckOtherCheckboxes(checkbox) {
    let checkboxes = document.querySelectorAll('.sort-checkbox');
    checkboxes.forEach(otherCheckbox => {
        if (otherCheckbox !== checkbox) {
            otherCheckbox.checked = false;
        }
    });
}

function getCheckedFilter() {
    let checkboxes = document.querySelectorAll('.sort-checkbox');
    let checked = null;
    checkboxes.forEach(c => {
        if (c.checked && checked === null) {
            checked = c;
        }
    });
    return checked;
}


