document.addEventListener('DOMContentLoaded', function () {
    const interest = document.querySelectorAll('.default-image');
    interest.forEach(async function (imgDiv) {
        const interestId = imgDiv.dataset.interestId;

        const interestResponse = await fetch(`/api/v1/interests/${interestId}`);
        const interest = await interestResponse.json();

        imgDiv.src = fetchDefaultInterestImage(interest);
    });
});

function fetchDefaultInterestImage(interest) {
    const categories = interest.categoryIds;
    let imgUrl;
    if (categories.includes(1) || categories.includes(2)) {
        imgUrl = '../images/food-drink.svg';
    } else if (categories.includes(3) || categories.includes(5) || categories.includes(7)) {
        imgUrl = '../images/outdoor-sport-relax.svg';
    } else if (categories.includes(4) || categories.includes(6) || categories.includes(9)) {
        imgUrl = '../images/art-culture-entertainment-educ.svg';
    } else {
        imgUrl = '../images/services.svg';
    }
    return imgUrl;
}