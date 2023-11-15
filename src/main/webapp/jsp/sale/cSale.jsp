<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="isthatkirill.itemmanagement.model.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.ItemShort" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Добавить продажу</title>
  <link rel="stylesheet" type="text/css" href="/styles/main.css">
  <link rel="stylesheet" type="text/css" href="/styles/form.css">
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<div class="container-form">
  <h4>Добавить продажу</h4>
  <form action="/sale?action=create" method="post">
    <input type="number" name="amount" min="1" placeholder="Количество (шт.)" required/>
    <br/>
    <input type="number" step="0.01" min="0" name="price" placeholder="Цена продажи" required/>
    <br/>
    <select name="itemId" required>
      <option value="" selected>Выберите товар</option>
      <% if (request.getAttribute("items") != null) { %>
      <% List<ItemShort> items = (List<ItemShort>) request.getAttribute("items"); %>
      <% for (ItemShort item : items) { %>
      <option value="<%=item.getId()%>"><%=item.getName()%> (id = <%=item.getId()%>)</option>
      <% } %>
      <% } %>
    </select>
    <input type="submit" value="Добавить"/>
  </form>
  <% if (request.getAttribute("generatedId") != null) { %>
  <% Long id = (Long) request.getAttribute("generatedId"); %>
  <p>Новая продажа успешно добавлена. Присвоенный идентификатор id = <%=id%>.</p>
  <% } %>
</div>
<div class="error-message">
  <% if (request.getAttribute("error") != null) { %>
  <%=request.getAttribute("error")%>
  <% } %>
</div>

</body>
</html>