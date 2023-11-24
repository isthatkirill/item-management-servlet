package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
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
public class SupplyRepository {

    public Long create(Supply supply) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_SUPPLY, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_AVERAGE_SUPPLY_PRICE)) {
            statement.setLong(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getDouble("average_price");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0D;
    }

    public List<SupplyExtended> findAllExtended() {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SUPPLIES_EXTENDED)) {
            ResultSet resultSet = statement.executeQuery();
            return SupplyMapper.extractSuppliesExtendedFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
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

    public Optional<Supply> findById(Long id) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SUPPLY_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return SupplyMapper.extractSupplyFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
