document.addEventListener('DOMContentLoaded', function() {
    updateCharCountAndResize(document.querySelector("#description").getAttribute("maxlength"));
})

function updateCharCountAndResize(maxLength) {
    const textarea = document.querySelector("#description");
    const charCount = document.querySelector("#charCount");
    const remainingChars = maxLength - textarea.value.length;
    charCount.textContent = "Characters remaining: " + remainingChars;
    // textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
}



