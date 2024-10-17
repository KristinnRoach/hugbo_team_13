// api-test-utils.js

import {httpRequest} from "../utility/fetch.js";
import {getLocalStorage, setLocalStorage} from "../utility/localStorage-utils";

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

/**
 * Creates a test event object compatible with the backend EventDTO.
 * @param {string} name - The name of the event.
 * @param {string} startDate - The start date of the event (YYYY-MM-DD).
 * @param {string} startTime - The start time of the event (HH:MM).
 * @param {string} endDate - The end date of the event (YYYY-MM-DD).
 * @param {string} endTime - The end time of the event (HH:MM).
 * @returns {Object} An event object with name, startTime, and endTime.
 */
export function createTestEvent(name, startDate, startTime, endDate, endTime) {
    if(!(name && startDate && startTime && endDate && endTime)) {
        return false;
    }

    const startDateTime = combineDateAndTime(startDate, startTime);
    const endDateTime = combineDateAndTime(endDate, endTime);

    return {
        name: name || 'testEvent' + Math.floor(Math.random() * 1000),
        startTime: formatDate(startDateTime),
        endTime: formatDate(endDateTime),
    };
}

/**
 * Combines a date string and a time string into a Date object.
 * @param {string} dateStr - Date string in YYYY-MM-DD format.
 * @param {string} timeStr - Time string in HH:MM format.
 * @returns {Date} Combined Date object.
 */
function combineDateAndTime(dateStr, timeStr) {
    const [year, month, day] = dateStr.split('-').map(Number);
    const [hours, minutes] = timeStr.split(':').map(Number);
    return new Date(year, month - 1, day, hours, minutes);
}

/**
 * Formats a Date object to an ISO 8601 string
 * @param {Date} date - The date to format.
 * @returns {string} Formatted date string.
 */
function formatDate(date) {
    return date.toISOString();
}