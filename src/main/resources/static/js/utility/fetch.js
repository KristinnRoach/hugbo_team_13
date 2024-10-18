

export async function httpRequest(
    url,
    method,
    data = null,
    queryParams = null,
    config = {}
) {

  const request = {
    method: method.toUpperCase(),
    headers: {
      ...config.headers,
    },
    ...config
  };

  // Set Content-Type only for methods that typically have a body
  if (['POST', 'PUT', 'PATCH'].includes(request.method)) {
    request.headers['Content-Type'] = request.headers['Content-Type'] || 'application/json';
  }

  // Handle query parameters for all methods
  if (queryParams) {
    const queryString = new URLSearchParams(queryParams).toString();
    url += (url.includes('?') ? '&' : '?') + queryString;
  }

  // Handle method-specific behaviors
  switch (request.method) {
    case 'GET':
    case 'HEAD':
      // TODO: TEST below for get & head // For GET and HEAD, merge data into query parameters if both are provided
      if (data) {
        const additionalQueryString = new URLSearchParams(data).toString();
        url += (url.includes('?') ? '&' : '?') + additionalQueryString;
      }
      delete request.body; // Remove body if accidentally added
      break;
    case 'POST':
    case 'PUT':
    case 'PATCH':
      if (data && Object.keys(data).length > 0) {
        request.body = JSON.stringify(data);
      }
      break;
    // case 'DELETE': // nothing special?
    default:
      throw new Error(`Unsupported HTTP method: ${request.method}`);
  }

  try {
    const response = await fetch(url, request);
    const contentType = response.headers.get("content-type");

    let responseData;
    if (contentType && contentType.includes("application/json")) {
      // If it's JSON, parse it
      responseData = await response.json();
    } else {
      // If it's not JSON, get it as text
      responseData = await response.text();
    }

    return {
      ok: response.ok,
      status: response.status,
      data: responseData,
    };
  } catch (error) {
    console.error('Network error:', error);

    return {
      ok: false,
      status: 0,
      errorMsg: error.message,
      error: error,
    };
  }
}


// Convenience wrappers for common HTTP methods
export const getData = (url, queryParams, config) => httpRequest(url, 'GET', null, queryParams, config);
export const postData = (url, data, queryParams, config) => httpRequest(url, 'POST', data, queryParams, config);
export const putData = (url, data, queryParams, config) => httpRequest(url, 'PUT', data, queryParams, config);
export const patchData = (url, data, queryParams, config) => httpRequest(url, 'PATCH', data, queryParams, config);
export const deleteData = (url, data, queryParams, config) => httpRequest(url, 'DELETE', data, queryParams, config);