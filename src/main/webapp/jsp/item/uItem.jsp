<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Редактировать товар</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Редактировать товар</h4>
    <form action="/item?action=update" method="post">
        <input type="number" min="0" name="id" placeholder="Идентификатор товара" required/>
        <br/>
        <input type="text" name="name" placeholder="Наименование товара"/>
        <br/>
        <input type="text" name="description" placeholder="Описание товара"/>
        <br/>
        <select name="categoryId">
            <option value="" selected>Выберите категорию</option>
            <% if (request.getAttribute("categories") != null) { %>
            <% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
                <% for (Category category : categories) { %>
            <option value="<%=category.getId()%>"><%=category.getName()%> (id = <%=category.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <br/>
        <input type="text" name="brand" placeholder="Производитель/бренд"/>
        <br/>
        <input type="submit" value="Обновить"/>
    </form>
</div>

<div class="ok-message">
    <% if (request.getAttribute("isSuccess") != null) { %>
    Товар успешно обновлен.
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
        <th>Номер категории</th>
        <th>Производитель</th>
    </tr>
    </thead>
    <% if (request.getAttribute("items") == null) { %>
    <tbody>
    <tr>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
    </tr>
    </tbody>
</table>
<% } else { %>
<% List<Item> items = (List<Item>) request.getAttribute("items"); %>
<tbody>
<% for (Item item : items) { %>
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
    <td><%=item.getCategoryId() == 0 ? "-" : item.getCategoryId()%>
    </td>
    <td><%=item.getBrand() == null ? "-" : item.getBrand()%>
    </td>
    <td style="border: none; width: 34px"><a href="/item?action=button-delete-<%=item.getId()%>">
        <img src="/images/delete.png" height="30px" width="30px">
    </a>
    </td>
</tr>
<% } %>
</tbody>
</table>
<% } %>
</body>
</html>