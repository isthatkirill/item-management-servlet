<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.category.Category" %>
<%@ page import="isthatkirill.itemmanagement.model.item.ItemExtended" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Редактировать товар</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
    <script src="/scripts/dialog.js" defer></script>
    <script src="/scripts/theme.js" defer></script>
    <script src="/scripts/updateMessage.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Редактировать товар</h4>
    <form action="/item?action=update" method="post">
        <select name="id" required>
            <option value="" selected>Выберите товар</option>
            <% if (request.getAttribute("items") != null) { %>
            <% List<ItemExtended> items = (List<ItemExtended>) request.getAttribute("items"); %>
            <% for (ItemExtended item : items) { %>
            <option value="<%=item.getId()%>"><%=item.getName()%> (id = <%=item.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <input type="text" name="name" placeholder="Новое наименование товара"/>
        <br/>
        <input type="text" name="description" placeholder="Новое описание товара"/>
        <br/>
        <select name="categoryId">
            <option value="" selected>Выберите новую категорию</option>
            <% if (request.getAttribute("categories") != null) { %>
            <% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
            <% for (Category category : categories) { %>
            <option value="<%=category.getId()%>"><%=category.getName()%> (id = <%=category.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <br/>
        <input type="text" name="brand" placeholder="Новый производитель/бренд"/>
        <br/>
        <input type="submit" value="Обновить"/>
    </form>

    <% if (request.getAttribute("isSuccess") != null) { %>
    <div class="ok-message" id="popupMessage">
        Товар успешно обновлен.
    </div>
    <% } %>

</div>

<div class="error-message">
    <% if (request.getAttribute("error") != null) { %>
    <%=request.getAttribute("error")%>
    <% } %>
</div>

<h4>Перечень всех товаров, хранящихся на складе</h4>

<table>
    <thead>
    <tr>
        <th>Идентификатор</th>
        <th>Наименование</th>
        <th>Описание</th>
        <th>Средняя цена закупки</th>
        <th>Средняя цена продажи</th>
        <th>Остаток на складе</th>
        <th>Категория</th>
        <th>Производитель</th>
    </tr>
    </thead>
    <% if (request.getAttribute("items") != null) { %>
    <% List<ItemExtended> items = (List<ItemExtended>) request.getAttribute("items"); %>
    <tbody>
    <% for (ItemExtended item : items) { %>
    <tr>
        <td><%=item.getId()%>
        </td>
        <td><%=item.getName()%>
        </td>
        <td><%=item.getDescription()%>
        </td>
        <td><%=item.getAveragePurchasePrice() == 0 ? "-" : String.format("%.2f", item.getAveragePurchasePrice())%>
        </td>
        <td><%=item.getAverageSalePrice() == 0 ? "-" : String.format("%.2f", item.getAverageSalePrice())%>
        </td>
        <td><%=item.getStockUnits()%>
        </td>
        <td><%=item.getCategoryId() == 0 || item.getCategoryName() == null ? "-" : item.getCategoryName() + " (id=" + item.getCategoryId() + ")"%>
        </td>
        <td><%=item.getBrand() == null ? "-" : item.getBrand()%>
        </td>
        <td style="border: none; width: 34px">
            <a style="text-decoration: none;" href="#" class="delete-link" data-item-id="<%=item.getId()%>">
                <div class="delete-icon">&#10006;</div>
            </a>
        </td>
    </tr>
    <% } %>
    </tbody>
    <% } %>
</table>

</body>
</html>