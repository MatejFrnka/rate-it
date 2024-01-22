document.addEventListener('DOMContentLoaded', () =>{
    const splide = new Splide('.splide', {
        fixedWidth: '10rem',
        fixedHeight: '12rem',
        gap: '1rem',
        type: 'loop',
        drag: 'free',
    });
    splide.mount();
});