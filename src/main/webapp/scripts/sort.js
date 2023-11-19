document.addEventListener('DOMContentLoaded', function () {
    let headers = document.querySelectorAll('th');

    headers.forEach(function (header) {
        header.addEventListener('click', function () {
            let currentUrl = new URL(window.location.href);

            let sortBy = header.dataset.sort;

            let currentSortBy = currentUrl.searchParams.get('sortBy');
            let currentSortOrder = currentUrl.searchParams.get('sortOrder');

            let newSortOrder = 'asc';
            if (sortBy === currentSortBy && currentSortOrder === 'asc') {
                newSortOrder = 'desc';
            }

            currentUrl.searchParams.set('sortBy', sortBy);
            currentUrl.searchParams.set('sortOrder', newSortOrder);

            window.location.href = currentUrl.toString();
        });
    });
});