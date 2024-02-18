package org.denys.hudymov.lab4.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.connection.DataSource;
import org.denys.hudymov.lab4.entity.Log;
import org.denys.hudymov.lab4.repository.LogRepository;

public class LogRepositoryImpl implements LogRepository {
    @Override
    public Log create(Log entity) throws SQLException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("Log can't be null");
        }

        String insertQuery = "INSERT INTO logs(date, action, user_id) VALUES(?,?,?)";

        String selectQuery = "SELECT id FROM logs WITH(HOLDLOCK) WHERE date= ? ";

        var connection = DataSource.getConnection();
        var commit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getDateTime().toLocalDateTime()));
            preparedStatement.setString(2, entity.getAction());
            preparedStatement.setLong(3, entity.getUserId());
            preparedStatement.executeUpdate();

            selectStatement.setTimestamp(1, Timestamp.valueOf(entity.getDateTime().toLocalDateTime()));

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
    public List<Log> read() {
        List<Log> logs = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM logs WITH(HOLDLOCK)");
            while (resultSet.next()) {
                Log log = Log.builder()
                        .id(resultSet.getLong("id"))
                        .dateTime(ZonedDateTime.from(resultSet.getTimestamp("date").toLocalDateTime()))
                        .action(resultSet.getString("action"))
                        .userId(resultSet.getLong("user_id"))
                        .build();
                logs.add(log);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public Optional<Log> findById(Long id) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Log update(Log entity) throws SQLException {

        return entity;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {

    }
}
