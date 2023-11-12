<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Удалить категорию</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Удалить категорию</h4>
    <form action="/category?action=delete" method="post">
        <input type="number" min="0" name="id" placeholder="Идентификатор категории" required/>
        <br/>
        <input type="submit" value="Удалить"/>
    </form>
    <% if (request.getAttribute("isSuccess") != null) { %>
    <p>Категория успешно удалена.</p>
    <% } %>
</div>
<div class="error-message">
    <% if (request.getAttribute("error") != null) { %>
    <%=request.getAttribute("error")%>
    <% } %>
</div>

</body>
</html>
