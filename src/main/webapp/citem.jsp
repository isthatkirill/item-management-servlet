<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить товар</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Добавить товар</h4>
    <form id="itemForm" action="/item/create-item" method="post">
        <input type="text" name="name" placeholder="Наименование товара" required/>
        <br/>
        <input type="text" name="description" placeholder="Описание товара" required/>
        <br/>
        <input type="number" step="0.01" min="0" name="purchasePrice" placeholder="Цена закупки" required/>
        <br/>
        <input type="number" min="0" name="stockUnits" placeholder="Начальный остаток на складе (по умолч. 0)"/>
        <br/>
        <input type="number" name="categoryId" min="0" placeholder="Номер категории"/>
        <br/>
        <input type="text" name="brand" placeholder="Производитель/бренд"/>
        <br/>
        <input type="submit" value="Добавить"/>
    </form>
</div>

<script>
    const sampleForm = document.getElementById("itemForm");

    sampleForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        let form = e.currentTarget;
        let url = form.action;
        let formData = new FormData(form);
        let responseData = await postFormFieldsAsJson({ url, formData });
    });

    async function postFormFieldsAsJson({ url, formData }) {
        let formDataObject = Object.fromEntries(formData.entries());
        let formDataJsonString = JSON.stringify(formDataObject);

        let fetchOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
            body: formDataJsonString,
        };

        await fetch(url, fetchOptions);
    }
</script>

</body>
</html>
