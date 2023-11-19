<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.category.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Получить категорию</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
    <script src="/scripts/theme.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Получить информацию о категории</h4>
    <form action="/category?action=read" method="post">
        <select style="width: 100%" name="id" required>
            <option value="" selected>Выберите категорию</option>
            <% if (request.getAttribute("categories") != null) { %>
            <% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
            <% for (Category category : categories) { %>
            <option value="<%=category.getId()%>"><%=category.getName()%> (id = <%=category.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <input type="submit" value="Получить"/>
    </form>
</div>
<% if (request.getAttribute("category") != null) { %>
<% Category category = (Category) request.getAttribute("category"); %>
<table>
    <thead>
    <tr>
        <th>Идентификатор</th>
        <th>Название</th>
        <th>Описание</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><%=category.getId()%></td>
        <td><%=category.getName()%></td>
        <td><%=category.getDescription()%></td>
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
