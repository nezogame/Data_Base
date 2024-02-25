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
        return Optional.empty();
    }

    @Override
    public Group update(Group entity) throws SQLException {
        return null;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {

    }

    @Override
    public Optional<Group> findGroupByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Group> findGroupByNameAndWorkerQuantity(String name) {
        return Optional.empty();
    }

    @Override
    public List<Group> findIncompleteGroups() {
        return null;
    }

    @Override
    public List<Group> findCompleteGroups() {
        return null;
    }
}
