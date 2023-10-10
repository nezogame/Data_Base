package org.denys.hudymov.model;

import lombok.Builder;

import java.sql.Timestamp;


@Builder
public record HotelAccommodationDto(
        Long accommodationId,
        Long clientId,
        Long roomId,
        Timestamp arrivalDate,
        Timestamp departureDate,
        String note
) {
}
