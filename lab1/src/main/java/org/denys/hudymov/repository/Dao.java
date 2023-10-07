package org.denys.hudymov.repository;

import java.util.List;

public interface Dao<T> {
    void create(T entity) ;

    void create(List<T> entities) ;

    List<T> read() ;

    void update(T entity) ;

    void update(List<T> entities) ;

    void delete(int entityId) ;
}
