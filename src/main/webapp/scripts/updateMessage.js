document.addEventListener('DOMContentLoaded', function() {

    function showPopup() {
        let popup = document.getElementById("popupMessage");
        popup.style.display = "block";
        popup.style.opacity = "0";
        popup.style.width = "300px";

        setTimeout(function() {
            popup.style.transition = "opacity 0.5s ease-in-out";
            popup.style.opacity = "1";
        }, 100);

        setTimeout(function() {
            popup.style.opacity = "0";
            setTimeout(function() {
                popup.style.display = "none";
            }, 500);
        }, 5000);
    }

    showPopup();
});