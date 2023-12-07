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
import org.denys.hudymov.entity.Staff;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StaffDao implements Dao<Staff>{

    private final String INSERT_SQL =
            "INSERT INTO Staff(name,salary,employment_date,email) VALUES(?,?,?,?)";

    private final String UPDATE_SQL=
            "UPDATE Staff SET name=?,salary=?,employment_date=?,email=?" +
                    "WHERE staff_id=?";

    private final String DELETE_SQL = "DELETE FROM Staff " +
            "WHERE staff_id=?";

    @Override
    public void create(Staff entity) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSalary());
            preparedStatement.setTimestamp(3, entity.getEmploymentDate());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public List<Staff> read() {
        List<Staff> servicesCategories = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Staff");
            while (resultSet.next()) {
                Staff services = Staff
                        .builder()
                        .staffId(resultSet.getLong("staff_id"))
                        .name(resultSet.getString("name"))
                        .salary(resultSet.getString("salary"))
                        .employmentDate(resultSet.getTimestamp("employment_date"))
                        .email(resultSet.getString("email"))
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
    public Optional<Staff> get(long id) {
        Optional<Staff> services = Optional.empty();
        String readSql = "SELECT * FROM Staff " +
                "WHERE category=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            services = Optional.ofNullable(Staff
                    .builder()
                    .name(resultSet.getString("name"))
                    .salary(resultSet.getString("salary"))
                    .email(resultSet.getString("employment_date"))
                    .email(resultSet.getString("email"))
                    .build());

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    @SneakyThrows
    @Override
    public void update(Staff entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSalary());
            preparedStatement.setTimestamp(3, entity.getEmploymentDate());
            preparedStatement.setString(4, entity.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) throws SQLIntegrityConstraintViolationException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        }
    }

}
