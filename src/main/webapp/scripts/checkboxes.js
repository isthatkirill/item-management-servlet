function showCheckboxes() {
    let reportType = document.getElementById("reportType").value;
    let checkboxGroup1 = document.getElementById("checkboxGroup1");
    let checkboxGroup2 = document.getElementById("checkboxGroup2");
    let checkboxGroup3 = document.getElementById("checkboxGroup3");
    let checkboxGroup4 = document.getElementById("checkboxGroup4");

    checkboxGroup1.style.display = "none";
    checkboxGroup2.style.display = "none";
    checkboxGroup3.style.display = "none";
    checkboxGroup4.style.display = "none";

    resetCheckboxes(checkboxGroup1);
    resetCheckboxes(checkboxGroup2);
    resetCheckboxes(checkboxGroup3);
    resetCheckboxes(checkboxGroup4);

    if (reportType === "itemStockReport") {
        checkboxGroup1.style.display = "block";
    } else if (reportType === "categoryStockReport") {
        checkboxGroup2.style.display = "block";
    } else if (reportType === "itemSaleReport") {
        checkboxGroup3.style.display = "block";
    } else if (reportType === "categorySaleReport") {
        checkboxGroup4.style.display = "block";
    }
}

function resetCheckboxes(checkboxGroup) {
    let checkboxes = checkboxGroup.querySelectorAll("input[type='checkbox']");
    checkboxes.forEach(function (checkbox) {
        checkbox.checked = false;
    });
}