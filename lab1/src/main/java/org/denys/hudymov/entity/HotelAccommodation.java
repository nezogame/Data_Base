package org.denys.hudymov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class HotelAccommodation {
    private Long accommodationId;
    private Long client_id;
    private Long room_id;
    private Timestamp arrivalDate;
    private Timestamp departureDate;
    private String note;
}
