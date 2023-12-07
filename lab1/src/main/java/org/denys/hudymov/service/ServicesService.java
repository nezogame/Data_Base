package org.denys.hudymov.service;

import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.repository.ServicesDao;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ServicesService {
    private static final ServicesDao SERVICES_DAO = ServicesDao.builder().build();

    public Vector<Vector<Object>> displayServices() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = SERVICES_DAO.read();
        clients.forEach(service -> {
            Vector<Object> row = new Vector<>();
            row.add(service.getServiceName());
            row.add(service.getPrice() != null ? service.getPrice() + "$" : "NULL");
            row.add(service.getCategory());
            clientsVector.add(row);
        });
        return clientsVector;
    }
}
