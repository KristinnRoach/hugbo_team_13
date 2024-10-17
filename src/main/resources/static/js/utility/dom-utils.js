// dom-utils.js

/**
 * Create an element with optional attributes and child elements
 * @param {string} tag - Tag name
 * @param {Object} [attributes={}] - Attributes to set on the element
 * @param parent
 * @param {Array} [children=[]] - Child elements or text to append
 * @returns {Element}
 */
export function createElement(tag, attributes = {}, parent = null, children = []) {
    const element = document.createElement(tag);

    Object.entries(attributes).forEach(([key, value]) => {
        if (key === 'className') {
            element.className = value;
        } else if (key === 'dataset') {
            Object.entries(value).forEach(([dataKey, dataValue]) => {
                element.dataset[dataKey] = dataValue;
            });
        } else {
            element.setAttribute(key, value);
        }
    });

    children.forEach(child => {
        if (typeof child === 'string') {
            element.appendChild(document.createTextNode(child));
        } else {
            element.appendChild(child);
        }
    });

    if (parent !== null && parent instanceof HTMLElement) {
        parent.appendChild(element);
    }

    return element;
}

/**
 * Toggle class on an element
 * @param {Element} element - Element to toggle class on
 * @param {string} className - Class to toggle
 */
export function toggleClass(element, className) {
    element.classList.toggle(className);
}


/**
 * Select a single element by CSS selector
 * @param {string} selector - CSS selector
 * @param {Element} [context=document] - Context to search within
 * @returns {Element|null}
 */
export function qs(selector, context = document) {
    return context.querySelector(selector);
}

/**
 * Select multiple elements by CSS selector
 * @param {string} selector - CSS selector
 * @param {Element} [context=document] - Context to search within
 * @returns {NodeList}
 */
export function qsa(selector, context = document) {
    return context.querySelectorAll(selector);
}

/**
 * Add event listener with error handling
 * @param {Element} element - Element to attach the listener to
 * @param {string} event - Event name
 * @param {Function} handler - Event handler function
 */
export function addEventListenerWithErrorHandling(element, event, handler) {
    element.addEventListener(event, async (e) => {
        try {
            await handler(e);
        } catch (error) {
            console.error(`Error in ${event} handler:`, error);
        }
    });
}
