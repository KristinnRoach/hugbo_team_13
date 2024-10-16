import {httpRequest} from "./fetch.js";

export const baseUrl = 'http://localhost:8080/api';

export function createEndpointButton(container, label, endpoint, method, getPayload = null) {
    const button = document.createElement('button');
    button.textContent = label;
    button.addEventListener('click', () => handleEndpointRequest(endpoint, method, getPayload));
    container.appendChild(button);
}

export function handleEndpointRequest(endpoint, method, getPayload = null) {
    const url = `${baseUrl}/${endpoint}`;
    const payload = getPayload ? getPayload() : null;

    httpRequest(url, method, payload).then((response) => {
        console.log('response:', response);
        console.log('data:', response.data);
    });
}

export function createInputField(container = testInputForm, id, placeholder) {
    const input = document.createElement('input');
    input.id = id;
    input.placeholder = placeholder;
    container.appendChild(input);
    return input;
}