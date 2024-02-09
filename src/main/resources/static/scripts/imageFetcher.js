const pictureContainer = document.getElementById('picture-container');

if (pictureContainer) {
    const imageId = pictureContainer.dataset.imageId;
    const apiUrl = '/api/v1/images/' + imageId;

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(blob => {
            const imageUrl = URL.createObjectURL(blob);
            pictureContainer.src = imageUrl;
        })
        .catch(error => {
            console.error('Error fetching picture:', error);
            pictureContainer.src = 'https://picsum.photos/500/300';
        });
}

function fetchInterestImageUrl(interest) {
    const imageName = interest.imageName;

    if (imageName == null || imageName === '') {
        console.error('No imageNames found');
        return fetchDefaultInterestImage(interest);
    } else {
        const apiUrl = '/api/v1/images/' + imageName;

        return fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.blob();
            })
            .then(blob => URL.createObjectURL(blob))
            .catch(error => {
                console.error('Error fetching image:', error);
                return fetchDefaultInterestImage(interest);
            });
    }
}


function fetchImageUrl(place, interest) {
    const imageNames = place.imageNames;

    if (imageNames && imageNames.length > 0) {
        const imageId = imageNames[0];
        const apiUrl = '/api/v1/images/' + imageId;

        return fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.blob();
            })
            .then(blob => URL.createObjectURL(blob))
            .catch(error => {
                console.error('Error fetching image:', error);
                return fetchDefaultInterestImage(interest);
            });
    } else {
        console.error('No imageNames found for the place');
        return fetchDefaultInterestImage(interest);
    }
}