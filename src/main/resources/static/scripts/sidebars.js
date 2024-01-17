function updateStyles() {
    const container = document.querySelector('.container-fluid');
    const windowWidth = window.innerWidth;

    if (windowWidth > 499) {
        const startWidth = 500;
        const endWidth = window.screen.width;
        let sidebarPercentage = (windowWidth - startWidth) / (endWidth - startWidth);
        sidebarPercentage = Math.max(0, Math.min(sidebarPercentage, 1));
        const actualSidebarWidth = sidebarPercentage * 35;
        container.style.setProperty('--sidebar-width', `${actualSidebarWidth}vw`);
        const paddingValue = Math.max(actualSidebarWidth, 0);
    }

}

updateStyles();

window.addEventListener('resize', updateStyles);