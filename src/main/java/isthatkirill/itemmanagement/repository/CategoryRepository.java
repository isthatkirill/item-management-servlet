package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.CategoryMapper;
import isthatkirill.itemmanagement.model.category.Category;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static isthatkirill.itemmanagement.util.Constants.*;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class CategoryRepository {

    private final DataSource dataSource;

    public CategoryRepository() {
        dataSource = ConnectionHelper.getDataSource();
    }


    public Long create(Category category) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();

            ResultSet key = statement.getGeneratedKeys();
            key.next();
            return key.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Optional<Category> findById(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_CATEGORY_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return CategoryMapper.extractCategoryFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean existsById(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_CATEGORY_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void update(Category category) {
        StringBuilder query = new StringBuilder("UPDATE categories SET ");
        List<Object> values = new ArrayList<>();

        if (category.getName() != null) {
            query.append("name = ?, ");
            values.add(category.getName());
        }

        if (category.getDescription() != null) {
            query.append("description = ?, ");
            values.add(category.getDescription());
        }

        if (values.isEmpty()) {
            return;
        }

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        values.add(category.getId());

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

    @SneakyThrows
    public List<Category> findAll() {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_CATEGORIES)) {
            ResultSet resultSet = statement.executeQuery();
            return CategoryMapper.extractCategoriesFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @SneakyThrows
    public void delete(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY_BY_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Connection getNewConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
