package org.denys.hudymov.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.denys.hudymov.entity.ServicesCategory;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ServicesCategoryDao implements Dao<ServicesCategory> {

    private final String INSERT_SQL =
            "INSERT INTO ServicesCategory VALUES(?,?)";
    private final String UPDATE_SQL =
            "UPDATE ServicesCategory SET description=?\n" +
                    "WHERE category=?";
    private final String DELETE_SQL =
            "DELETE FROM ServicesCategory\n" +
                    "WHERE category=?";

    @Override
    public void create(ServicesCategory entity) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setString(1, entity.getCategory());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public List<ServicesCategory> read() {
        List<ServicesCategory> servicesCategories = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM ServicesCategory");
            while (resultSet.next()) {
                ServicesCategory servicesCategory = ServicesCategory
                        .builder()
                        .category(resultSet.getString("category"))
                        .description(resultSet.getString("description"))
                        .build();
                servicesCategories.add(servicesCategory);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesCategories;
    }

    @Override
    public Optional<ServicesCategory> get(long id) {
        return Optional.empty();
    }


    public Optional<ServicesCategory> get(String id) {

        Optional<ServicesCategory> servicesCategory = Optional.empty();
        String readSql = "SELECT * FROM ServicesCategory " +
                "WHERE category=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                servicesCategory = Optional.ofNullable(ServicesCategory
                        .builder()
                        .category(resultSet.getString("category"))
                        .description(resultSet.getString("description"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesCategory;
    }

    @SneakyThrows
    @Override
    public void update(ServicesCategory entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getCategory());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) throws SQLIntegrityConstraintViolationException {
    }

    public void delete(String entityId) throws SQLIntegrityConstraintViolationException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setString(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        }
    }

    public List<String> getAllId() {
        List<String> servicesId = new ArrayList<>();
        String selectSQL = "SELECT category FROM ServicesCategory " +
                "ORDER BY category";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                servicesId.add(resultSet.getString("category"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesId;
    }

    public List<ServicesCategory> findCategoriesWithoutServices() {
        List<ServicesCategory> servicesCategories = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                    "SELECT sc.Category, sc.description\n" +
                    "FROM ServicesCategory sc\n" +
                    "LEFT JOIN Services s ON sc.Category = s.category\n" +
                    "WHERE s.category IS NULL");
            while (resultSet.next()) {
                ServicesCategory servicesCategory = ServicesCategory
                        .builder()
                        .category(resultSet.getString("category"))
                        .description(resultSet.getString("description"))
                        .build();
                servicesCategories.add(servicesCategory);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesCategories;
    }
}
