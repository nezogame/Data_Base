package org.denys.hudymov.model;

public record RoomDto(
        Long roomId,
        String roomNumber,
        String comfort,
        String price,
        Boolean occupied
) {
}
