document.addEventListener( 'DOMContentLoaded', function() {
    const splide = new Splide('#splide-interests', {
        fixedWidth: '10rem',
        fixedHeight: '11rem',
        gap: '1rem',
        type: 'slide',
        drag: 'free',
        arrows: false,
        loop: false
    });
    splide.mount();
});