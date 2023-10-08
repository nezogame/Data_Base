package org.denys.hudymov.model;

public record ClientDto(
        Long clientId,
        String surname,
        String name,
        String patronymic,
        String passportData,
        String Comment
) {
}
