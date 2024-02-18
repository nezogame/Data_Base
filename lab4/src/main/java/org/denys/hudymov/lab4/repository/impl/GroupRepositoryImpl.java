package org.denys.hudymov.lab4.repository.impl;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.entity.Group;
import org.denys.hudymov.lab4.repository.GroupRepository;

public class GroupRepositoryImpl implements GroupRepository {
    @Override
    public Group create(Group entity) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<Group> read() {
        return null;
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
