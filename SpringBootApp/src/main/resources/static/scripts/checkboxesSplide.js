function initializeCheckboxesSplide() {
    const checkboxesSplide = new Splide('#splide-checkboxes', {
        autoWidth: true,
        autoHeight: true,
        gap: '0.5rem',
        type: 'slide',
        drag: 'free',
        arrows: false,
        loop: false,
        pagination: false
    });
    checkboxesSplide.mount();
}