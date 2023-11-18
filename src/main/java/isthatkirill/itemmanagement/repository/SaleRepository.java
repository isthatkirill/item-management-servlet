package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.SaleMapper;
import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.sale.Sale;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

public class SaleRepository {

    private final DataSource dataSource;

    public SaleRepository() {
        dataSource = ConnectionHelper.getDataSource();
    }

    public Long create(Sale sale) {
        String query = "INSERT INTO sales (amount, price, item_id, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, sale.getAmount());
            statement.setDouble(2, sale.getPrice());
            statement.setLong(3, sale.getItemId());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();

            ResultSet key = statement.getGeneratedKeys();
            key.next();
            return key.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Double findAverageSalePrice(Long itemId) {
        String query = "SELECT CAST(SUM(amount * price) AS DECIMAL(10, 2)) / SUM(amount) AS average_price FROM sales WHERE item_id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getDouble("average_price");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0D;
    }

    public List<SaleExtended> findAllExtended() {
        String query = "SELECT s.*, i.name as item_name FROM sales s LEFT JOIN items i ON i.id = s.item_id ORDER BY s.id ASC;";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return SaleMapper.extractSalesExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Optional<Sale> findById(Long id) {
        String query = "SELECT * FROM sales WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return SaleMapper.extractSaleFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void update(Sale sale) {
        StringBuilder query = new StringBuilder("UPDATE sales SET ");
        List<Object> values = new ArrayList<>();

        if (sale.getPrice() != null) {
            query.append("price = ?, ");
            values.add(sale.getPrice());
        }

        if (sale.getAmount() != null) {
            query.append("amount = ?, ");
            values.add(sale.getAmount());
        }

        if (values.isEmpty()) {
            return;
        }

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        values.add(sale.getId());

        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getNewConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
