<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.Category" %>
<%@ page import="isthatkirill.itemmanagement.model.Item" %>
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
        <input type="number" min="0" name="id" placeholder="Идентификатор товара" required/>
        <br/>
        <input type="submit" value="Получить"/>
    </form>
</div>
<% if (request.getAttribute("item") != null) { %>
<% Item item = (Item) request.getAttribute("item"); %>
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
    <tbody>
    <tr>
        <td><%=item.getId()%>
        </td>
        <td><%=item.getName()%>
        </td>
        <td><%=item.getDescription()%>
        </td>
        <td><%=item.getAveragePurchasePrice() == 0? "-" : item.getAveragePurchasePrice()%>
        </td>
        <td><%=item.getAverageSalePrice() == 0 ? "-" : item.getAverageSalePrice()%>
        </td>
        <td><%=item.getStockUnits()%>
        </td>
        <td><%=item.getCategoryId() == 0 ? "-" : item.getCategoryId()%>
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