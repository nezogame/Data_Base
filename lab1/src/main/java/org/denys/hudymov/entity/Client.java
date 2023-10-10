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
public class Client {
    private Long clientId;
    private String surname;
    private String name;
    private String patronymic;
    private String passportData;
    private String Comment;

    public String getPatronymic() {
        if(patronymic==null){
            return "─";
        }
        return patronymic;
    }

    public String getPassportData() {
        if(patronymic==null){
            return "<<FRAUD>>";
        }
        return passportData;
    }
}
