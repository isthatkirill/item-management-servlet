package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Item;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        String categoryId = request.getParameter("categoryId");
        if (categoryId != null && !categoryId.isBlank()) item.setCategoryId(Long.valueOf(categoryId));

        String brand = request.getParameter("brand");
        if (brand != null && !brand.isBlank()) item.setBrand(decode(brand));

        return item;
    }

    @SneakyThrows
    public static List<Item> extractItemsFromResultSet(ResultSet resultSet) {
        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            items.add(mapResultSetToItem(resultSet));
        }
        return items;
    }

    @SneakyThrows
    public static Optional<Item> extractItemFromResultSet(ResultSet resultSet) {
        if (!resultSet.next()) return Optional.empty();
        return Optional.of(mapResultSetToItem(resultSet));
    }

    @SneakyThrows
    private static Item mapResultSetToItem(ResultSet resultSet) {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(resultSet.getString("name"));
        item.setDescription(resultSet.getString("description"));
        item.setAveragePurchasePrice(resultSet.getDouble("purchase_price"));
        item.setAverageSalePrice(resultSet.getDouble("sale_price"));
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
