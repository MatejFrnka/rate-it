const pictureContainer = document.getElementById('picture-container');

const imageId = document.getElementById('picture-container').dataset.imageId;

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