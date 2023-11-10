package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.CategoryMapper;
import isthatkirill.itemmanagement.model.Category;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

public class CategoryRepository {

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

    @SneakyThrows
    public Long create(Category category) {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";
        Connection connection = getNewConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, category.getName());
        statement.setString(2, category.getDescription());
        statement.executeUpdate();

        ResultSet key = statement.getGeneratedKeys();
        key.next();
        Long generatedKey = key.getLong(1);

        statement.close();
        connection.close();

        return generatedKey;
    }

    @SneakyThrows
    public Optional<Category> findById(Long id) {
        String query = "SELECT * FROM categories WHERE id = ?";
        Connection connection = getNewConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        return CategoryMapper.extractCategoryFromResultSet(resultSet);
    }

}
