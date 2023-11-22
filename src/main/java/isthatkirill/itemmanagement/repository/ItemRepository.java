package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.mapper.ReportDataMapper;
import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;
import jakarta.enterprise.context.ApplicationScoped;

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
public class ItemRepository {

    private final DataSource dataSource;

    public ItemRepository() {
        dataSource = ConnectionHelper.getDataSource();
    }


    public Long create(Item item) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ITEM, Statement.RETURN_GENERATED_KEYS)) {
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

    public Optional<ItemExtended> findById(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ITEM_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Long findStockById(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_STOCK_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("stock_units");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public boolean existsById(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_ITEM_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<ItemExtended> findAllExtended(String sortBy, String sortOrder) {
        StringBuilder query = new StringBuilder("SELECT i.*, c.name as category_name FROM items i LEFT JOIN categories c ON i.category_id = c.id ");
        if (sortBy == null || sortOrder == null) {
            query.append("ORDER BY id ASC");
        } else {
            query.append("ORDER BY ").append(sortBy).append(" ").append(sortOrder);
        }
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemsExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<ItemShort> findAllShort() {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_ITEMS_SHORT)) {
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemsShortFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
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

    public void updateWithNewSupplyData(Double currentAveragePrice, Integer stockUnits, Long itemId) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM_SUPPLY)) {
            statement.setDouble(1, currentAveragePrice);
            statement.setInt(2, stockUnits);
            statement.setLong(3, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWithNewSaleData(Double currentAveragePrice, Integer stockUnits, Long itemId) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM_SALE)) {
            statement.setDouble(1, currentAveragePrice);
            statement.setInt(2, stockUnits);
            statement.setLong(3, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ITEM_BY_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getCategoryStockReport(List<String> selectedFields) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORY_STOCK_REPORT)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String[]> getCategorySaleReport(List<String> selectedFields) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORY_SALE_REPORT)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String[]> getItemSaleReport(List<String> selectedFields) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITEM_SALE_REPORT)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String[]> getItemStockReport(List<String> selectedFields) {
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITEM_STOCK_REPORT)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private Connection getNewConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
