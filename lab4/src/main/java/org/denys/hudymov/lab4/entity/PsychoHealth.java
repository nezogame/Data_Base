package org.denys.hudymov.lab4.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsychoHealth {
    private Long id;
    private String characteristic;
    private Long userId;
}
