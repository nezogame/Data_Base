package org.denys.hudymov.lab4.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.entity.Group;

public interface GroupRepository extends Crud<Group, Long> {
    Optional<Group> findGroupByName(String name);

    Optional<Group> findGroupByNameAndWorkerQuantity(String name, Integer size);

    List<Group> findIncompleteGroups() throws SQLException;

    Boolean isGroupNotCompleted(Long id);
}
