package org.denys.hudymov.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void create(T entity) throws SQLException;

    List<T> read();

    Optional<T> get(long id);

    void update(T entity);

    void delete(int entityId);
}
