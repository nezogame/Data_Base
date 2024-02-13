package org.denys.hudymov.lab4.entity;

import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    private Long id;
    private ZonedDateTime dateTime;
    private String action;
    private Long userId;
    private Set<User> users;
}
