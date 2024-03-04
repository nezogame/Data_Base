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
import javax.validation.ConstraintViolationException;
import org.denys.hudymov.lab4.connection.DataSource;
import org.denys.hudymov.lab4.entity.Group;
import org.denys.hudymov.lab4.repository.GroupRepository;
import org.denys.hudymov.lab4.utilities.FxUtilities;

public class GroupRepositoryImpl implements GroupRepository {
    @Override
    public Group create(Group entity) throws SQLException, IllegalArgumentException, ConstraintViolationException {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        String insertQuery = "INSERT INTO groups(name, size) VALUES(?,?)";

        String selectQuery = "SELECT id FROM groups WITH(HOLDLOCK) WHERE name = ?";

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
            preparedStatement.setInt(2, entity.getSize());
            preparedStatement.executeUpdate();

            selectStatement.setString(1, entity.getName());

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
            throw new SQLIntegrityConstraintViolationException("This name already exist!");
        } finally {
            message.close();
        }
        connection.commit();
        connection.setAutoCommit(commit);
        connection.close();

        return entity;
    }

    @Override
    public List<Group> read() {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM groups WITH(HOLDLOCK)");
            while (resultSet.next()) {
                Group group = Group.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .size(resultSet.getInt("size"))
                        .build();
                groups.add(group);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public Optional<Group> findById(Long id) throws IllegalArgumentException {
        String readSql = "SELECT * FROM groups WITH(HOLDLOCK) " +
                "WHERE id=?";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        Optional<Group> group = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                group = Optional.ofNullable(Group.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .size(resultSet.getInt("size"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public Group update(Group entity) throws SQLException {
        String lockQuery = "SELECT * FROM groups With(UPDLOCK) " +
                "WHERE id=?";

        String updateQuery = "UPDATE groups set name=?, size=? " +
                "WHERE id=?";

        if (entity == null) {
            throw new IllegalArgumentException("Group can't be null");
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

                var group = Group.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .size(resultSet.getInt("size"))
                        .build();

                if (!FxUtilities.getOptimisticLockHash().equals(group.hashCode())) {
                    throw new SQLWarning("This row already updated by another user try one more time after checking");
                }
            }

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getSize());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } finally {
            message.close();
        }

        return entity;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {
        String deleteQuery = "DELETE FROM groups " +
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
    public Optional<Group> findGroupByName(String name) {
        String readSql = "SELECT * FROM groups WITH(HOLDLOCK) " +
                "WHERE name=?";

        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }

        Optional<Group> group = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                group = Optional.ofNullable(Group.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .size(resultSet.getInt("size"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public Optional<Group> findGroupByNameAndWorkerQuantity(String name, Integer size) {
        String readSql = "SELECT * FROM groups WITH(HOLDLOCK) " +
                "WHERE name=? and size=?";

        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        if (size == null) {
            throw new IllegalArgumentException("Size can't be null");
        }

        Optional<Group> group = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, size);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                group = Optional.ofNullable(Group.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .size(resultSet.getInt("size"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public List<Group> findIncompleteGroups() throws SQLException {
        String readSql = "SELECT g.* FROM groups g WITH (HOLDLOCK) \n" +
                "LEFT JOIN users_group u \n" +
                "ON g.id = u.group_id \n" +
                "GROUP BY g.id, g.name, g.size \n" +
                "HAVING COUNT(u.group_id) < g.size;";

        List<Group> groups = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(readSql);
            while (resultSet.next()) {
                Group group = Group.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .size(resultSet.getInt("size"))
                        .build();
                groups.add(group);
            }
            resultSet.close();
        }
        return groups;
    }

    public Boolean isGroupNotCompleted(Long id) {
        String readSql = "SELECT g.id FROM groups g WITH (HOLDLOCK) \n" +
                "LEFT JOIN users_group u \n" +
                "ON g.id = u.group_id \n" +
                "WHERE g.id = ? \n" +
                "GROUP BY g.id, g.size \n" +
                "HAVING COUNT(u.group_id) < g.size;";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
