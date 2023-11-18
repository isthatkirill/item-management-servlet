package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.model.sale.Sale;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * @author Kirill Emelyanov
 */

public class SaleRepository {

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
