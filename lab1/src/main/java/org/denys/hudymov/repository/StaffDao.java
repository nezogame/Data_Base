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
public class StaffDao implements Dao<Staff> {

    private final String INSERT_SQL =
            "INSERT INTO Staff(name,salary,employment_date,email) VALUES(?,?,?,?)";

    private final String UPDATE_SQL =
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
            preparedStatement.setDate(3, entity.getEmploymentDate());
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
                        .employmentDate(resultSet.getDate("employment_date"))
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
                "WHERE staff_id=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                services = Optional.ofNullable(Staff
                        .builder()
                        .staffId(resultSet.getLong("staff_id"))
                        .name(resultSet.getString("name"))
                        .salary(resultSet.getString("salary"))
                        .employmentDate(resultSet.getDate("employment_date"))
                        .email(resultSet.getString("email"))
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
    public void update(Staff entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSalary());
            preparedStatement.setDate(3, entity.getEmploymentDate());
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

    public List<Long> getAllId() {
        List<Long> staffId = new ArrayList<>();
        String selectSQL = "SELECT staff_id FROM Staff " +
                "ORDER BY staff_id";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                staffId.add(resultSet.getLong("staff_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffId;
    }

    /**
     *
     * @param month it is amount of month that will be subtracted from current time
     * @return Hired staff last N month (N is month parameter)
     */
    public List<Staff> staffHiredMonthAgo(Integer month) {
        List<Staff> hiredStaff = new ArrayList<>();
        String staffHiredMonthAgo =
                "SELECT *\n" +
                "FROM Staff\n" +
                "WHERE employment_date >= add_months(TRUNC(SYSDATE, 'MONTH'), -?)\n" +
                "AND employment_date < TRUNC(SYSDATE, 'MONTH')";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(staffHiredMonthAgo)) {

            preparedStatement.setInt(1, month);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hiredStaff.add(
                        Staff.builder()
                                .staffId(resultSet.getLong("staff_id"))
                                .name(resultSet.getString("name"))
                                .salary(resultSet.getString("salary"))
                                .employmentDate(resultSet.getDate("employment_date"))
                                .email(resultSet.getString("email"))
                                .build()
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hiredStaff;
    }
}
