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
import org.denys.hudymov.lab4.entity.PsychoHealth;
import org.denys.hudymov.lab4.repository.PsychoHealthRepository;
import org.denys.hudymov.lab4.utilities.FxUtilities;

public class PsychoHealthRepositoryImpl implements PsychoHealthRepository {
    @Override
    public PsychoHealth create(PsychoHealth entity) throws SQLException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        String insertQuery = "INSERT INTO psycho_health(characteristic, user_id) VALUES(?,?)";

        String selectQuery = "SELECT id FROM psycho_health WITH(HOLDLOCK) WHERE user_id = ?";

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

            preparedStatement.setString(1, entity.getCharacteristic());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.executeUpdate();

            selectStatement.setLong(1, entity.getUserId());

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
            throw new SQLIntegrityConstraintViolationException("This user ID already exist!");
        } finally {
            message.close();
        }
        connection.commit();
        connection.setAutoCommit(commit);
        connection.close();

        return entity;
    }

    @Override
    public List<PsychoHealth> read() {
        List<PsychoHealth> psychoHealths = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM psycho_health WITH(HOLDLOCK)");
            while (resultSet.next()) {
                PsychoHealth psychoHealth = PsychoHealth.builder()
                        .id(resultSet.getLong("id"))
                        .characteristic(resultSet.getString("characteristic"))
                        .userId(resultSet.getLong("user_id"))
                        .build();
                psychoHealths.add(psychoHealth);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return psychoHealths;
    }

    @Override
    public Optional<PsychoHealth> findById(Long id) throws IllegalArgumentException {
        String readSql = "SELECT * FROM psycho_health WITH(HOLDLOCK) " +
                "WHERE id=?";

        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        Optional<PsychoHealth> psychoHealth = Optional.empty();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                psychoHealth = Optional.ofNullable(PsychoHealth.builder()
                        .id(resultSet.getLong("id"))
                        .characteristic(resultSet.getString("characteristic"))
                        .userId(resultSet.getLong("user_id"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return psychoHealth;
    }

    @Override
    public PsychoHealth update(PsychoHealth entity) throws SQLException {
        String lockQuery = "SELECT * FROM psycho_health With(UPDLOCK) " +
                "WHERE id=?";

        String updateQuery = "UPDATE psycho_health set characteristic=?, user_id=? " +
                "WHERE id=?";

        if (entity == null) {
            throw new IllegalArgumentException("Psycho Health can't be null");
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

                var psychoHealth = PsychoHealth.builder()
                        .id(resultSet.getLong("id"))
                        .characteristic(resultSet.getString("characteristic"))
                        .userId(resultSet.getLong("user_id"))
                        .build();

                if (!FxUtilities.getOptimisticLockHash().equals(psychoHealth.hashCode())) {
                    throw new SQLWarning("This row already updated by another user try one more time after checking");
                }
            }

            preparedStatement.setString(1, entity.getCharacteristic());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } finally {
            message.close();
        }

        return entity;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {
        String deleteQuery = "DELETE FROM psycho_health " +
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
}
