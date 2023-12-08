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
import org.denys.hudymov.entity.Services;
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

    public List<String> getIdList() {
        return SERVICES_DAO.getAllId();
    }

    public void addService(String service, String price, String category) throws SQLException {
        SERVICES_DAO.create(
                Services.builder()
                        .serviceName(service)
                        .price(price)
                        .category(category)
                        .build()
        );
    }

    public void updateService(String service, String price, String category) {
        SERVICES_DAO.update(
                Services.builder()
                        .serviceName(service)
                        .price(price)
                        .category(category)
                        .build()
        );
    }

    public void deleteService(String id) throws SQLIntegrityConstraintViolationException {
        SERVICES_DAO.delete(id);
    }

    public Services getServiceById(String id) {
        return SERVICES_DAO.get(id).get();
    }
}
