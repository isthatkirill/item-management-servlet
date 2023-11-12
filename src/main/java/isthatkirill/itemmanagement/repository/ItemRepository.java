package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.CategoryMapper;
import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.model.Item;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

public class ItemRepository {

    @SneakyThrows
    public Long create(Item item) {
        String query = "INSERT INTO items (name, description, purchase_price, sale_price, stock_units, brand, created_at, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getNewConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setNull(3, Types.DECIMAL);
            statement.setNull(4, Types.DECIMAL);
            statement.setInt(5, item.getStockUnits());
            if (item.getBrand() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, item.getBrand());
            }
            statement.setTimestamp(7, Timestamp.valueOf(item.getCreatedAt()));
            if (item.getCategoryId() == null) {
                statement.setNull(8, Types.BIGINT);
            } else {
                statement.setLong(8, item.getCategoryId());
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
    public Optional<Item> findById(Long id) {
        String query = "SELECT * FROM items WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @SneakyThrows
    public List<Item> findAll() {
        String query = "SELECT * FROM items";
        try (Connection connection = getNewConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemsFromResultSet(resultSet);
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
