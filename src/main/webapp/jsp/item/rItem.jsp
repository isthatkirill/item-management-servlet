<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.category.Category" %>
<%@ page import="isthatkirill.itemmanagement.model.item.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.item.ItemExtended" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Получить товар</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Получить информацию о товаре</h4>
    <form action="/item?action=read" method="post">
        <select name="id" required>
            <option value="" selected>Выберите товар</option>
            <% if (request.getAttribute("items") != null) { %>
            <% List<ItemExtended> items = (List<ItemExtended>) request.getAttribute("items"); %>
            <% for (ItemExtended item : items) { %>
            <option value="<%=item.getId()%>"><%=item.getName()%> (id = <%=item.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <input type="submit" value="Получить"/>
    </form>
</div>
<% if (request.getAttribute("item") != null) { %>
<% ItemExtended item = (ItemExtended) request.getAttribute("item"); %>
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
    <tbody>
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
    </tr>
    </tbody>
</table>
<% } %>
<div class="error-message">
    <% if (request.getAttribute("error") != null) { %>
    <%=request.getAttribute("error")%>
    <% } %>
</div>

</body>
</html>