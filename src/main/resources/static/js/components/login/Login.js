// Login.js
import { qs, qsa, createElement, addEventListenerWithErrorHandling } from '../../utility/dom-utils.js';

export class LoginComponent {
    constructor(containerId) {
        this.container = qs(containerId);
        this.render();
        this.attachEventListeners();
    }

    render() {
        const loginForm = createElement('form', { id: 'loginForm', className: 'login-form' }, [
            createElement('label', { for: 'username' }, ['Username']),
            createElement('input', { type: 'text', id: 'username', name: 'username', required: true }),
            createElement('label', { for: 'password' }, ['Password']),
            createElement('input', { type: 'password', id: 'password', name: 'password', required: true }),
            createElement('button', { type: 'submit' }, ['Login']),
            createElement('p', { className: 'error-message', style: 'display: none;' })
        ]);

        const signupForm = createElement('form', { id: 'signupForm', className: 'signup-form', style: 'display: none;' }, [
            createElement('h2', {}, ['Complete Signup']),
            createElement('label', { for: 'signupUsername' }, ['Username']),
            createElement('input', { type: 'text', id: 'signupUsername', name: 'signupUsername', required: true }),
            createElement('label', { for: 'email' }, ['Email']),
            createElement('input', { type: 'email', id: 'email', name: 'email', required: true }),
            createElement('label', { for: 'signupPassword' }, ['Password']),
            createElement('input', { type: 'password', id: 'signupPassword', name: 'signupPassword', required: true }),
            createElement('button', { type: 'submit' }, ['Complete Signup']),
            createElement('p', { className: 'error-message', style: 'display: none;' })
        ]);

        this.container.appendChild(loginForm);
        this.container.appendChild(signupForm);
    }

    attachEventListeners() {
        const loginForm = qs('#loginForm');
        const signupForm = qs('#signupForm');

        addEventListenerWithErrorHandling(loginForm, 'submit', (e) => this.handleLoginSubmit(e));
        addEventListenerWithErrorHandling(signupForm, 'submit', (e) => this.handleSignupSubmit(e));
    }

    handleLoginSubmit(e) {
        e.preventDefault();
        const username = qs('#username').value.trim();
        const password = qs('#password').value.trim();

        if (this.validateInput(username, password)) {
            // For now, always show signup form
            qs('#loginForm').style.display = 'none';
            qs('#signupForm').style.display = 'block';
            qs('#signupUsername').value = username;
            qs('#signupPassword').value = password;
        } else {
            this.showError(loginForm, 'Please enter both username and password.');
        }
    }

    handleSignupSubmit(e) {
        e.preventDefault();
        const username = qs('#signupUsername').value.trim();
        const email = qs('#email').value.trim();
        const password = qs('#signupPassword').value.trim();

        if (this.validateInput(username, email, password)) {
            // Here you would typically send the data to your server
            console.log('Signup data:', { username, email, password });
            alert('Signup successful!');
            this.resetForms();
        } else {
            this.showError(signupForm, 'Please fill in all fields.');
        }
    }

    validateInput(...fields) {
        return fields.every(field => field !== '');
    }

    showError(form, message) {
        const errorMessage = qs('.error-message', form);
        errorMessage.textContent = message;
        errorMessage.style.display = 'block';
    }

    resetForms() {
        qsa('form', this.container).forEach(form => {
            form.reset();
            form.style.display = 'none';
        });
        qs('#loginForm').style.display = 'block';
    }
}