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
import org.denys.hudymov.entity.ServicesCategory;
import org.denys.hudymov.repository.ServicesCategoryDao;


@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ServicesCategoryService {
    private static final ServicesCategoryDao SERVICES_CATEGORY_DAO = ServicesCategoryDao.builder().build();

    public Vector<Vector<Object>> displayCategory() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = SERVICES_CATEGORY_DAO.read();
        clients.forEach(category -> {
            Vector<Object> row = new Vector<>();
            row.add(category.getCategory());
            row.add(category.getDescription());
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public List<String> getIdList() {
        return SERVICES_CATEGORY_DAO.getAllId();
    }

    public void addCategory(String category, String description) throws SQLException {
        SERVICES_CATEGORY_DAO.create(
                ServicesCategory.builder()
                        .category(category)
                        .description(description)
                        .build()
        );
    }

    public void updateCategory(String category, String description) {
        SERVICES_CATEGORY_DAO.update(
                ServicesCategory.builder()
                        .category(category)
                        .description(description)
                        .build()
        );
    }

    public void deleteCategory(String category) throws SQLIntegrityConstraintViolationException {
        SERVICES_CATEGORY_DAO.delete(category);
    }

    public ServicesCategory getCategoryById(String id) {
        return SERVICES_CATEGORY_DAO.get(id).get();
    }
}
