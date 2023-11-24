package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.SaleMapper;
import isthatkirill.itemmanagement.model.sale.Sale;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static isthatkirill.itemmanagement.util.Constants.*;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class SaleRepository {

    public Long create(Sale sale) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_SALE, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_AVERAGE_SALE_PRICE)) {
            statement.setLong(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getDouble("average_price");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0D;
    }

    public List<SaleExtended> findAllExtended() {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SALES_EXTENDED)) {
            ResultSet resultSet = statement.executeQuery();
            return SaleMapper.extractSalesExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Optional<Sale> findById(Long id) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SALE_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return SaleMapper.extractSaleFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void update(Sale sale) {
        StringBuilder query = new StringBuilder("UPDATE sales SET ");
        List<Object> values = new ArrayList<>();

        if (sale.getPrice() != null) {
            query.append("price = ?, ");
            values.add(sale.getPrice());
        }

        if (sale.getAmount() != null) {
            query.append("amount = ?, ");
            values.add(sale.getAmount());
        }

        if (values.isEmpty()) {
            return;
        }

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        values.add(sale.getId());

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
