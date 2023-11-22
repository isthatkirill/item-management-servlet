package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SupplyMapper {

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

        return supply;
    }

    @SneakyThrows
    public static List<SupplyExtended> extractSuppliesExtendedFromResultSet(ResultSet resultSet) {
        List<SupplyExtended> supplies = new ArrayList<>();
        while (resultSet.next()) {
            supplies.add(mapResultSetToSupplyExtended(resultSet));
        }
        return supplies;
    }

    @SneakyThrows
    public static Optional<Supply> extractSupplyFromResultSet(ResultSet resultSet) {
        if (!resultSet.next()) return Optional.empty();
        return Optional.of(mapResultSetToSupply(resultSet));
    }

    @SneakyThrows
    private static SupplyExtended mapResultSetToSupplyExtended(ResultSet resultSet) {
        SupplyExtended supply = new SupplyExtended();
        mapCommonFields(resultSet, supply);
        supply.setItemName(resultSet.getString("item_name"));
        return supply;
    }

    @SneakyThrows
    private static Supply mapResultSetToSupply(ResultSet resultSet) {
        Supply supply = new Supply();
        mapCommonFields(resultSet, supply);
        return supply;
    }

    private static void mapCommonFields(ResultSet resultSet, Supply supply) throws SQLException {
        supply.setId(resultSet.getLong("id"));
        supply.setCompany(resultSet.getString("company"));
        supply.setAmount(resultSet.getLong("amount"));
        supply.setPrice(resultSet.getDouble("price"));
        supply.setItemId(resultSet.getLong("item_id"));
        supply.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
    }


    private static String decode(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

}
