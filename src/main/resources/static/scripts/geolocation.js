const findMyLocation = () => {
    const lat = document.getElementById("lat");
    const lng = document.getElementById("lng");
    const status = document.querySelector('.status')

    const success = (position) => {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;

        lat.value = latitude;
        lng.value = longitude;
    }

    const error = () => {
        status.textContent = 'Unable to get your location.'
    }

    navigator.geolocation.getCurrentPosition(success, error);
}

document.querySelector('.myLocation').addEventListener('click', findMyLocation);