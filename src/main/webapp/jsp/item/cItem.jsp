<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.category.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить товар</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
    <script src="/scripts/theme.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Добавить товар</h4>
    <form action="/item?action=create" method="post">
        <input type="text" name="name" placeholder="Наименование товара" required/>
        <br/>
        <input type="text" name="description" placeholder="Описание товара" required/>
        <br/>
        <select name="categoryId">
            <option value="" selected>Выберите категорию</option>
            <% if (request.getAttribute("categories") != null) { %>
            <% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
            <% for (Category category : categories) { %>
            <option value="<%=category.getId()%>"><%=category.getName()%> (id = <%=category.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <input type="text" name="brand" placeholder="Производитель/бренд"/>
        <br/>
        <input type="submit" value="Добавить"/>
    </form>
    <div class="ok-message">
        <% if (request.getAttribute("generatedId") != null) { %>
        <% Long id = (Long) request.getAttribute("generatedId"); %>
        <p>Новый товар успешно добавлен. Присвоенный идентификатор id = <%=id%>.</p>
        <% } %>
    </div>
</div>
<div class="error-message">
    <% if (request.getAttribute("error") != null) { %>
    <%=request.getAttribute("error")%>
    <% } %>
</div>

</body>
</html>
