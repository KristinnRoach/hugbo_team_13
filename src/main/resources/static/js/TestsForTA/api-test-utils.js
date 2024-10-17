// api-test-utils.js

import {httpRequest} from "../utility/fetch.js";

const baseUrl = 'http://localhost:8080/api';


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
        displayResponse(document.getElementById(`${endpoint}-response`), response, successMsg, failMsg);
    });
}

export function createTestUser() {
    return {
        username: 'test user ' + Math.floor(Math.random() * 1000),
        email: 'test.user@ex.com',
        password: 'qwerty123',
    };
}

export function createTestGame(name, platform) {
    return {id: null, name: `${name}`, platform: `${platform}`, rank: null};
}

/**
 * Creates a test event object compatible with the backend EventDTO.
 * @param {string} name - The name of the event.
 * @param {string} startDate - The start date of the event (YYYY-MM-DD).
 * @param {string} startTime - The start time of the event (HH:MM).
 * @param {string} endDate - The end date of the event (YYYY-MM-DD).
 * @param {string} endTime - The end time of the event (HH:MM).
 * @returns {Object} An event object with name, startTime, and endTime.
 */
export function createTestEvent(name, startDate, endDate, startTime,  endTime) {
    if(!(name && startDate && startTime && endDate && endTime)) {
        return false;
    }

    // Ensure date is in YYYY-MM-DD format
    const formatDate = (dateStr) => {
        const date = new Date(dateStr);
        return date.toISOString().split('T')[0];
    };

    // Ensure time is in HH:mm format
    const formatTime = (timeStr) => {
        const [hours, minutes] = timeStr.split(':');
        return `${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}`;
    };

    return {
        name: name || 'testEvent' + Math.floor(Math.random() * 1000),
        startDate: formatDate(startDate),
        endDate: formatDate(endDate),
        startTime: formatTime(startTime),
        endTime: formatTime(endTime)
    };
}