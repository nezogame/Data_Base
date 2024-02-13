package org.denys.hudymov.lab4.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.denys.hudymov.lab4.enums.Role;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    @NotEmpty(message = "Name cannot be empty")
    private Long id;
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;
    @NotEmpty(message = "password cannot be empty")
    private String password;
    @NotNull(message = "User must have role")
    private Role role;
    //One-to-one relationship
    private PsychoHealth psychoHealths;

}

