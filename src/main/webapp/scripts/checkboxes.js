function showCheckboxes() {
    let reportType = document.getElementById("reportType").value;
    let checkboxGroup1 = document.getElementById("checkboxGroup1");
    let checkboxGroup2 = document.getElementById("checkboxGroup2");

    checkboxGroup1.style.display = "none";
    checkboxGroup2.style.display = "none";

    resetCheckboxes(checkboxGroup1);
    resetCheckboxes(checkboxGroup2);

    if (reportType === "itemStockReport") {
        checkboxGroup1.style.display = "block";
    } else if (reportType === "categoryStockReport") {
        checkboxGroup2.style.display = "block";
    }
}

function resetCheckboxes(checkboxGroup) {
    let checkboxes = checkboxGroup.querySelectorAll("input[type='checkbox']");
    checkboxes.forEach(function (checkbox) {
        checkbox.checked = false;
    });
}