// Signup.js
import { qs, createElement, addEventListenerWithErrorHandling } from '../../utility/dom-utils.js';

export class SignupComponent {
    constructor(containerId) {
        this.container = qs(containerId);
        this.render();
        this.attachEventListeners();
    }

    render() {
        const signupForm = createElement('form', { id: 'signupForm', className: 'signup-form', style: 'display: none;' }, [
            createElement('input', { type: 'text', id: 'username', name: 'username', placeholder: 'Username', required: true }),
            createElement('input', { type: 'email', id: 'email', name: 'email', placeholder: 'Email', required: true }),
            createElement('input', { type: 'password', id: 'password', name: 'password', placeholder: 'Password', required: true }),
            createElement('button', { type: 'submit' }, ['Sign Up']),
            createElement('p', { className: 'error-message', style: 'display: none;' })
        ]);

        const signupButton = createElement('button', { id: 'showSignup', type: 'button' }, ['Sign Up']);

        this.container.appendChild(signupButton);
        this.container.appendChild(signupForm);
    }

    attachEventListeners() {
        const signupButton = qs('#showSignup');
        const signupForm = qs('#signupForm');
        const errorMessage = qs('.error-message', signupForm);

        addEventListenerWithErrorHandling(signupButton, 'click', () => {
            signupButton.style.display = 'none';
            signupForm.style.display = 'block';
        });

        addEventListenerWithErrorHandling(signupForm, 'submit', async (e) => {
            e.preventDefault();
            errorMessage.style.display = 'none';

            const username = qs('#username').value.trim();
            const email = qs('#email').value.trim();
            const password = qs('#password').value.trim();

            if (!this.validateInput(username, email, password)) {
                errorMessage.textContent = 'Please fill in all fields.';
                errorMessage.style.display = 'block';
                return;
            }

            try {
                // Here you would typically send the data to your server
                console.log('Signup data:', { username, email, password });
                // Simulating a successful signup
                alert('Signup successful!');
                signupForm.reset();
                signupForm.style.display = 'none';
                signupButton.style.display = 'block';
            } catch (error) {
                errorMessage.textContent = 'An error occurred. Please try again later.';
                errorMessage.style.display = 'block';
            }
        });
    }

    validateInput(username, email, password) {
        return username !== '' && email !== '' && password !== '';
    }
}