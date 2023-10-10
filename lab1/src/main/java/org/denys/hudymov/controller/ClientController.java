package org.denys.hudymov.controller;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.repository.ClientDao;

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
}
