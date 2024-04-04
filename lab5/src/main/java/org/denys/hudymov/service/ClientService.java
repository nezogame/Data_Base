package org.denys.hudymov.service;


import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.denys.hudymov.entity.Client;
import org.denys.hudymov.entity.People;
import org.denys.hudymov.repository.ClientRepository;
import org.denys.hudymov.repository.PeopleRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Builder
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PeopleRepository peopleRepository;

    public Vector<Vector<Object>> displayClients() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = clientRepository.findAll();

        for (int i = 0; i < clients.size(); i++) {
            Vector<Object> row = new Vector<>();
            row.add(clients.get(i).getClientId());
            row.add(clients.get(i).getPeople().getSurname());
            row.add(clients.get(i).getPeople().getName());
            row.add(clients.get(i).getPeople().getPatronymic());
            row.add(clients.get(i).getPeople().getPassportData());
            row.add(clients.get(i).getUserComment());
            clientsVector.add(row);
        }
        return clientsVector;
    }

    @Transactional
    public void addClient(String name, String surname, String patronymic,
                          String passport, String comment) throws SQLException {

        var person = People.builder()
                .surname(surname)
                .name(name)
                .patronymic(patronymic)
                .passportData(passport)
                .build();
        clientRepository.save(Client.builder()
                .people(person)
                .userComment(comment).build());
    }

    public List<Long> getId() {
        return clientRepository.findAllId();
    }

    public List<String> getPassportCodes() {
        return peopleRepository.findAllPassportCode();
    }

    public Client getClientByPassport(String passport) {
        if (passport.isEmpty()) {
            passport = null;
        }
        return clientRepository.findClientByPeoplePassportData(passport).get();
    }

    @Transactional
    public void updateClient(Long id, String surname, String name, String patronymic,
                             String passport, String comment) throws IllegalArgumentException {

        var person = People.builder()
                .surname(surname)
                .name(name)
                .patronymic(patronymic)
                .passportData(passport)
                .build();
        clientRepository.save(
                Client.builder()
                        .clientId(BigInteger.valueOf(id))
                        .people(person)
                        .userComment(comment)
                        .build()
        );
    }

    public void deleteClient(String id) throws SQLIntegrityConstraintViolationException {
        clientRepository.deleteById(BigInteger.valueOf(Long.parseLong(id)));
    }

    public Vector<Vector<Object>> getClientHistory(String passportCode) {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = clientRepository.findClientHotelStayByPassportData(passportCode);

        for (var c : clients) {
            Vector<Object> row = new Vector<>();
            row.add(c[0]);
            row.add(c[1]);
            row.add(c[2]);
            row.add(c[3]);
            row.add(c[4]);
            clientsVector.add(row);
        }

        return clientsVector;
    }

    public Vector<Vector<Object>> displayGuests() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = clientRepository.findCurrentlyStayingClients();

        for (var c : clients) {
            Vector<Object> row = new Vector<>();
            row.add(c[0]);
            row.add(c[1]);
            row.add(c[2]);
            row.add(c[3]);
            row.add(c[4]);
            clientsVector.add(row);
        }

        return clientsVector;
    }

    public Vector<Vector<Object>> displayAvgStayDuration() {
        var clientsVector = new Vector<Vector<Object>>();
        var clientsStayDuration = clientRepository.findAverageStayDurationByClient();

        for (var c : clientsStayDuration) {
            Vector<Object> row = new Vector<>();
            row.add(c[0]);
            row.add(c[1]);
            row.add(c[2]);
            row.add(c[3]);
            row.add(c[4]);
            clientsVector.add(row);
        }

        return clientsVector;
    }

    public Vector<Vector<Object>> displayClientsPlacementForLastYear() {
        var clientsVector = new Vector<Vector<Object>>();
        var clientsForLastYear = clientRepository.findActiveAccommodationsByClientInLastYear();

        for (var c : clientsForLastYear) {
            Vector<Object> row = new Vector<>();
            row.add(c[0]);
            row.add(c[1]);
            row.add(c[2]);
            row.add(c[3]);
            row.add(c[4]);
            clientsVector.add(row);
        }
        return clientsVector;
    }

    public Vector<Vector<Object>> displayClientsByNumberOfHotelStays(Integer minAccommodation) {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = clientRepository.findClientsByNumberOfHotelStays(minAccommodation);

        for (var c : clients) {
            Vector<Object> row = new Vector<>();
            row.add(c[0]);
            row.add(c[1]);
            row.add(c[2]);
            row.add(c[3]);
            row.add(c[4]);
            clientsVector.add(row);
        }
        return clientsVector;
    }

}
