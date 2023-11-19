<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.item.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.item.ItemShort" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить продажу</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
    <script src="/scripts/theme.js" defer></script>
    <script>
        function showCheckboxes() {
            var reportType = document.getElementById("reportType").value;
            var checkboxGroup1 = document.getElementById("checkboxGroup1");
            var checkboxGroup2 = document.getElementById("checkboxGroup2");

            // Скрываем все группы чекбоксов
            checkboxGroup1.style.display = "none";
            checkboxGroup2.style.display = "none";

            // Сбрасываем состояние всех чекбоксов в обеих группах
            resetCheckboxes(checkboxGroup1);
            resetCheckboxes(checkboxGroup2);

            // Отображаем выбранную группу чекбоксов
            if (reportType === "Отчет по каждому товару") {
                checkboxGroup1.style.display = "block";
            } else if (reportType === "Отчет по категориям") {
                checkboxGroup2.style.display = "block";
            }
        }

        function resetCheckboxes(checkboxGroup) {
            var checkboxes = checkboxGroup.querySelectorAll("input[type='checkbox']");
            checkboxes.forEach(function (checkbox) {
                checkbox.checked = false;
            });
        }
    </script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Сгенерировать отчет</h4>
    <form action="/report?action=stock" method="post">
        <select id="reportType" name="itemId" onchange="showCheckboxes()" required>
            <option value="" selected>Выберите тип отчета</option>
            <option value="Отчет по каждому товару">Отчет по каждому товару</option>
            <option value="Отчет по категориям">Отчет по категориям</option>
        </select>

        <div id="checkboxGroup1" class="checkbox-group">
            <fieldset>
                <legend>Поля в отчете</legend>
                <input type="checkbox" id="option1" name="name" value="1"/>
                <label for="option1">Имя товара</label>

                <input type="checkbox" id="option2" name="description" value="1"/>
                <label for="option2">Описание товара</label>

                <input type="checkbox" id="option3" name="brand" value="1"/>
                <label for="option3">Производитель</label>

                <input type="checkbox" id="option4" name="stockUnits" value="1"/>
                <label for="option4">Остаток на складе</label>

                <input type="checkbox" id="option5" name="averagePurchasePrice" value="1"/>
                <label for="option5">Средняя цена закупки</label>

                <input type="checkbox" id="option6" name="categoryId" value="1"/>
                <label for="option6">Категория товара</label>

                <input type="checkbox" id="option7" name="supplyCount" value="1"/>
                <label for="option7">Количество поступлений</label>

                <input type="checkbox" id="option8" name="lastSupplyDate" value="1"/>
                <label for="option8">Дата последнего поступления</label>

                <input type="checkbox" id="option9" name="commonStock" value="1"/>
                <label for="option9">Общий остаток</label>
            </fieldset>
        </div>

        <div id="checkboxGroup2" class="checkbox-group">
            <fieldset>
                <legend>Поля в отчете</legend>
                <input type="checkbox" id="categoryOption1" name="name" value="1"/>
                <label for="categoryOption1">Имя категории</label>

                <input type="checkbox" id="categoryOption2" name="description" value="1"/>
                <label for="categoryOption2">Описание категории</label>

                <input type="checkbox" id="categoryOption3" name="amountPerCategory" value="1"/>
                <label for="categoryOption3">Количество товаров в категории</label>

                <input type="checkbox" id="categoryOption5" name="stockPerCategory" value="1"/>
                <label for="categoryOption5">Остаток на складе</label>

                <input type="checkbox" id="categoryOption6" name="allPrice" value="1"/>
                <label for="categoryOption6">Стоимость товаров</label>

                <input type="checkbox" id="categoryOption7" name="commonStock" value="1"/>
                <label for="categoryOption7">Общий остаток</label>
            </fieldset>
        </div>

        <br/>
        <input type="submit" value="Сгенерировать"/>
    </form>

    <% if (request.getAttribute("generatedId") != null) { %>
    <div class="ok-message" id="popupMessage">
        Новая продажа успешно добавлена. Присвоенный идентификатор id = <%=request.getAttribute("generatedId")%>.
    </div>
    <% } %>

</div>


</body>
</html>