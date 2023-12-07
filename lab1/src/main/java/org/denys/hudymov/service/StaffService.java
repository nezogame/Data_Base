package org.denys.hudymov.service;

import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.repository.StaffDao;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StaffService {
    private static final StaffDao STAFF_DAO = StaffDao.builder().build();

    public Vector<Vector<Object>> displayStaff() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = STAFF_DAO.read();
        clients.forEach(emp -> {
            Vector<Object> row = new Vector<>();
            row.add(emp.getStaffId());
            row.add(emp.getName());
            row.add(emp.getEmail());
            row.add(emp.getSalary() != null ? emp.getSalary() + "$" : "NULL");
            row.add(emp.getEmploymentDate());
            clientsVector.add(row);
        });
        return clientsVector;
    }
}
