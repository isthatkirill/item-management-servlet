package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.model.Supply;
import isthatkirill.itemmanagement.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

public class SupplyMapper {

    private SupplyMapper() {

    }

    public static Supply extractSupplyFromRequest(HttpServletRequest request) {
        Supply supply = new Supply();
        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) supply.setId(Long.valueOf(id));

        String company = request.getParameter("company");
        if (company != null && !company.isBlank()) supply.setCompany(decode(company));

        String amount = request.getParameter("amount");
        if (amount != null && !amount.isBlank()) supply.setAmount(Long.valueOf(amount));

        String price = request.getParameter("price");
        if (price != null && !price.isBlank()) supply.setPrice(Double.valueOf(price));

        String itemId = request.getParameter("itemId");
        if (itemId != null && !itemId.isBlank()) supply.setItemId(Long.valueOf(itemId));

        String receivedAt = request.getParameter("receivedAt");
        if (receivedAt != null && !receivedAt.isBlank()) supply.setReceivedAt(LocalDateTime.parse(receivedAt, Constants.FORMATTER));

        return supply;
    }

    @SneakyThrows
    public static List<Supply> extractSuppliesFromResultSet(ResultSet resultSet) {
        List<Supply> supplies = new ArrayList<>();
        while (resultSet.next()) {
            supplies.add(mapResultSetToSupply(resultSet));
        }
        return supplies;
    }

    @SneakyThrows
    public static Optional<Supply> extractSupplyFromResultSet(ResultSet resultSet) {
        if (!resultSet.next()) return Optional.empty();
        return Optional.of(mapResultSetToSupply(resultSet));
    }

    @SneakyThrows
    private static Supply mapResultSetToSupply(ResultSet resultSet) {
        Supply supply = new Supply();
        supply.setId(resultSet.getLong("id"));
        supply.setCompany(resultSet.getString("company"));
        supply.setReceivedAt(resultSet.getTimestamp("received_at").toLocalDateTime());
        supply.setAmount(resultSet.getLong("amount"));
        supply.setPrice(resultSet.getDouble("price"));
        supply.setItemId(resultSet.getLong("item_id"));
        return supply;
    }

    private static String decode(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

}
