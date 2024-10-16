import { createInputField } from "../utility/endpoint-utils.js";
import { setupResponseDisplay, testNewUser, setupTestInputForm, testEndpointButton  } from './api-test-utils.js';

document.addEventListener('DOMContentLoaded', () => {
  console.log('endpoint-tests.js loaded');

  const testContainer = document.getElementById('test-container');
  const buttonContainer = document.createElement('div');
  buttonContainer.classList.add('test-buttons');
  testContainer.appendChild(buttonContainer);
  setupResponseDisplay(testContainer);
  const testInputForm = setupTestInputForm(testContainer);

  // Create input fields
  const userIdInput = createInputField(testInputForm, 'test-user-id-input', 'User ID');
  const usernameInput = createInputField(testInputForm, 'test-username-input', 'Username');
  const emailInput = createInputField(testInputForm, 'test-email-input', 'Email');
  const passwordInput = createInputField(testInputForm, 'test-password-input', 'Password');

  // Create buttons for each endpoint
  testEndpointButton(buttonContainer, 'Create User', 'users', 'POST', null, testNewUser, 'User created', 'Failed to create user');
  testEndpointButton(buttonContainer, 'Get All Users', 'users', 'GET');
  testEndpointButton(buttonContainer, 'Get User by ID', 'users', 'GET', () => userIdInput.value);
  testEndpointButton(buttonContainer, 'Update User', 'users', 'PATCH', () => userIdInput.value, () => ({
    username: usernameInput.value,
    email: emailInput.value,
    password: passwordInput.value
  }), 'User updated', 'Failed to update user');
  testEndpointButton(buttonContainer, 'Delete User', 'users', 'DELETE', () => userIdInput.value, null, 'User deleted', 'Failed to delete user');
  testEndpointButton(buttonContainer, 'Delete All Users', 'users/delete-all', 'DELETE', null, null, 'Cleared all users', 'Failed to delete all users');

  // Add new test button example:
  // testEndpointButton(buttonContainer, 'Label', 'endpoint', 'GET', ()=>getPathParam, ()=>getPayload);
});
