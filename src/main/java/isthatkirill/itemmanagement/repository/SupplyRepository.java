package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Supply;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;

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
