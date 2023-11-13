<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Добавить поступление</title>
  <link rel="stylesheet" type="text/css" href="/styles/main.css">
  <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
  <h4>Добавить поступление</h4>
  <form action="/supply?action=create" method="post">
    <input type="text" name="company" placeholder="Поставщик товара"/>
    <br/>
    <input type="text" name="receivedAt" placeholder="Дата поставки (дд-ММ-гггг чч:мм)"
           pattern="(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[1,2])-(19|20)\d{2} ([01][0-9]|2[0-3]):([0-5][0-9])" required/>
    <br/>
    <input type="number" name="amount" min="0" placeholder="Количество (шт.)" required/>
    <br/>
    <input type="number" step="0.01" min="0" name="price" placeholder="Цена закупки" required/>
    <br/>
    <input type="number" name="itemId" min="0" placeholder="Номер товара" required/>
    <br/>
    <input type="submit" value="Добавить"/>
  </form>
  <% if (request.getAttribute("generatedId") != null) { %>
  <% Long id = (Long) request.getAttribute("generatedId"); %>
  <p>Новое поступление успешно добавлено. Присвоенный идентификатор id = <%=id%>.</p>
  <% } %>
</div>
<div class="error-message">
  <% if (request.getAttribute("error") != null) { %>
  <%=request.getAttribute("error")%>
  <% } %>
</div>

</body>
</html>
