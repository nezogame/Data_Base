package org.denys.hudymov.lab4.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import org.denys.hudymov.lab4.connection.DataSource;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.entity.UserHealth;
import org.denys.hudymov.lab4.enums.Role;
import org.denys.hudymov.lab4.repository.UserRepository;
import org.denys.hudymov.lab4.utilities.FxUtilities;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User create(User entity) throws IllegalArgumentException, SQLException, SQLIntegrityConstraintViolationException {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        String insertQuery = "INSERT INTO users(name, last_name, password, role) VALUES(?,?,?,?)";

        String selectQuery = "SELECT id FROM users WITH(HOLDLOCK) WHERE password = ?";

        var message = FxUtilities.displayInformationAboutTransaction(
                "Please wait until another transaction will execute",
                Alert.AlertType.INFORMATION
        );
        message.show();

        var connection = DataSource.getConnection();
        var commit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getRole().toString());
            preparedStatement.executeUpdate();

            selectStatement.setString(1, entity.getPassword());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    entity.setId(id);
                }
            }

        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(commit);
            connection.close();
            throw new SQLIntegrityConstraintViolationException("This password already exist!");
        } finally {
            message.close();
        }
        connection.commit();
        connection.setAutoCommit(commit);
        connection.close();

        return entity;
    }

    @Override
    public List<User> read() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WITH(HOLDLOCK)");
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .lastName(resultSet.getString("last_name"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build();
                users.add(user);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) throws IllegalArgumentException {
        String readSql = "SELECT * FROM users WITH(HOLDLOCK) " +
                "WHERE id=?";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        Optional<User> user = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = Optional.ofNullable(User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .lastName(resultSet.getString("last_name"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User entity) throws SQLException, IllegalArgumentException, SQLWarning {
        String lockQuery = "SELECT * FROM users With(UPDLOCK) " +
                "WHERE id=?";
        String updateQuery = "UPDATE users set name=?, last_name=?, password=?, role=? " +
                "WHERE id=?";

        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        var message = FxUtilities.displayInformationAboutTransaction(
                "Please wait until another transaction will execute",
                Alert.AlertType.INFORMATION
        );
        message.show();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
             PreparedStatement lockStatement = connection.prepareStatement(lockQuery)) {

            lockStatement.setLong(1, entity.getId());

            ResultSet resultSet = lockStatement.executeQuery();
            if (resultSet.next()) {

                var user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .lastName(resultSet.getString("last_name"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build();

                if(!FxUtilities.getOptimisticLockHash().equals(user.hashCode())){
                    throw new SQLWarning("This row already updated by another user try one more time after checking");
                }
            }

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getRole().toString());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.executeUpdate();
        } finally {
            message.close();
        }

        return entity;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {
        String deleteQuery = "DELETE FROM users " +
                "WHERE id=?";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        var message = FxUtilities.displayInformationAboutTransaction(
                "Please wait until another transaction will execute",
                Alert.AlertType.INFORMATION
        );
        message.show();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        } finally {
            message.close();
        }
    }


    @Override
    public Optional<User> findUserByNameAndPassword(String name, String password) throws IllegalArgumentException {
        String readSql = "SELECT * FROM users WITH(HOLDLOCK) " +
                "WHERE name=? and password=?";

        if (name == null || password == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        Optional<User> user = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = Optional.ofNullable(User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .lastName(resultSet.getString("last_name"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<UserHealth> userWithPsychoHealth() throws SQLException {
        String readSql = "SELECT us.id, us.name, us.last_name, p.characteristic " +
                "FROM users us " +
                "INNER JOIN psycho_health p " +
                "ON us.id = p.user_id";

        List<UserHealth> users = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(readSql);

            while (resultSet.next()) {
                UserHealth userHealth = UserHealth.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .lastName(resultSet.getString("last_name"))
                        .characteristic(resultSet.getString("characteristic"))
                        .build();
                users.add(userHealth);
            }
            resultSet.close();
        }
        return users;
    }
}
