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
    options.body = JSON.stringify(data);
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
