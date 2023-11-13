<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
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
        <input type="number" min="0" name="id" placeholder="Идентификатор поступления" required/>
        <br/>
        <input type="text" name="company" placeholder="Поставщик товара"/>
        <br/>
        <input type="text" name="receivedAt" placeholder="Дата поступления (дд-ММ-гггг чч:мм)"
               pattern="(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[1,2])-(19|20)\d{2} ([01][0-9]|2[0-3]):([0-5][0-9])"/>
        <br/>
        <input type="number" step="0.01" min="0" name="price" placeholder="Цена закупки"/>
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

</body>
</html>