package org.denys.hudymov.lab4.repository.impl;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.entity.Log;
import org.denys.hudymov.lab4.repository.LogRepository;

public class LogRepositoryImpl implements LogRepository {
    @Override
    public Log create(Log entity) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<Log> read() {
        return null;
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
