package org.denys.hudymov.lab4.repository;

import java.util.List;
import java.util.Optional;
import org.denys.hudymov.lab4.entity.Group;

public interface GroupRepository extends Crud<Group, Long> {
    Optional<Group> findGroupByName(String name);

    Optional<Group> findGroupByNameAndWorkerQuantity(String name);

    List<Group> findIncompleteGroups();

    List<Group> findCompleteGroups();
}
