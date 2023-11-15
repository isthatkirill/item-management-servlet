package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.model.Supply;
import isthatkirill.itemmanagement.model.SupplyExtended;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

public class SupplyRepository {

    public Long create(Supply supply) {
        String query = "INSERT INTO supplies (company, received_at, amount, price, item_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, supply.getCompany());
            statement.setTimestamp(2, Timestamp.valueOf(supply.getReceivedAt()));
            statement.setLong(3, supply.getAmount());
            statement.setDouble(4, supply.getPrice());
            statement.setLong(5, supply.getItemId());
            statement.executeUpdate();

            ResultSet key = statement.getGeneratedKeys();
            key.next();
            return key.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Double findAveragePurchasePrice(Long itemId) {
        String query = "SELECT CAST(SUM(amount * price) AS DECIMAL(10, 2)) / SUM(amount) AS average_price FROM supplies WHERE item_id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("average_price");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0D;
    }

    public List<SupplyExtended> findAllExtended() {
        String query = "SELECT s.*, i.name as item_name FROM supplies s LEFT JOIN items i ON i.id = s.item_id ORDER BY s.id ASC;";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return SupplyMapper.extractSuppliesExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Integer findAllUnits (Long itemId) {
        String query = "SELECT SUM(amount) AS stock_units FROM supplies WHERE item_id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("stock_units");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void update(Item item) {
        StringBuilder query = new StringBuilder("UPDATE items SET ");
        List<Object> values = new ArrayList<>();

        if (item.getName() != null) {
            query.append("name = ?, ");
            values.add(item.getName());
        }

        if (item.getDescription() != null) {
            query.append("description = ?, ");
            values.add(item.getDescription());
        }

        if (item.getCategoryId() != null) {
            query.append("category_id = ?, ");
            values.add(item.getCategoryId());
        }

        if (item.getBrand() != null) {
            query.append("brand = ?, ");
            values.add(item.getBrand());
        }

        if (values.isEmpty()) {
            return;
        }

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        values.add(item.getId());

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

    public void update(Supply supply) {
        StringBuilder query = new StringBuilder("UPDATE supplies SET ");
        List<Object> values = new ArrayList<>();

        if (supply.getCompany() != null) {
            query.append("company = ?, ");
            values.add(supply.getCompany());
        }

        if (supply.getPrice() != null) {
            query.append("price = ?, ");
            values.add(supply.getPrice());
        }

        if (supply.getReceivedAt() != null) {
            query.append("received_at = ?, ");
            values.add(supply.getReceivedAt());
        }

        if (values.isEmpty()) {
            return;
        }

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        values.add(supply.getId());

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

    public Optional<Supply> findById(Long id) {
        String query = "SELECT * FROM supplies WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return SupplyMapper.extractSupplyFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Connection getNewConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    private DataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setDatabaseName("item-managment");
        dataSource.setUser("postgres");
        dataSource.setPassword("admin");
        return dataSource;
    }

}
