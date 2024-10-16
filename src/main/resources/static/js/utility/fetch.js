
// Todo: make separate for each method

export async function httpRequest(
  url,
  method,
  data = null,
  customHeaders = {}
) {
  const options = {
    method: method,
    headers: {
      'Content-Type': 'application/json',
      ...customHeaders,
    },
  };


  if (data) {
    if (method === 'GET' || method === 'HEAD') {
      // For GET and HEAD requests, append data to URL as query parameters
      const params = new URLSearchParams(data);
      url += `?${params.toString()}`;
    } else {
      // For other methods, add data to the body
      options.body = JSON.stringify(data);
    }
  }

  try {
    const response = await fetch(url, options);
    const contentType = response.headers.get('content-type');
    const isJson = contentType?.includes('application/json');
    const responseData = isJson ? await response.json() : await response.text();

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
      error: error.message,
    };
  }
}
