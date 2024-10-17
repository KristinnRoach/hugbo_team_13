import { createInputField } from "../utility/endpoint-utils.js";
import { testEndpointButton, createTestUser, createTestEvent  } from './api-test-utils.js';
import { createElement } from "../utility/dom-utils.js";

document.addEventListener('DOMContentLoaded', () => {
  console.log('endpoint-tests.js loaded');

  const mainEl = document.getElementById('main');
  const parentContainer = createElement('div', {className: 'test-container', id: 'test-container'}, mainEl);

  /* USERS */

  // containers setup
  const usersDiv = createElement('div', {id: 'test-users-div', className: 'flex-column endpoint-tests'}, parentContainer);
  const userBtnsDiv = createElement('div', {className: 'test-buttons'}, usersDiv);
  const userInputForm = createElement('form', {className: 'test-inputs-form'}, usersDiv, [createElement('p', {id: 'users-response', className: 'test-response'}, usersDiv)]);

  // inputs setup
  const userIdInput = createInputField(userInputForm, 'test-user-id-input', 'User ID: GET/PATCH/DELETE');
  const usernameInput = createInputField(userInputForm, 'test-username-input', 'Username: POST/PATCH');
  const emailInput = createInputField(userInputForm, 'test-email-input', 'Email: POST/PATCH');
  const passwordInput = createInputField(userInputForm, 'test-password-input', 'Password: POST/PATCH');

  // endpoint buttons setup
  testEndpointButton(userBtnsDiv, 'Create User', 'users', 'POST', null, createTestUser, 'User created', 'Failed to create user');
  testEndpointButton(userBtnsDiv, 'Get All Users', 'users', 'GET');
  testEndpointButton(userBtnsDiv, 'Get User by ID', 'users', 'GET', () => userIdInput.value);
  testEndpointButton(userBtnsDiv, 'Update User', 'users', 'PATCH', () => userIdInput.value,
      () => ({
    username: usernameInput.value,
    email: emailInput.value,
    password: passwordInput.value
  }), 'User updated', 'Failed to update user');
  testEndpointButton(userBtnsDiv, 'Delete User', 'users', 'DELETE', () => userIdInput.value, null, 'User deleted', 'Failed to delete user');
  testEndpointButton(userBtnsDiv, 'Delete All Users', 'users', 'DELETE', ()=>'delete-all', null, 'Cleared all users', 'Failed to delete all users');


  /* EVENTS */

  // containers
  const eventsDiv = createElement('div', {id: 'test-events-div', className: 'flex-column endpoint-tests'}, parentContainer)
  const eventBtnsDiv = createElement('div', {className: 'test-buttons'}, eventsDiv);
  const eventInputForm = createElement('form', {className: 'test-inputs-form'}, eventsDiv, [createElement('p', {id: 'events-response', className: 'test-response'}, eventsDiv)]);

  // inputs
  const eventIdInput = createInputField(eventInputForm, 'test-event-id-input', 'Event ID: GET/PATCH/DELETE');
  const eventNameInput = createInputField(eventInputForm, 'test-event-name-input', 'Event name: POST/PATCH');
  const eventStartDateInput = createInputField(eventInputForm, 'test-event-start-date-input', 'start date: GET/POST/PATCH', 'date');
  const eventEndDateInput = createInputField(eventInputForm, 'test-event-end-date-input', 'end date: GET/POST/PATCH', 'date');
  const eventStartTimeInput = createInputField(eventInputForm, 'test-event-start-time-input', 'start time: GET/POST/PATCH', 'time');
  const eventEndTimeInput = createInputField(eventInputForm, 'test-event-end-time-input', 'end time: GET/POST/PATCH', 'time');

  // buttons
  testEndpointButton(eventBtnsDiv, 'Create Event', 'events', 'POST', null,
      () => createTestEvent(
          eventNameInput.value,
          eventStartDateInput.value,
          eventStartTimeInput.value,
          eventEndDateInput.value,
          eventEndTimeInput.value
      ),
      'Event created', 'Failed to create event');
  testEndpointButton(eventBtnsDiv, 'Get All Events', 'events', 'GET');
  testEndpointButton(eventBtnsDiv, 'Get Event by ID', 'events', 'GET', () => eventIdInput.value);
  // testEndpointButton(eventBtnsDiv, 'Get Event by Date', 'events', 'GET', () => eventStartInput.value);
  testEndpointButton(eventBtnsDiv, 'Delete Event', 'events', 'DELETE', () => eventIdInput.value, null, 'Event deleted', 'Failed to delete event');
  testEndpointButton(eventBtnsDiv, 'Delete All Events', 'events', 'DELETE', ()=>'delete-all', null, 'Cleared all events', 'Failed to delete all events');


  /* GAMES */

  // containers
  /*
  const gamesDiv = createElement('div', {id: 'test-games-div', className: 'flex-column endpoint-tests'}, parentContainer)
  const gameBtnsDiv = createElement('div', {className: 'test-buttons'}, gamesDiv);
  createElement('p', {id: 'games-response', className: 'test-response'}, gamesDiv);
  const gameInputForm = createElement('form', {className: 'test-inputs-form'}, gamesDiv)
  */


  // Add new test button example:
  // testEndpointButton(container, 'Label', 'endpoint', 'GET', ()=>getPathParam, ()=>getPayload());
});
