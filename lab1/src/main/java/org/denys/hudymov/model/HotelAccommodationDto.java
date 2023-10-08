package org.denys.hudymov.model;

import java.sql.Timestamp;

public record HotelAccommodationDto(
        Long accommodationId,
        Long client_id,
        Long room_id,
        Timestamp arrivalDate,
        Timestamp departureDate,
        String note
) {
}
