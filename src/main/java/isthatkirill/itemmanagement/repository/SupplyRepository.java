package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import org.postgresql.ds.PGSimpleDataSource;

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

public class SupplyRepository {

    public Long create(Supply supply) {
        String query = "INSERT INTO supplies (company, amount, price, item_id, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, supply.getCompany());
            statement.setLong(2, supply.getAmount());
            statement.setDouble(3, supply.getPrice());
            statement.setLong(4, supply.getItemId());
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
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
            if (resultSet.next()) return resultSet.getDouble("average_price");
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

    public Integer findAllUnits(Long itemId) {
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
