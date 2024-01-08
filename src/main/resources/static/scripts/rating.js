// https://medium.com/geekculture/how-to-build-a-simple-star-rating-system-abcbb5117365

const STARS = 10;

function addClassToStars(stars, rating, className){
    //loop through and set the active class on preceding stars
    for (let i = 1; i <= STARS; i++){
        if (i <= rating) {
            //check if the classlist contains the active class, if not, add the class
            if(!stars.querySelector('.star-'+i).classList.contains(className)){
                stars.querySelector('.star-'+i).classList.add(className);
            }
        } else {
            //check if the classlist contains the active class, if yes, remove the class
            if(stars.querySelector('.star-'+i).classList.contains(className)){
                stars.querySelector('.star-'+i).classList.remove(className);
            }
        }
    }
}

function initializeRating(){
    let placeRatingStars = document.querySelectorAll('.place-rating-star');

    placeRatingStars.forEach(function(star){
        //attach click event
        star.addEventListener('mouseover', function(){
            let stars = this.parentElement;
            stars.classList.add('hover');

            // add hover class to all preceding stars
            let rating = parseInt(this.getAttribute("data-star"));
            addClassToStars(stars, rating, 'hover-active');
        });

        star.addEventListener('mouseout', function(){
            let stars = this.parentElement;
            stars.classList.remove('hover');
            for (let j = 1; j <= STARS; j++){
                stars.querySelector('.star-'+j).classList.remove('hover-active');
            }
        })

        star.addEventListener('click', function(){
            let stars = this.parentElement;
            let input = stars.parentElement.querySelector('input');
            let rating = parseInt(this.getAttribute("data-star"));

            input.value = rating;

            addClassToStars(stars, rating, 'is-active');
        })
    })
}

document.addEventListener('DOMContentLoaded', initializeRating);