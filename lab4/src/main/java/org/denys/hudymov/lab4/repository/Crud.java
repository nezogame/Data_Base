package org.denys.hudymov.lab4.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public interface Crud<T, ID> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have
     * changed the entity instance completely.
     *
     * @param entity must not be null.
     * @return the saved entity; will never be null.
     * @throws SQLException             in case of any error in sql query
     * @throws IllegalArgumentException in case the given entity is null.
     */
    T create(T entity) throws SQLException, IllegalArgumentException;

    /**
     * Returns all instances of the type.
     *
     * @return all entities.
     */
    List<T> read();

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be null.
     * @return the entity with the given id or Optional.empty() if none found.
     * @throws IllegalArgumentException if id is null.
     */
    Optional<T> findById(ID id) throws IllegalArgumentException;

    /**
     * update a given entity. Use the returned instance for further operations as the update operation might have
     * changed the entity instance completely.
     *
     * @param entity must not be null.
     * @return the updated entity; will never be null.
     * @throws SQLException             in case of any error in sql query
     * @throws IllegalArgumentException in case the given entity is null.
     */
    T update(T entity) throws SQLException;

    /**
     * Deletes the entity with the given id.
     * If the entity is not found in the persistence store it is silently ignored.
     *
     * @param id must not be null.
     * @throws IllegalArgumentException                 in case the given id is null.
     * @throws SQLIntegrityConstraintViolationException in case of constraint violation.
     */
    void deleteById(ID id) throws IllegalArgumentException, SQLIntegrityConstraintViolationException;


}
