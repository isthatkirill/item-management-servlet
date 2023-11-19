<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.category.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактировать категорию</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
    <script src="/scripts/theme.js" defer></script>
    <script src="/scripts/dialog.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Редактировать категорию</h4>
    <form action="/category?action=update" method="post">
        <select style="width: 100%" name="id" required>
            <option value="" selected>Выберите категорию</option>
            <% if (request.getAttribute("categories") != null) { %>
            <% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
            <% for (Category category : categories) { %>
            <option value="<%=category.getId()%>"><%=category.getName()%> (id = <%=category.getId()%>)</option>
            <% } %>
            <% } %>
        </select>
        <br/>
        <input type="text" name="name" placeholder="Новое название категории"/>
        <br/>
        <input type="text" name="description" placeholder="Новое описание категории"/>
        <br/>
        <input type="submit" value="Обновить"/>
    </form>
</div>

<div class="ok-message">
    <% if (request.getAttribute("isSuccess") != null) { %>
    Категория успешно обновлена.
    <% } %>
</div>

<div class="error-message">
    <% if (request.getAttribute("error") != null) { %>
    <%=request.getAttribute("error")%>
    <% } %>
</div>

<h4>Перечень доступных категорий</h4>

<table>
    <thead>
    <tr>
        <th>Идентификатор</th>
        <th>Наименование</th>
        <th>Описание</th>
    </tr>
    </thead>
<% if (request.getAttribute("categories") != null)  { %>
<% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
<tbody>
<% for (Category category : categories) { %>
<tr>
    <td><%=category.getId()%>
    </td>
    <td><%=category.getName()%>
    </td>
    <td><%=category.getDescription()%>
    </td>
    <td style="border: none; width: 34px">
        <a style="text-decoration: none;" href="#" class="delete-link" data-category-id="<%=category.getId()%>">
            <div class="delete-icon">&#10006;</div>
        </a>
    </td>
</tr>
<% } %>
</tbody>
<% } %>
</table>


</body>
</html>