package org.denys.hudymov.lab4.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.entity.UserHealth;

public interface UserRepository extends Crud<User, Long> {
    /**
     * Return an Optional of a searched user.
     * If name or password is null throw IllegalArgumentException.
     *
     * @param name     the name of the searched user. Must not be null.
     * @param password the password of the searched user. Must not be null.
     * @throws IllegalArgumentException in case the given name or password is null.
     */
    Optional<User> findUserByNameAndPassword(String name, String password) throws IllegalArgumentException;

    List<UserHealth> userWithPsychoHealth() throws SQLException;
}
