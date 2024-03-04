package org.denys.hudymov.lab4.repository;

import java.sql.SQLIntegrityConstraintViolationException;
import org.denys.hudymov.lab4.entity.UsersGroup;

public interface UsersGroupRepository extends Crud<UsersGroup,Long> {
    void deleteById(Long id, Long userId) throws IllegalArgumentException, SQLIntegrityConstraintViolationException;
}
