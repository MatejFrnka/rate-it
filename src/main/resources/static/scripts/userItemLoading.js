function toggleExtension(toggleDiv) {
    const isExtended = toggleDiv.classList.toggle('extended');

    if (isExtended) {
        if (toggleDiv.classList.contains("user-interest")) {
            loadAndFormatItems(toggleDiv, 3);
        } else if (toggleDiv.classList.contains("user-detail-place")) {
            loadAndFormatItems(toggleDiv, null); // Indicates loading all items
        }
    }
}

function loadAndFormatItems(toggleDiv, maxItems) {
    const extensionContainer = toggleDiv.querySelector('.extension-list');

    if (extensionContainer) {
        const ulElements = extensionContainer.querySelectorAll('.user-interest-place');
        let itemsProcessed = 0;
        for (const ulElement of ulElements) {
            ulElement.style.display = 'flex';
            itemsProcessed++;
            if (maxItems !== null && itemsProcessed >= maxItems) {
                break;
            }
        }
    }
}

function handleDetailsClick(event) {
    event.stopPropagation();
}

const detailsButtons = document.querySelectorAll('.extension .button');
detailsButtons.forEach(function (button) {
    button.addEventListener('click', handleDetailsClick);
});

