package org.denys.hudymov.service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Client;
import org.denys.hudymov.repository.ClientDao;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClientService {
    private static final ClientDao CLIENT_DAO = ClientDao.builder().build();

    public Vector<Vector<Object>> displayClients() {
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
        if (passport.isEmpty()) {
            passport = null;
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

    public Vector<Vector<Object>> getClientHistory(String passportCode) {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = CLIENT_DAO.getUsersAndDayTheySpendByPassportCode(passportCode);
        clients.forEach((days, client) -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getPassportData());
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(days);
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public Vector<Vector<Object>> displayGuests() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = CLIENT_DAO.displayGuestsInTheHotel();
        clients.forEach(client -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(client.getPassportData());
            row.add(client.getComment());
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public Vector<Vector<Object>> displayAvgStayDuration() {
        var clientsVector = new Vector<Vector<Object>>();
        var clientsStayDuration = CLIENT_DAO.avgDaysAtHotel();
        clientsStayDuration.forEach((client, days) -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getPassportData());
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(days);
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public Vector<Vector<Object>> displayClientsPlacementForLastYear() {
        var clientsVector = new Vector<Vector<Object>>();
        var clientsForLastYear = CLIENT_DAO.findClientsPlacementForLastYear();
        clientsForLastYear.forEach((client, activeAccommodation) -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getPassportData());
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(activeAccommodation);
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public Vector<Vector<Object>> displayClientsByNumberOfHotelStays(Integer minAccommodation) {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = CLIENT_DAO.findClientsByNumberOfHotelStays(minAccommodation);
        clients.forEach((client) -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getPassportData());
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(client.getComment());
            clientsVector.add(row);
        });
        return clientsVector;
    }

}
