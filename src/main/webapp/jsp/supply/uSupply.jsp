<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.Supply" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.SupplyExtended" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Редактировать поступление</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Редактировать поступление</h4>
    <form action="/supply?action=update" method="post">
        <select name="id" required>
            <option value="" selected>Выберите поступление</option>
            <% if (request.getAttribute("supplies") != null) { %>
            <% List<SupplyExtended> supplies = (List<SupplyExtended>) request.getAttribute("supplies"); %>
            <% for (SupplyExtended supply : supplies) { %>
            <option value="<%=supply.getId()%>">Поступление id=<%=supply.getId()%> (Товар: <%=supply.getItemName()%>, id=<%=supply.getItemId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <input type="text" name="company" placeholder="Новый поставщик товара"/>
        <br/>
        <input type="text" name="receivedAt" placeholder="Новая дата поступления (дд-ММ-гггг чч:мм)"
               pattern="(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[1,2])-(19|20)\d{2} ([01][0-9]|2[0-3]):([0-5][0-9])"/>
        <br/>
        <input type="number" step="0.01" min="0" name="price" placeholder="Новая цена закупки"/>
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

<h4>Перечень всех поступлений</h4>

<table>
    <thead>
    <tr>
        <th>Идентификатор</th>
        <th>Поставщик</th>
        <th>Дата поступления</th>
        <th>Количество</th>
        <th>Цена закупки</th>
        <th>Товар</th>
    </tr>
    </thead>
    <% if (request.getAttribute("supplies") == null) { %>
    <tbody>
    <tr>
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
<% List<SupplyExtended> supplies = (List<SupplyExtended>) request.getAttribute("supplies"); %>
<tbody>
<% for (SupplyExtended supply : supplies) { %>
<tr>
    <td><%=supply.getId()%>
    </td>
    <td><%=supply.getCompany() == null ? "-" : supply.getCompany()%>
    </td>
    <td><%=supply.getReceivedAt().format(Constants.FORMATTER)%>
    </td>
    <td><%=supply.getAmount()%>
    </td>
    <td><%=String.format("%.2f", supply.getPrice())%>
    </td>
    <td><%=supply.getItemName()%> (id=<%=supply.getItemId()%>)
    </td>
</tr>
<% } %>
</tbody>
</table>
<% } %>

</body>
</html>