// api-test-utils.js

import {httpRequest} from "../utility/fetch.js";

const baseUrl = 'http://localhost:8080/api';

export function setupResponseDisplay(container) {
    const responseContainer = document.createElement('p');
    responseContainer.id = 'test-response';
    responseContainer.classList.add('test-response');
    container.appendChild(responseContainer);
    return responseContainer;
}

export function setupTestInputForm(container) {
    const testInputForm = document.createElement('form');
    testInputForm.classList.add('test-inputs-form');
    container.appendChild(testInputForm);
    return testInputForm;
}

export function displayResponse(responseContainer, response, successMsg = '', failMsg = '') {
    const responseString = JSON.stringify(response.data);
    responseContainer.textContent = response.ok
        ? responseString + ' ' + successMsg
        : response.data + ' ' + failMsg;
}

export function testEndpointButton(container, label, endpoint, method, getPathParam = null, getPayload = null, successMsg = '', failMsg = '') {
    const button = document.createElement('button');
    button.textContent = label;
    button.addEventListener('click', () => handleTestRequest(endpoint, method, getPathParam, getPayload, successMsg, failMsg));
    container.appendChild(button);
}

export function handleTestRequest(endpoint, method, getPathParam = null, getPayload = null, successMsg = '', failMsg = '') {
    let url = `${baseUrl}/${endpoint}`;
    const payload = getPayload ? getPayload() : null;

    // Append path parameter if provided
    if (getPathParam) {
        const pathParam = getPathParam();
        url += `/${pathParam}`;
    }

    httpRequest(url, method, payload).then((response) => {
        console.log('response:', response);
        console.log('data:', response.data);
        displayResponse(document.getElementById('test-response'), response, successMsg, failMsg);
    });
}

export function testNewUser() {
    return {
        username: 'test user ' + Math.floor(Math.random() * 1000),
        email: 'test.user@ex.com',
        password: 'qwerty123',
    };
}