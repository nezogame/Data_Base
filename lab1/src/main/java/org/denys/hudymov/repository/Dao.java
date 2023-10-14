package org.denys.hudymov.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void create(T entity) throws SQLException;

    List<T> read();

    Optional<T> get(long id);

    void update(T entity) throws SQLException;

    void delete(int entityId) throws SQLIntegrityConstraintViolationException;
}
