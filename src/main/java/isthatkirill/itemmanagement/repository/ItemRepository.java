package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.mapper.ReportDataMapper;
import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Optional<ItemExtended> findById(Long id) {
        String query = "SELECT i.*, c.name as category_name FROM items i LEFT JOIN categories c ON i.category_id = c.id WHERE i.id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return ItemMapper.extractItemExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Long findStockById(Long id) {
        String query = "SELECT stock_units FROM items WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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
        String query = "SELECT 1 FROM items WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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
            query.append( "ORDER BY ").append(sortBy).append(" ").append(sortOrder);
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
        String query = "SELECT id, name FROM items ORDER BY id ASC";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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
        String query = "UPDATE items SET purchase_price = ?, stock_units = stock_units + ? WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, currentAveragePrice);
            statement.setInt(2, stockUnits);
            statement.setLong(3, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWithNewSaleData(Double currentAveragePrice, Integer stockUnits, Long itemId) {
        String query = "UPDATE items SET sale_price = ?, stock_units = stock_units - ? WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, currentAveragePrice);
            statement.setInt(2, stockUnits);
            statement.setLong(3, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        String query = "DELETE FROM items WHERE id = ?";
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getCategoryStockReport(List<String> selectedFields) {
        String query = """
                SELECT
                    c.id,
                    c.name,
                    c.description,
                    COUNT(i.id) as items_in_category,
                    SUM(i.stock_units) as stock_units,
                    CAST(SUM(i.stock_units * i.purchase_price) AS DECIMAL(13, 2)) as stock_price,
                    COUNT(s.item_id) as supplies_count,
                    MAX(s.last_supply_date) as last_supply_date,
                    (SELECT
                        CONCAT(i_inner.name, ' (id = ', i_inner.id, ' stock = ', i_inner.stock_units, ')')
                        FROM items i_inner
                        WHERE i_inner.category_id = c.id
                        ORDER BY i_inner.stock_units DESC LIMIT 1) as most_units_item,\s
                    (SELECT
                        CONCAT(i_inner.name, ' (id = ', i_inner.id, ' stock = ', i_inner.stock_units, ')')
                        FROM items i_inner
                        WHERE i_inner.category_id = c.id
                        ORDER BY i_inner.stock_units ASC LIMIT 1) as less_units_item,\s
                    (SELECT
                        CONCAT(i_inner.name, ' (id = ', i_inner.id, ' price = ', CAST(i_inner.purchase_price AS DECIMAL(13, 2)), ')')
                        FROM items i_inner
                        WHERE i_inner.category_id = c.id
                        ORDER BY i_inner.purchase_price DESC LIMIT 1) as most_expensive_item,
                    (SELECT
                        CONCAT(i_inner.name, ' (id = ', i_inner.id, ' price = ', CAST(i_inner.purchase_price AS DECIMAL(13, 2)), ')')
                        FROM items i_inner
                        WHERE i_inner.category_id = c.id
                        ORDER BY i_inner.purchase_price ASC LIMIT 1) as most_cheap_item
                FROM categories c
                LEFT JOIN items i ON c.id = i.category_id
                LEFT JOIN (
                    SELECT
                        s.item_id,
                        COUNT(s.id) as supplies_count,
                        MAX(s.created_at) as last_supply_date
                    FROM supplies s
                    GROUP BY s.item_id
                ) s ON i.id = s.item_id
                GROUP BY c.id, c.name, c.description
                ORDER BY c.id;
                """;

        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String[]> getCategorySaleReport(List<String> selectedFields) {
        String query = """
        SELECT
            c.id,
            c.name,
            c.description,
            SUM(sup.supply_price) AS supply_price,
            SUM(sal.sale_price) AS sale_price,
            SUM(sal.sale_price - sup.supply_price) AS profit,
            CAST(SUM(sal.sale_price - sup.supply_price) / SUM(sup.supply_price) * 100 AS DECIMAL(13, 2)) AS profit_percentage,
            SUM(sal.sold) AS sold,
            MAX(sal.last_sale_date) AS last_sale_date,
            SUM(sal.sales_count) AS sales_count
        FROM categories c
        LEFT JOIN items i ON c.id = i.category_id
        LEFT JOIN (
            SELECT
                item_id,
                CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS supply_price
            FROM supplies
            GROUP BY item_id
        ) sup ON i.id = sup.item_id
        LEFT JOIN (
            SELECT
                item_id,
                CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS sale_price,
                SUM(amount) AS sold,
                MAX(created_at) AS last_sale_date,
                COUNT(sales) AS sales_count
            FROM sales
            GROUP BY item_id
        ) sal ON i.id = sal.item_id
        GROUP BY c.id, c.name, c.description
        ORDER BY c.id;
                """;

        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String[]> getItemSaleReport(List<String> selectedFields) {
        String query = """
        SELECT
            i.id,
            i.name,
            i.description,
            i.brand,
            sup.supply_price,
            sal.sale_price,
            sal.sale_price - sup.supply_price AS profit,
            CAST((sal.sale_price - sup.supply_price) / sup.supply_price * 100 AS DECIMAL(13, 2)) as profit_percentage,
            sal.sold,
            sal.last_sale_date,
            sal.sales_count,
            sal.most_big_sale_ttl_price
        FROM items i
        LEFT JOIN ( SELECT item_id, CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS supply_price
                    FROM supplies GROUP BY item_id) sup ON i.id = sup.item_id
        LEFT JOIN ( SELECT item_id, CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS sale_price,
                    SUM(amount) as sold, MAX(created_at) as last_sale_date, COUNT(sales) as sales_count,
                    CAST(MAX(amount * price) AS DECIMAL(13, 2)) as most_big_sale_ttl_price\s
                    FROM sales GROUP BY item_id) sal ON i.id = sal.item_id
        ORDER BY i.id      
                """;
        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return ReportDataMapper.extractRows(selectedFields, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String[]> getItemStockReport(List<String> selectedFields) {
        String query = """
        SELECT
             i.id,
             i.name,
             i.description,
             i.brand,
             i.stock_units,
             CAST(i.purchase_price AS DECIMAL(10, 2)),\s
             CAST((i.stock_units * i.purchase_price) AS DECIMAL(10, 2)) AS stock_price,
             c.name as category_name,
             s.last_supply_date,
             s.supplies_count
         FROM items i
         LEFT JOIN categories c ON c.id = i.category_id
         LEFT JOIN ( SELECT item_id, MAX(created_at) as last_supply_date,
                    COUNT(id) as supplies_count FROM supplies GROUP BY
                     item_id) s ON i.id = s.item_id
         ORDER BY i.id
                """;

        try (Connection connection = getNewConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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
