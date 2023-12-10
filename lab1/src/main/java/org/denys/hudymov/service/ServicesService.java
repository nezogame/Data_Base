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
        var serviceVector = new Vector<Vector<Object>>();
        var service = SERVICES_DAO.read();
        service.forEach(s -> {
            Vector<Object> row = new Vector<>();
            row.add(s.getServiceName());
            row.add(s.getPrice() != null ? s.getPrice() + "$" : "NULL");
            row.add(s.getCategory());
            serviceVector.add(row);
        });
        return serviceVector;
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

    public Vector<Vector<Object>> displayServicesWithinCategory() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = SERVICES_DAO.findServicesWithinCategory();
        service.forEach(s -> {
            Vector<Object> row = new Vector<>();
            row.add(s.getCategory());
            row.add(s.getServiceName());
            row.add(s.getPrice()+"$");
            serviceVector.add(row);
        });
        return serviceVector;
    }

    public Vector<Vector<Object>> displayPricesAboveAvg() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = SERVICES_DAO.findPricesAboveAvg();
        service.forEach(s -> {
            Vector<Object> row = new Vector<>();
            row.add(s.getServiceName());
            row.add(s.getPrice()+"$");
            row.add(s.getCategory());
            serviceVector.add(row);
        });
        return serviceVector;
    }

    public Vector<Vector<Object>> displayServicesInEachCategory() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = SERVICES_DAO.findServicesInEachCategory();
        service.forEach((category, serviceCount)  -> {
            Vector<Object> row = new Vector<>();
            row.add(category);
            row.add(serviceCount);
            serviceVector.add(row);
        });
        return serviceVector;
    }
}
