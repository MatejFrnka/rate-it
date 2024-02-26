document.addEventListener('DOMContentLoaded', function () {
    const button = document.querySelector('.follow');
    updateFollowButton(button, isFollowed);
    button.addEventListener('click', () => followUser(button));
});

async function followUser(button) {
    const isFollowed = button.classList.contains('followed');
    try {
        const response = await fetch(`/api/v1/users/${userId}/follow`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ follow: !isFollowed })
        });

        const data = await response.json();

        const followersCount = document.querySelector('.user-follow p:first-child');
        const currentFollowers = parseInt(followersCount.textContent, 10);
        followersCount.textContent = ((currentFollowers + (isFollowed ? -1 : 1)) + ' Followers');


        updateFollowButton(button, data.follow);
    } catch (error) {
        console.error('Error:', error);
    }
}

function updateFollowButton(button, isFollowed) {
    button.classList.toggle('followed', isFollowed);
    button.textContent = isFollowed ? 'Unfollow' : 'Follow';
}