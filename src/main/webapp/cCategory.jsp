<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить категорию</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Добавить категорию</h4>
    <form id="itemForm" action="/item?action=create-category" method="post">
        <input type="text" name="name" placeholder="Название категории" required/>
        <br/>
        <input type="text" name="description" placeholder="Описание категории" required/>
        <br/>
        <input type="submit" value="Добавить"/>
    </form>
    <% if (request.getAttribute("generatedId") != null) { %>
    <% Long id = (Long) request.getAttribute("generatedId"); %>
    <p>Новая категория успешно добавлена. Присвоенный идентификатор id = <%=id%>.</p>
    <% } %>
</div>

</body>
</html>
