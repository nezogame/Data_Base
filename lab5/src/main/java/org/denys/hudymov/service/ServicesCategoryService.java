package org.denys.hudymov.service;

import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.denys.hudymov.entity.ServicesCategory;
import org.denys.hudymov.repository.ServicesCategoryRepository;
import org.denys.hudymov.repository.ServicesRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Data
@Builder
@RequiredArgsConstructor
@Component
public class ServicesCategoryService {

    private final ServicesCategoryRepository servicesCategoryRepository;

/*    public ServicesCategoryService() {
        this.context = new AnnotationConfigApplicationContext();
        this.servicesCategoryRepository = context.getBean(ServicesCategoryRepository.class);
    }*/

    public Vector<Vector<Object>> displayCategory() {
        var categoriesVector = new Vector<Vector<Object>>();
        var categories = servicesCategoryRepository.findAll();
        categories.forEach(category -> {
            Vector<Object> row = new Vector<>();
            row.add(category.getCategory());
            row.add(category.getDescription());
            categoriesVector.add(row);
        });
        return categoriesVector;
    }

    public List<String> getIdList() {
        return servicesCategoryRepository.findAll()
                .stream()
                .map(ServicesCategory::getCategory)
                .toList();
    }

    public void addCategory(String category, String description) throws SQLException {
        servicesCategoryRepository.save(
                ServicesCategory.builder()
                        .category(category)
                        .description(description)
                        .build()
        );
    }

    @Transactional
    public void updateCategory(String category, String description) {
        servicesCategoryRepository.save(
                ServicesCategory.builder()
                        .category(category)
                        .description(description)
                        .build()
        );
    }

    public void deleteCategory(String category) throws SQLIntegrityConstraintViolationException {
        servicesCategoryRepository.deleteById(category);
    }

    public ServicesCategory getCategoryById(String id) {
        return servicesCategoryRepository.findById(id).get();
    }

    public Vector<Vector<Object>> displayCategoriesWithoutServices() {
        var categoriesVector = new Vector<Vector<Object>>();
        var categories = servicesCategoryRepository.findAllByServicesCategoryEmpty();
        categories.forEach(category -> {
            Vector<Object> row = new Vector<>();
            row.add(category.getCategory());
            row.add(category.getDescription());
            categoriesVector.add(row);
        });
        return categoriesVector;
    }
}
