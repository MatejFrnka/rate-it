let data = [];
let usersCoords;

document.addEventListener('DOMContentLoaded', async () => {
    initializeCheckboxesSplide()
    document.querySelector('.loader').style.display = 'block';
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
    usersCoords = [position.coords.latitude, position.coords.longitude];
    try {
        await fetchData();
        setNumberOfTitleLinesForAll();
    } catch (error) {
        console.error('Error fetching suggestions:', error);
    }
}

async function error() {
    console.log("Unable to retrieve your location");
    await fetchData();
    setNumberOfTitleLinesForAll();
}

async function fetchData() {
    try {
        let url = '/api/v1/interests/suggestions';

        // Append query parameters if latitude and longitude are provided
        if (usersCoords !== undefined) {
            url += `?latitude=${usersCoords[0]}&longitude=${usersCoords[1]}`;
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

    dataSet.forEach(interest => {
        const clone = document.importNode(template.content, true);

        const elements = {
            interestLink: clone.querySelector('.discover-interest'),
            interestImg: clone.querySelector('.discover-interest img'),
            titleH3: clone.querySelector('.discover-interest-title h3'),
            distanceSpan: clone.querySelector('.discover-interest-distance span'),
            interestLikes: clone.querySelector('.discover-interest-like-value'),
            placesAmount: clone.querySelector('.discover-interest-places-amount'),
            interestDescription: clone.querySelector('.discover-interest-content p')
        };

        elements.interestLink.href = `/interests/${interest.id}`;
        elements.interestImg.src = interest.imageUrl;
        elements.titleH3.textContent = interest.name;
        if (!isNaN(interest.distanceKm) && interest.distanceKm > 0) {
            elements.distanceSpan.textContent = interest.distanceKm.toFixed(1) + ' km';
        }
        elements.interestLikes.textContent = interest.likes;
        elements.placesAmount.textContent = interest.places;
        elements.interestDescription.textContent = interest.description;

        container.appendChild(clone);
    })
    document.querySelector('.loader').style.display = 'none';
}

function isEmptyOrSpaces(str) {
    return str === null || str.match(/^ *$/) !== null;
}

function setNumberOfTitleLinesForAll() {
    const discoverInterestElements = document.querySelectorAll('.discover-interest');
    discoverInterestElements.forEach((element) => {
        setNumberOfTitleLines(element);
    });
}

function setNumberOfTitleLines(discoverInterestElement) {
    const interestTitle = discoverInterestElement.querySelector('.discover-interest-title h3');
    const titleStyle = window.getComputedStyle(interestTitle);
    const titleLines = Math.ceil(parseInt(titleStyle.height) / parseInt(titleStyle.lineHeight));
    interestTitle.style.setProperty('--title-lines', titleLines.toString());

    document.documentElement.style.setProperty('--title-lines', titleLines.toString());

    const interestDesc = discoverInterestElement.querySelector('.discover-interest-content p');
    interestDesc.style.setProperty('--title-lines', titleLines.toString());
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