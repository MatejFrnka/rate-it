function submitForm() {
    const form = document.getElementById('contactForm');
    const formData = new FormData(form);

    const jsonData = {};
    formData.forEach((value, inputName) => {
        jsonData[inputName] = value;
    });

    fetch('/api/v1/emails/contact-us', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (response.ok) {
                form.reset();
                const successful = document.querySelector('.successful');
                successful.style.marginTop = '1rem';
                successful.innerHTML = 'Email was sent successfully';
            } else {
                const error = document.querySelector('.error');
                error.style.marginTop = '1rem';
                error.innerHTML = 'Failed to send email. Please try again.';
            }
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}