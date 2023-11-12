<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Обновить товар</title>
  <link rel="stylesheet" type="text/css" href="/styles/main.css">
  <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
  <h4>Обновить товар</h4>
  <form action="/item?action=update" method="post">
    <input type="number" min="0" name="id" placeholder="Идентификатор товара" required/>
    <br/>
    <input type="text" name="name" placeholder="Наименование товара"/>
    <br/>
    <input type="text" name="description" placeholder="Описание товара"/>
    <br/>
    <input type="number" name="categoryId" min="0" placeholder="Номер категории"/>
    <br/>
    <input type="text" name="brand" placeholder="Производитель/бренд"/>
    <br/>
    <input type="submit" value="Обновить"/>
  </form>
  <% if (request.getAttribute("isSuccess") != null) { %>
  <p>Товар успешно обновлен.</p>
  <% } %>
</div>
<div class="error-message">
  <% if (request.getAttribute("error") != null) { %>
  <%=request.getAttribute("error")%>
  <% } %>
</div>

</body>
</html>