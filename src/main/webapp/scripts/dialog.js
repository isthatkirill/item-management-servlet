document.addEventListener('DOMContentLoaded', function () {
    let deleteLinks = document.querySelectorAll('.delete-link');

    deleteLinks.forEach(function (link) {
        link.addEventListener('click', function (event) {
            event.preventDefault();

            let itemId = link.getAttribute('data-item-id');
            let categoryId = link.getAttribute('data-category-id');

            if (itemId && !document.querySelector('.overlay.show')) {
                showConfirmationDialog(itemId, 'item');
            } else if (categoryId && !document.querySelector('.overlay.show')) {
                showConfirmationDialog(categoryId, 'category');
            }
        });
    });

    function showConfirmationDialog(id, entity) {
        let overlay = document.createElement('div');
        overlay.className = 'overlay';

        let dialog = document.createElement('div');
        dialog.className = 'custom-dialog';

        let message = document.createElement('p');
        message.textContent = 'Вы уверены, что хотите удалить этот элемент?';

        let confirmButton = document.createElement('button');
        confirmButton.textContent = 'Подтвердить';
        confirmButton.className = 'dialog-button';
        confirmButton.addEventListener('click', function () {
            window.location.href = '/' + entity + '?action=button-delete-' + id;
        });

        let cancelButton = document.createElement('button');
        cancelButton.textContent = 'Отмена';
        cancelButton.className = 'dialog-button';

        cancelButton.addEventListener('click', function () {
            overlay.classList.remove('show');
            dialog.classList.remove('show');

            setTimeout(function () {
                overlay.remove();
                dialog.remove();
            }, 300);
        });

        dialog.appendChild(message);
        dialog.appendChild(confirmButton);
        dialog.appendChild(cancelButton);

        document.body.appendChild(overlay);
        document.body.appendChild(dialog);

        setTimeout(function () {
            overlay.classList.add('show');
            dialog.classList.add('show');
        }, 10);
    }
});