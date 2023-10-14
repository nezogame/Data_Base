package org.denys.hudymov.controller;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Client;
import org.denys.hudymov.repository.ClientDao;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Vector;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClientController {
    private static final ClientDao CLIENT_DAO = ClientDao.builder().build();
    public  Vector<Vector<Object>> displayClients(){
        var clientsVector = new Vector<Vector<Object>>();
        var clients = CLIENT_DAO.read();
        clients.forEach(client -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getClientId());
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(client.getPassportData());
            row.add(client.getComment());
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public void addClient(String name, String surname, String patronymic,
                          String passport, String comment) throws SQLException {
        CLIENT_DAO.create(
                Client.builder()
                        .surname(surname)
                        .name(name)
                        .patronymic(patronymic)
                        .passportData(passport)
                        .comment(comment)
                        .build()
        );
    }

    public List<Long> getId() {
        return CLIENT_DAO.getAllId();
    }

    public List<String> getPassportCodes() {
        return CLIENT_DAO.getAllPassportCode();
    }

    public Client getClientByPassport(String passport) {
        if (passport.isEmpty()){
            passport=null;
        }
        return CLIENT_DAO.getByPassport(passport).get();
    }

    public void updateClient(Long id, String surname, String name, String patronymic,
                             String passport, String comment) throws IllegalArgumentException {

        CLIENT_DAO.update(
                Client.builder()
                        .clientId(id)
                        .surname(surname)
                        .name(name)
                        .patronymic(patronymic)
                        .passportData(passport)
                        .comment(comment)
                        .build()
        );
    }

    public void deleteClient(String id) throws SQLIntegrityConstraintViolationException {
        CLIENT_DAO.delete(Integer.parseInt(id));
    }
}
