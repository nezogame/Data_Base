package org.denys.hudymov.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class People {
    @Id
    @Column(name = "PASSPORT_DATA")
    private String passportData;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PATRONYMIC")
    private String patronymic;

    @OneToOne(mappedBy = "people")
    private Client client;

    public String getPatronymic() {
        if (patronymic == null) {
            return "─";
        }
        return patronymic;
    }

    public String getPassportData() {
        if (patronymic == null) {
            return "<<FRAUD>>";
        }
        return passportData;
    }
}
