package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.Item;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public class ItemMapper {

    private ItemMapper() {

    }

    public static Item extractItemFromRequest(HttpServletRequest request) {
        Item item = new Item();
        String name = request.getParameter("name");
        if (name != null && !name.isBlank()) item.setName(decode(name));

        String description = request.getParameter("description");
        if (description != null && !description.isBlank()) item.setDescription(decode(description));

        String purchasePrice = request.getParameter("purchasePrice");
        if (purchasePrice != null && !purchasePrice.isBlank()) item.setPurchasePrice(Double.valueOf(purchasePrice));

        String stockUnits = request.getParameter("stockUnits");
        if (stockUnits != null && !stockUnits.isBlank()) item.setStockUnits(Integer.valueOf(stockUnits));

        String categoryId = request.getParameter("categoryId");
        if (categoryId != null && !categoryId.isBlank()) item.setCategoryId(Long.valueOf(categoryId));

        String brand = request.getParameter("brand");
        if (brand != null && !brand.isBlank()) item.setBrand(decode(brand));

        return item;
    }

    @SneakyThrows
    public static List<Item> extractItemFromResultSet(ResultSet resultSet) {
        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            items.add(mapResultSetToItem(resultSet));
        }
        return items;
    }

    @SneakyThrows
    private static Item mapResultSetToItem(ResultSet resultSet) {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(resultSet.getString("name"));
        item.setDescription(resultSet.getString("description"));
        item.setPurchasePrice(resultSet.getDouble("purchase_price"));
        item.setStockUnits(resultSet.getInt("stock_units"));
        item.setBrand(resultSet.getString("brand"));
        item.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        item.setCategoryId(resultSet.getLong("category_id"));
        return item;
    }

    private static String decode(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

}
