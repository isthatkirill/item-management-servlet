<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.supply.Supply" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.supply.SupplyExtended" %>
<%@ page import="isthatkirill.itemmanagement.model.sale.SaleExtended" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Редактировать поступление</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
    <script src="/scripts/theme.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Редактировать продажи</h4>
    <form action="/sale?action=update" method="post">
        <select name="id" required>
            <option value="" selected>Выберите продажу</option>
            <% if (request.getAttribute("sales") != null) { %>
            <% List<SaleExtended> sales = (List<SaleExtended>) request.getAttribute("sales"); %>
            <% for (SaleExtended sale : sales) { %>
            <option value="<%=sale.getId()%>">Продажа id=<%=sale.getId()%> (Товар: <%=sale.getItemName()%>, id=<%=sale.getItemId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <input type="number" name="amount" min="1" placeholder="Новое количество (шт.)"/>
        <br/>
        <input type="number" step="0.01" min="0" name="price" placeholder="Новая цена продажи"/>
        <br/>
        <input type="submit" value="Обновить"/>
    </form>
    <% if (request.getAttribute("isSuccess") != null) { %>
    <p>Поступление успешно обновлено.</p>
    <% } %>
</div>
<div class="error-message">
    <% if (request.getAttribute("error") != null) { %>
    <%=request.getAttribute("error")%>
    <% } %>
</div>

<h4>Перечень всех продаж</h4>

<table>
    <thead>
    <tr>
        <th>Идентификатор</th>
        <th>Количество</th>
        <th>Цена продажи</th>
        <th>Товар</th>
    </tr>
    </thead>
    <% if (request.getAttribute("sales") != null) { %>
    <% List<SaleExtended> sales = (List<SaleExtended>) request.getAttribute("sales"); %>
    <tbody>
    <% for (SaleExtended sale : sales) { %>
    <tr>
        <td><%=sale.getId()%>
        </td>
        <td><%=sale.getAmount()%>
        </td>
        <td><%=String.format("%.2f", sale.getPrice())%>
        </td>
        <td><%=sale.getItemName()%> (id=<%=sale.getItemId()%>)
        </td>
    </tr>
    <% } %>
    </tbody>
    <% } %>
</table>

</body>
</html>