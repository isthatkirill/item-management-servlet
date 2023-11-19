function toggleTheme() {
    let themeToggle = document.getElementById('themeToggle');
    let body = document.body;

    if (themeToggle.checked) {
        body.classList.remove('light-theme');
        body.classList.add('dark-theme');
        localStorage.setItem('theme', 'dark');
    } else {
        body.classList.remove('dark-theme');
        body.classList.add('light-theme');
        localStorage.setItem('theme', 'light');
    }
}

function applyThemePreference() {
    let themePreference = localStorage.getItem('theme');
    let themeToggle = document.getElementById('themeToggle');

    if (themePreference === 'dark') {
        document.body.classList.add('dark-theme');
        themeToggle.checked = true;
    } else {
        document.body.classList.add('light-theme');
        themeToggle.checked = false;
    }
}

window.onload = applyThemePreference;