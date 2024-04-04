package org.denys.hudymov.service;

import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.denys.hudymov.entity.Services;
import org.denys.hudymov.entity.ServicesCategory;
import org.denys.hudymov.repository.ClientRepository;
import org.denys.hudymov.repository.HotelAccommodationRepository;
import org.denys.hudymov.repository.RoomRepository;
import org.denys.hudymov.repository.ServicesRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Data
@Builder
@RequiredArgsConstructor
@Service
public class ServicesService {

    private final ServicesRepository servicesRepository;

    public Vector<Vector<Object>> displayServices() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = servicesRepository.findAll();
        service.forEach(s -> {
            Vector<Object> row = new Vector<>();
            row.add(s.getServiceName());
            row.add(s.getPrice() != null ? s.getPrice() + "$" : "NULL");
            row.add(s.getServicesCategory().getCategory());
            serviceVector.add(row);
        });
        return serviceVector;
    }

    public List<String> getIdList() {
        return servicesRepository.findAll()
                .stream()
                .map(Services::getServiceName)
                .toList();
    }

    public void addService(String service, String price, String category) throws SQLException {
        servicesRepository.save(
                Services.builder()
                        .serviceName(service)
                        .price(Integer.parseInt(price))
                        .servicesCategory(ServicesCategory.builder()
                                .category(category)
                                .build())
                        .build()
        );
    }

    @Transactional
    public void updateService(String service, String price, String category) {
        servicesRepository.save(
                Services.builder()
                        .serviceName(service)
                        .price(Integer.parseInt(price))
                        .servicesCategory(ServicesCategory.builder()
                                .category(category)
                                .build())
                        .build()
        );
    }

    public void deleteService(String id) throws SQLIntegrityConstraintViolationException {
        servicesRepository.deleteById(id);
    }

    public Services getServiceById(String id) {
        return servicesRepository.findById(id).get();
    }

    public Vector<Vector<Object>> displayServicesWithinCategory() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = servicesRepository.findServicesWithinCategory();
        service.forEach(s -> {
            Vector<Object> row = new Vector<>();
            row.add(s.getServicesCategory().getCategory());
            row.add(s.getServiceName());
            row.add(s.getPrice() + "$");
            serviceVector.add(row);
        });
        return serviceVector;
    }

    public Vector<Vector<Object>> displayPricesAboveAvg() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = servicesRepository.findServicesAboveAveragePrice();
        service.forEach(s -> {
            Vector<Object> row = new Vector<>();
            row.add(s.getServiceName());
            row.add(s.getPrice() + "$");
            row.add(s.getServicesCategory().getCategory());
            serviceVector.add(row);
        });
        return serviceVector;
    }

    public Vector<Vector<Object>> displayServicesInEachCategory() {
        var serviceVector = new Vector<Vector<Object>>();
        var service = servicesRepository.findServicesInEachCategory();

        for (var s : service) {
            Vector<Object> row = new Vector<>();
            row.add(s[0]);
            row.add(s[1]);
            serviceVector.add(row);
        }

        return serviceVector;
    }
}
