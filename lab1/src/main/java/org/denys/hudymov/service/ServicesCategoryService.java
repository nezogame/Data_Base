package org.denys.hudymov.service;

import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
}
