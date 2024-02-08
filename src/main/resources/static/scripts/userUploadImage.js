function fetchAndDisplayPicture(name) {
    fetch(`/api/v1/images/users/${name}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch picture');
            }
        })
        .then(data => {
            if (data.picture) {
                const imgElement = document.querySelector('.dropdown-toggle');
                imgElement.src = data.picture;
            } else {
                const imgElement = document.querySelector('.dropdown-toggle');
                imgElement.src = '/icons/user.svg';
            }
        })
        .catch(error => {
            console.error('Error fetching picture:', error);
            const imgElement = document.querySelector('.dropdown-toggle');
            imgElement.src = '/icons/user.svg';
        });
}