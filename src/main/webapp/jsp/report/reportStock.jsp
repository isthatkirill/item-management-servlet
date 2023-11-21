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
    <script src="/scripts/checkboxes.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Сгенерировать отчет</h4>
    <form action="/report" method="post">
        <select style="width: 100%" id="reportType" name="reportType" onchange="showCheckboxes()" required>
            <option value="" selected>Выберите тип отчета</option>
            <option value="itemStockReport">Отчет по каждому товару</option>
            <option value="categoryStockReport">Отчет по категориям</option>
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

                <input type="checkbox" id="option4" name="stock_units" value="1"/>
                <label for="option4">Остаток на складе</label>

                <input type="checkbox" id="option5" name="purchase_price" value="1"/>
                <label for="option5">Средняя стоимость закупки</label>

                <input type="checkbox" id="option6" name="stock_price" value="1"/>
                <label for="option6">Стоимость товаров на складе</label>

                <input type="checkbox" id="option7" name="category_name" value="1"/>
                <label for="option7">Категория товара</label>

                <input type="checkbox" id="option8" name="supplies_count" value="1"/>
                <label for="option8">Количество поступлений</label>

                <input type="checkbox" id="option9" name="last_supply_date" value="1"/>
                <label for="option9">Дата последнего поступления</label>
            </fieldset>
        </div>

        <div id="checkboxGroup2" class="checkbox-group">
            <fieldset>
                <legend>Поля в отчете</legend>
                <input type="checkbox" id="categoryOption1" name="name" value="1"/>
                <label for="categoryOption1">Имя категории</label>

                <input type="checkbox" id="categoryOption2" name="description" value="1"/>
                <label for="categoryOption2">Описание категории</label>

                <input type="checkbox" id="categoryOption3" name="items_in_category" value="1"/>
                <label for="categoryOption3">Количество товаров в категории</label>

                <input type="checkbox" id="categoryOption5" name="stock_units" value="1"/>
                <label for="categoryOption5">Остаток на складе</label>

                <input type="checkbox" id="categoryOption6" name="stock_price" value="1"/>
                <label for="categoryOption6">Стоимость товаров на складе</label>

                <input type="checkbox" id="categoryOption4" name="supplies_count" value="1"/>
                <label for="categoryOption4">Количество поступлений</label>

                <input type="checkbox" id="categoryOption11" name="last_supply_date" value="1"/>
                <label for="categoryOption11">Дата последнего поустпления</label>

                <input type="checkbox" id="categoryOption7" name="most_units_item" value="1"/>
                <label for="categoryOption7">Товар с наибольшим остатком</label>

                <input type="checkbox" id="categoryOption9" name="less_units_item" value="1"/>
                <label for="categoryOption9">Товар с наименьшим остатком</label>

                <input type="checkbox" id="categoryOption10" name="most_cheap_item" value="1"/>
                <label for="categoryOption10">Самый дешевый товар в категории</label>

                <input type="checkbox" id="categoryOption8" name="most_expensive_item" value="1"/>
                <label for="categoryOption8">Самый дорогой товар в категории</label>
            </fieldset>
        </div>

        <br/>
        <input type="submit" value="Сгенерировать"/>
    </form>

    <% if (request.getAttribute("path") != null) { %>
    <br/>
    <a id="download-a" href="<%=request.getAttribute("path")%>">Клик</a>
    <% } %>


</div>


</body>
</html>