package org.denys.hudymov.lab4.repository;

import java.util.Optional;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.enums.Role;

public interface UserRepository extends Crud<User, Long> {
    /**
     * Return an Optional role of a searched user.
     * If name or password is null throw IllegalArgumentException.
     *
     * @param name     the name of the searched user. Must not be null.
     * @param password the password of the searched user. Must not be null.
     * @throws IllegalArgumentException in case the given name or password is null.
     */
    Optional<Role> findUserRoleByNameAndPassword(String name, String password) throws IllegalArgumentException;
}
