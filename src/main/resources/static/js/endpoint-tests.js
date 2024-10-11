import { httpRequest } from './utility/fetch.js';

document.addEventListener('DOMContentLoaded', () => {
  console.log('endpoint-tests.js loaded');
});

const usersUrl = 'http://localhost:8080/api/users';
const deleteAllUrl = 'http://localhost:8080/api/users/delete-all';

let count = 0;

const testNewUser = (count) => {
  return {
    username: 'test nr: ' + count,
    email: 'test.user@ex.com',
    password: 'qwerty123',
  };
};

/* DISPLAY */

const testContainer = document.getElementById('test-container');

const responseContainer = document.createElement('p');
responseContainer.id = 'test-response';
responseContainer.classList.add('test-response');
testContainer.appendChild(responseContainer);

function displayResponse(response) {
  response.ok
    ? (responseContainer.innerHTML = JSON.stringify(response.data))
    : (responseContainer.innerHTML = response.data);
}

/* HANDLERS */

document.addEventListener('keydown', (event) => {
  //console.log('Key pressed:', event.key);

  if (event.key === 'g') {
    httpRequest(usersUrl, 'GET').then((response) => {
      console.log('response:', response);
      console.log('data:', response.data);
      displayResponse(response);
    });
  }

  if (event.key === 'p') {
    count++;
    const userData = testNewUser(count);
    httpRequest(usersUrl, 'POST', userData).then((response) => {
      console.log('response:', response);
      console.log('data:', response.data);
      console.log('userData:', userData);
      displayResponse(response);
    });
  }

  if (event.key === 'd') {
    httpRequest(deleteAllUrl, 'DELETE').then((response) => {
      console.log('response:', response);
      console.log('data:', response.data);
      displayResponse(response);
    });
  }
});

const createUserButton = document.getElementById('test-create-user');
createUserButton.addEventListener('click', () => {
  count++;
  const userData = testNewUser(count);
  httpRequest(usersUrl, 'POST', userData).then((response) => {
    console.log('response:', response);
    console.log('data:', response.data);
    console.log('userData:', userData);
    displayResponse(response);
  });
});

const getUsersButton = document.getElementById('test-get-users');
getUsersButton.addEventListener('click', () => {
  httpRequest(usersUrl, 'GET').then((response) => {
    console.log('response:', response);
    console.log('data:', response.data);
    displayResponse(response);
  });
});

const deleteUserButton = document.getElementById('test-delete-all-users');
deleteUserButton.addEventListener('click', () => {
  httpRequest(deleteAllUrl, 'DELETE').then((response) => {
    console.log('response:', response);
    console.log('data:', response.data);
    displayResponse(response);
  });
});
