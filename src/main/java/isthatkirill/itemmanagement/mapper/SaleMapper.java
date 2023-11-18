package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.sale.Sale;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

public class SaleMapper {

    private SaleMapper() {

    }

    public static Sale extractSaleFromRequest(HttpServletRequest request) {
        Sale sale = new Sale();
        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) sale.setId(Long.valueOf(id));

        String amount = request.getParameter("amount");
        if (amount != null && !amount.isBlank()) sale.setAmount(Long.valueOf(amount));

        String price = request.getParameter("price");
        if (price != null && !price.isBlank()) sale.setPrice(Double.valueOf(price));

        String itemId = request.getParameter("itemId");
        if (itemId != null && !itemId.isBlank()) sale.setItemId(Long.valueOf(itemId));

        return sale;
    }

    @SneakyThrows
    public static List<SaleExtended> extractSalesExtendedFromResultSet(ResultSet resultSet) {
        List<SaleExtended> sales = new ArrayList<>();
        while (resultSet.next()) {
            sales.add(mapResultSetToSaleExtended(resultSet));
        }
        return sales;
    }

    @SneakyThrows
    public static Optional<Sale> extractSaleFromResultSet(ResultSet resultSet) {
        if (!resultSet.next()) return Optional.empty();
        return Optional.of(mapResultSetToSale(resultSet));
    }

    @SneakyThrows
    private static SaleExtended mapResultSetToSaleExtended(ResultSet resultSet) {
        SaleExtended sale = new SaleExtended();
        mapCommonFields(resultSet, sale);
        sale.setItemName(resultSet.getString("item_name"));
        return sale;
    }

    @SneakyThrows
    private static Sale mapResultSetToSale(ResultSet resultSet) {
        Sale sale = new Sale();
        mapCommonFields(resultSet, sale);
        return sale;
    }

    private static void mapCommonFields(ResultSet resultSet, Sale sale) throws SQLException {
        sale.setId(resultSet.getLong("id"));
        sale.setAmount(resultSet.getLong("amount"));
        sale.setPrice(resultSet.getDouble("price"));
        sale.setItemId(resultSet.getLong("item_id"));
        sale.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
    }

}
