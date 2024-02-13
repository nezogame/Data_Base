package org.denys.hudymov.lab4.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.connection.DataSource;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.enums.Role;
import org.denys.hudymov.lab4.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User create(User entity) throws SQLException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        String insertQuery = "INSERT INTO users(name, last_name, password, role) VALUES(?,?,?,?)";

        String selectQuery = "SELECT id FROM users WITH(HOLDLOCK) WHERE password = ?";

        var connection = DataSource.getConnection();
        var commit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getRole().toString());
            preparedStatement.executeUpdate();

            selectStatement.setString(1, entity.getPassword());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    entity.setId(id);
                }
            }

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
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        Optional<User> user = Optional.empty();
        String readSql = "SELECT * FROM users WITH(HOLDLOCK) " +
                "WHERE client_id=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = Optional.ofNullable(User.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .lastName(resultSet.getString("last_name"))
                    .role(Role.valueOf(resultSet.getString("role")))
                    .build());

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User entity) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        String updateQuery = "UPDATE users set name=?, last_name=?, password=?, role=? " +
                "WHERE id=?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getRole().toString());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.executeUpdate();
        }

        return entity;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        String deleteQuery = "DELETE FROM users " +
                "WHERE id=?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        }
    }


    @Override
    public Optional<Role> findUserRoleByNameAndPassword(String name, String password) throws IllegalArgumentException {
        if (name == null || password == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        Optional<Role> role = Optional.empty();
        String readSql = "SELECT role FROM users WITH(HOLDLOCK) " +
                "WHERE name=? and password=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = Optional.of(Role.valueOf(resultSet.getString("role")));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}
