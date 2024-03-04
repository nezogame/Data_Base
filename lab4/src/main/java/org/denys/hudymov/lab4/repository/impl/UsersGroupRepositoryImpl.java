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
import javafx.scene.control.Alert;
import org.denys.hudymov.lab4.connection.DataSource;
import org.denys.hudymov.lab4.entity.UsersGroup;
import org.denys.hudymov.lab4.repository.UsersGroupRepository;
import org.denys.hudymov.lab4.utilities.FxUtilities;

public class UsersGroupRepositoryImpl implements UsersGroupRepository {
    @Override
    public UsersGroup create(UsersGroup entity) throws SQLException, IllegalArgumentException,
            SQLIntegrityConstraintViolationException {

        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        String insertQuery = "INSERT INTO users_group(group_id, user_id) VALUES(?,?)";

        var message = FxUtilities.displayInformationAboutTransaction(
                "Please wait until another transaction will execute",
                Alert.AlertType.INFORMATION
        );
        message.show();

        var connection = DataSource.getConnection();
        var commit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setLong(1, entity.getGroupId());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e){
            connection.rollback();
            connection.setAutoCommit(commit);
            connection.close();
            throw new SQLIntegrityConstraintViolationException("This user already in this group!");
        }
        catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(commit);
            connection.close();
            throw new SQLException(e.getMessage());
        } finally {
            message.close();
        }
        connection.commit();
        connection.setAutoCommit(commit);
        connection.close();

        return entity;
    }

    @Override
    public List<UsersGroup> read() {
        List<UsersGroup> workGroups = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users_group WITH(HOLDLOCK)");
            while (resultSet.next()) {
                UsersGroup workGroup = UsersGroup.builder()
                        .groupId(resultSet.getLong("group_id"))
                        .userId(resultSet.getLong("user_id"))
                        .build();
                workGroups.add(workGroup);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workGroups;
    }

    @Override
    public Optional<UsersGroup> findById(Long id) throws IllegalArgumentException {
        String readSql = "SELECT * FROM users_group WITH(HOLDLOCK) " +
                "WHERE group_id=?";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        Optional<UsersGroup> workGroup = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                 workGroup = Optional.ofNullable(UsersGroup.builder()
                         .groupId(resultSet.getLong("group_id"))
                         .userId(resultSet.getLong("user_id"))
                         .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workGroup;
    }

    @Override
    public UsersGroup update(UsersGroup entity) throws SQLException, IllegalArgumentException {
        String updateQuery = "UPDATE users_group set group_id=?, user_id=? " +
                "WHERE group_id=?";

        if (entity == null) {
            throw new IllegalArgumentException("User Group can't be null");
        }

        var message = FxUtilities.displayInformationAboutTransaction(
                "Please wait until another transaction will execute",
                Alert.AlertType.INFORMATION
        );
        message.show();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {


            preparedStatement.setLong(1, entity.getGroupId());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.executeUpdate();
        } finally {
            message.close();
        }

        return entity;
    }

    @Override
    public void deleteById(Long aLong) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {
        throw new UnsupportedOperationException("Feature incomplete. Contact assistance.");
    }

    @Override
    public void deleteById(Long id, Long userId) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {
        String deleteQuery = "DELETE FROM users_group " +
                "WHERE group_id=? and user_id=?";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID can't be null");
        }

        var message = FxUtilities.displayInformationAboutTransaction(
                "Please wait until another transaction will execute",
                Alert.AlertType.INFORMATION
        );
        message.show();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        } finally {
            message.close();
        }
    }
}
