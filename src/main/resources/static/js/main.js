
document.addEventListener('DOMContentLoaded', () => {
  console.log('main.js loaded');
});

/*
const routes = {
  '': 'home',
  'events': 'events',
  'games': 'games',
  'ranking': 'ranking',
  'userPage': 'userPage'
};

function router() {
  let path = window.location.hash.slice(1) || '';
  loadContent(routes[path]);
}

function loadContent(page) {
  const main = document.getElementById('main');
  fetch(`/views/${page}.html`)
      .then(response => response.text())
      .then(html => {
        main.innerHTML = html;
        loadData(page);
      })
      .catch(error => {
        console.error('Error loading content:', error);
        main.innerHTML = '<p>Error loading content</p>';
      });
}

function loadData(page) {
  switch(page) {
    case 'events':
      fetchEvents();
      break;
    case 'games':
      fetchGames();
      break;
    case 'ranking':
      fetchRanking();
      break;
    case 'userPage':
      fetchUserData();
      break;
  }
}

window.addEventListener('hashchange', router);
window.addEventListener('load', router);

// Example data fetching function
function fetchEvents() {
  fetch('http://localhost:8080/api/events')
      .then(response => response.json())
      .then(data => {
        // Process and display events data
        console.log(data);
      })
      .catch(error => console.error('Error fetching events:', error));
}
*/

/* const loginBtn = document.getElementById("login-button");
 loginBtn.addEventListener("click", () => {
     // login or sign up if doesnt exist
 }) */