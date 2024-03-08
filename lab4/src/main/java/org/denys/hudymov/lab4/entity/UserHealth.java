package org.denys.hudymov.lab4.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHealth {
    private Long id;
    private String name;
    private String lastName;
    private String characteristic;
}
