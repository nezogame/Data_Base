package org.denys.hudymov.lab4.entity;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersGroup {
    Long group_id;
    Long user_id;
    private Set<User> users;
    private Set<Group> groups;
}
