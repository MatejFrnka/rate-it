// https://medium.com/geekculture/how-to-build-a-simple-star-rating-system-abcbb5117365

document.addEventListener('DOMContentLoaded', function(){
    (function(){
        let sr = document.querySelectorAll('.place-rating-star');
        let i = 0;

        //loop through stars
        while (i < sr.length){
            //attach click event
            sr[i].addEventListener('mouseover', function(){
                let currentStars = this.parentElement;
                // add hover class to all preceding stars
                let starNumber = parseInt(this.getAttribute("data-star"));
                // get preceding stars
                for (let j = 1; j <= starNumber; j++){
                    // check if the classlist contains the active class, if not, add the class
                    if(!currentStars.querySelector('.star-'+j).classList.contains('hover')){
                        currentStars.querySelector('.star-'+j).classList.add('hover');
                    }
                }
            });

            sr[i].addEventListener('mouseout', function(){
                let currentStars = this.parentElement;
                // remove hover class from all preceding stars
                let starNumber = parseInt(this.getAttribute("data-star"));
                // get preceding stars
                for (let j = 1; j <= starNumber; j++){
                    //check if the classlist contains the active class, if yes, remove the class
                    if(currentStars.querySelector('.star-'+j).classList.contains('hover')){
                        currentStars.querySelector('.star-'+j).classList.remove('hover');
                    }
                }
            })

            sr[i].addEventListener('click', function(){
                let currentStars = this.parentElement;
                //current star
                let starNumber = parseInt(this.getAttribute("data-star"));
                //output current clicked star value
                let input = currentStars.parentElement.querySelector('input');
                input.value = starNumber;

                //loop through and set the active class on preceding stars
                for (let i = 1; i <= 10; i++){
                    if (i <= starNumber) {
                        //check if the classlist contains the active class, if not, add the class
                        if(!currentStars.querySelector('.star-'+i).classList.contains('is-active')){
                            currentStars.querySelector('.star-'+i).classList.add('is-active');
                        }
                    } else {
                        //check if the classlist contains the active class, if yes, remove the class
                        if(currentStars.querySelector('.star-'+i).classList.contains('is-active')){
                            currentStars.querySelector('.star-'+i).classList.remove('is-active');
                        }
                    }

                }
            })//end of click event
            i++;
        }//end of while loop
    })();//end of function
})