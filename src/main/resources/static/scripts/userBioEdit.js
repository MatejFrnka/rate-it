document.addEventListener('DOMContentLoaded', function () {
    const userBio = document.getElementById('user-bio');
    const maxBioLength = MAX_BIO_LENGTH;
    const defaultText = 'Tap here to write something about yourself...';

    if (userBio.textContent.trim() === '') {
        userBio.textContent = defaultText;
    }

    let originalBio = userBio.textContent;

    userBio.addEventListener('focus', function () {
        if (userBio.textContent === defaultText) {
            userBio.textContent = '';
        }
    });

    userBio.addEventListener('input', function () {
        if (userBio.textContent.length > maxBioLength) {
            userBio.textContent = originalBio;
        } else {
            originalBio = userBio.textContent;
        }
    });

    userBio.addEventListener('blur', function () {
        if (userBio.textContent.trim() === '') {
            userBio.textContent = defaultText;
        } else {
            saveBio();
        }
    });
});

function saveBio() {
    const userBio = document.getElementById('user-bio');
    const newBio = userBio.textContent;

    fetch(`/api/v1/users/${userId}/editBio`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            id: userId,
            bio: newBio
        })
    })
        .then(response => {
            if (response.ok) {
                console.log('Bio updated successfully!');
            } else {
                console.error('Failed to update bio:', response.status);
            }
        })
        .catch(error => {
            console.error('Error updating bio:', error)
        });
}