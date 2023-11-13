<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактировать категорию</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
    <h4>Редактировать категорию</h4>
    <form action="/category?action=update" method="post">
        <input type="number" min="0" name="id" placeholder="Идентификатор категории" required/>
        <br/>
        <input type="text" name="name" placeholder="Название категории"/>
        <br/>
        <input type="text" name="description" placeholder="Описание категории"/>
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
    <% if (request.getAttribute("categories") == null) { %>
    <tbody>
    <tr>
        <td>-</td>
        <td>-</td>
        <td>-</td>
    </tr>
    </tbody>
</table>
<% } else { %>
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
    <td style="border: none; width: 34px"><a href="/category?action=button-delete-<%=category.getId()%>">
        <img src="/images/delete.png" height="30px" width="30px">
    </a>
    </td>
</tr>
<% } %>
</tbody>
</table>
<% } %>


</body>
</html>