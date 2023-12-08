package org.denys.hudymov.service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Staff;
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

    public void addStaff(String name, String email, Date employmentDate, String salary) throws SQLException {
        STAFF_DAO.create(
                Staff.builder()
                        .name(name)
                        .email(email)
                        .employmentDate(employmentDate)
                        .salary(salary)
                        .build()
        );
    }

    public List<Long> getIdList() {
        return STAFF_DAO.getAllId();
    }

    public void updateStaff(Long id, String name, String email, Date date, String salary) {
        STAFF_DAO.update(
                Staff.builder()
                        .staffId(id)
                        .name(name)
                        .email(email)
                        .employmentDate(date)
                        .salary(salary)
                        .build()
        );
    }

    public Staff getStaffById(Long id) {
        return STAFF_DAO.get(id).get();
    }

    public void deleteStaff(String id) throws SQLIntegrityConstraintViolationException {
        STAFF_DAO.delete(Integer.parseInt(id));
    }
}
