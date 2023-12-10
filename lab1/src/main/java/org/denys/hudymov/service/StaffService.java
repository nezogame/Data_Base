package org.denys.hudymov.service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
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
        var staffVector = new Vector<Vector<Object>>();
        var staff = STAFF_DAO.read();
        staff.forEach(emp -> {
            Vector<Object> row = new Vector<>();
            row.add(emp.getStaffId());
            row.add(emp.getName());
            row.add(emp.getEmail());
            row.add(emp.getSalary() != null ? emp.getSalary() + "$" : "NULL");
            row.add(emp.getEmploymentDate());
            staffVector.add(row);
        });
        return staffVector;
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

    public Vector<Vector<Object>> staffHiredMonthAgo(Integer month) {
        var staffVector = new Vector<Vector<Object>>();
        var staff = STAFF_DAO.staffHiredMonthAgo(month);
        staff.forEach(emp -> {
            Vector<Object> row = new Vector<>();
            row.add(emp.getStaffId());
            row.add(emp.getName());
            row.add(emp.getSalary() != null ? emp.getSalary() + "$" : "NULL");
            row.add(emp.getEmploymentDate());
            row.add(emp.getEmail());
            staffVector.add(row);
        });
        return staffVector;
    }
}
