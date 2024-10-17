
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

  // Only add Content-Type if we're sending data // todo: TEMPFIX
  if (data && method !== 'GET' && method !== 'HEAD') {
    options.headers['Content-Type'] = 'application/json';
  }

  try {
    console.log('ReQuest type:', {
      url,
      method,
      headers: options.headers,
      body: options.body
    });

    const response = await fetch(url, options);
    // Get the content type of the response
    const contentType = response.headers.get("content-type");

    let responseData;
    if (contentType && contentType.includes("application/json")) {

      // If it's JSON, parse it
      responseData = await response.json();
    } else {
      // If it's not JSON, get it as text
      responseData = await response.text();
    }

    console.log('RESPONSE type', contentType);

    return {
      ok: response.ok,
      status: response.status,
      data: responseData,
      // contentType: contentType
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


// const contentType = {'Content-Type': 'application/json'};
// const contentType = response.headers.get('content-type');
// const isJson = contentType?.includes('application/json');
// const responseData = isJson ? await response.json() : await response.text();
