package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.model.Item;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public class ItemRepository {

    @SneakyThrows
    public Long create(Item item) {
        String query = "INSERT INTO items (name, description, purchase_price, stock_units, brand, created_at, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getNewConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setDouble(3, item.getPurchasePrice());
            statement.setInt(4, item.getStockUnits());
            if (item.getBrand() == null) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, item.getBrand());
            }
            statement.setTimestamp(6, Timestamp.valueOf(item.getCreatedAt()));
            if (item.getCategoryId() == null) {
                statement.setNull(7, Types.BIGINT);
            } else {
                statement.setLong(7, item.getCategoryId());
            }
            statement.executeUpdate();

            ResultSet key = statement.getGeneratedKeys();
            key.next();
            return key.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SneakyThrows
    public List<Item> findAll() {
        String query = "SELECT * FROM items";
        try (Connection connection = getNewConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
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
