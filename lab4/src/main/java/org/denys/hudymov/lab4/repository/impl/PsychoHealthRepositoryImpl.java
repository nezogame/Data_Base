package org.denys.hudymov.lab4.repository.impl;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.entity.PsychoHealth;
import org.denys.hudymov.lab4.repository.PsychoHealthRepository;

public class PsychoHealthRepositoryImpl implements PsychoHealthRepository {
    @Override
    public PsychoHealth create(PsychoHealth entity) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<PsychoHealth> read() {
        return null;
    }

    @Override
    public Optional<PsychoHealth> findById(Long id) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public PsychoHealth update(PsychoHealth entity) throws SQLException {

        return entity;
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException {

    }
}
