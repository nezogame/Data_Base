package org.denys.hudymov.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.denys.hudymov.entity.Services;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ServicesDao implements Dao<Services>{

    private final String INSERT_SQL =
            "INSERT INTO Services VALUES(?,?,?)";

    private final String UPDATE_SQL=
            "UPDATE Services SET price=?,category=?\n" +
                    "WHERE service_name=?";

    private final String DELETE_SQL = "DELETE FROM Services " +
            "WHERE service_name=?";

    @Override
    public void create(Services entity) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setString(1, entity.getServiceName());
            preparedStatement.setString(2, entity.getPrice());
            preparedStatement.setString(3, entity.getCategory());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public List<Services> read() {
        List<Services> servicesCategories = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Services");
            while (resultSet.next()) {
                Services services = Services
                        .builder()
                        .serviceName(resultSet.getString("service_name"))
                        .category(resultSet.getString("category"))
                        .price(resultSet.getString("price"))
                        .build();
                servicesCategories.add(services);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesCategories;
    }

    @Override
    public Optional<Services> get(long id) { return Optional.empty();}

    public Optional<Services> get(String id) {
        Optional<Services> services = Optional.empty();
        String readSql = "SELECT * FROM Services " +
                "WHERE service_name=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                services = Optional.ofNullable(Services
                        .builder()
                        .serviceName(resultSet.getString("service_name"))
                        .price(resultSet.getString("price"))
                        .category(resultSet.getString("category"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    @SneakyThrows
    @Override
    public void update(Services entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, entity.getServiceName());
            preparedStatement.setString(2, entity.getPrice());
            preparedStatement.setString(3, entity.getCategory());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) throws SQLIntegrityConstraintViolationException {}

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
        String selectSQL = "SELECT service_name FROM Services " +
                "ORDER BY service_name";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                servicesId.add(resultSet.getString("service_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesId;
    }

    public List<Services> findServicesWithinCategory() {
        List<Services> servicesCategories = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT sc.Category, s.service_name, s.price\n" +
                    "FROM Services s\n" +
                    "JOIN ServicesCategory sc ON s.category = sc.Category\n" +
                    "ORDER BY sc.Category, s.price desc");
            while (resultSet.next()) {
                Services services = Services
                        .builder()
                        .serviceName(resultSet.getString("service_name"))
                        .category(resultSet.getString("category"))
                        .price(resultSet.getString("price"))
                        .build();
                servicesCategories.add(services);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesCategories;
    }

    public List<Services> findPricesAboveAvg() {
        List<Services> servicesCategories = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                    "SELECT *\n" +
                    "FROM Services\n" +
                    "WHERE price > (SELECT AVG(price) FROM Services)");
            while (resultSet.next()) {
                Services services = Services
                        .builder()
                        .serviceName(resultSet.getString("service_name"))
                        .category(resultSet.getString("category"))
                        .price(resultSet.getString("price"))
                        .build();
                servicesCategories.add(services);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesCategories;
    }

    public Map<String,Float> findServicesInEachCategory() {
        Map<String,Float> servicesInEachCategory = new LinkedHashMap<>();
        String selectServicesInEachCategory = "SELECT sc.Category, COUNT(*) AS ServiceCount\n" +
                "FROM Services s\n" +
                "JOIN ServicesCategory sc ON s.category = sc.Category\n" +
                "GROUP BY sc.Category";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectServicesInEachCategory);

            while (resultSet.next()) {
                servicesInEachCategory.put(
                        resultSet.getString("category"),
                        resultSet.getFloat("ServiceCount")
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicesInEachCategory;
    }
}
