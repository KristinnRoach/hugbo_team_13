/**
 * Sets an item in localStorage after stringifying it.
 * @param {string} key - The key to set in localStorage.
 * @param {any} value - The value to stringify and store.
 */
export function setLocalStorage(key, value) {
    try {
        localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
        console.error('Error setting localStorage item:', error);
    }
}

/**
 * Gets an item from localStorage and parses it from JSON.
 * @param {string} key - The key to retrieve from localStorage.
 * @returns {any|null} The parsed value from localStorage, or null if not found or on error.
 */
export function getLocalStorage(key) {
    try {
        const data = localStorage.getItem(key);
        return JSON.parse(data);
    } catch (error) {
        console.error('Error getting localStorage item:', error);
        return null;
    }
}
