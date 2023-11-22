<%@ page import="isthatkirill.itemmanagement.util.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="isthatkirill.itemmanagement.model.item.ItemExtended" %>
<%@ page import="isthatkirill.itemmanagement.model.item.enums.SortType" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ItemManagement</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
    <script src="/scripts/theme.js" defer></script>
    <script src="/scripts/sort.js" defer></script>
</head>
<body>

<%=Constants.COMMON_NAVBAR%>

<h4>Перечень всех товаров, хранящихся на складе</h4>

<table>
    <thead>
    <tr>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_ID.getValue()%>" title="Сортировка по идентификатору">
            Идентификатор
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_NAME.getValue()%>" title="Сортировка по наименованию">
            Наименование
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_DESCRIPTION.getValue()%>" title="Сортировка по описанию">
            Описание
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_PURCHASE_PRICE.getValue()%>"
            title="Сортировка по средней цене закупки">Средняя цена закупки
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_SALE_PRICE.getValue()%>"
            title="Сортировка по средней цене продажи">Средняя цена продажи
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_STOCK_UNITS.getValue()%>"
            title="Сортировка по остатку на складе">Остаток на складе
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_CATEGORY_ID.getValue()%>" title="Сортировка по категории">
            Категория
        </th>
        <th class="th-main" data-sort="<%=SortType.SORT_BY_BRAND.getValue()%>" title="Сортировка по производителю">
            Производитель
        </th>
    </tr>
    </thead>
    <% if (request.getAttribute("items") != null) { %>
    <% List<ItemExtended> items = (List<ItemExtended>) request.getAttribute("items"); %>
    <tbody>
    <% for (ItemExtended item : items) { %>
    <tr>
        <td><%=item.getId()%>
        </td>
        <td><%=item.getName()%>
        </td>
        <td><%=item.getDescription()%>
        </td>
        <td><%=item.getAveragePurchasePrice() == 0 ? "-" : String.format("%.2f", item.getAveragePurchasePrice())%>
        </td>
        <td><%=item.getAverageSalePrice() == 0 ? "-" : String.format("%.2f", item.getAverageSalePrice())%>
        </td>
        <td><%=item.getStockUnits()%>
        </td>
        <td><%=item.getCategoryId() == 0 || item.getCategoryName() == null ? "-" : item.getCategoryName() + " (id=" + item.getCategoryId() + ")"%>
        </td>
        <td><%=item.getBrand() == null ? "-" : item.getBrand()%>
        </td>
    </tr>
    <% } %>
    </tbody>
    <% } %>
</table>


</body>
</html>