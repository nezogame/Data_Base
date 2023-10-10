package org.denys.hudymov.repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void create(T entity);

    void create(List<T> entities);

    List<T> read();

    Optional<List<T>> get(long id);

    void update(T entity);

    void update(List<T> entities);

    void delete(int entityId);
}
