package org.denys.hudymov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Room {
    private Long roomId;
    private String roomNumber;
    private Integer seatsNumber;
    private String comfort;
    private String price;
    private Boolean occupied;
}
