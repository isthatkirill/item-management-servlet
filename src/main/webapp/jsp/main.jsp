<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ItemManagement</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

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
</tr>
<% } %>
</tbody>
</table>
<% } %>

</body>
</html>