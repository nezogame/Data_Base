package org.denys.hudymov.service;

import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.denys.hudymov.entity.Staff;
import org.denys.hudymov.repository.StaffRepository;
import org.springframework.stereotype.Service;

@Data
@Builder
@RequiredArgsConstructor
@Service
public class StaffService {
    private final StaffRepository staffRepository;

    public Vector<Vector<Object>> displayStaff() {
        var staffVector = new Vector<Vector<Object>>();
        var staff = staffRepository.findAll();
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
        staffRepository.save(
                Staff.builder()
                        .name(name)
                        .email(email)
                        .employmentDate(employmentDate)
                        .salary(Integer.parseInt(salary))
                        .build()
        );
    }

    public List<Long> getIdList() {
        return staffRepository.findAll()
                .stream()
                .map(s -> s.getStaffId().longValue())
                .toList();
    }

    @Transactional
    public void updateStaff(Long id, String name, String email, Date date, String salary) {
        staffRepository.save(
                Staff.builder()
                        .staffId(BigInteger.valueOf(id))
                        .name(name)
                        .email(email)
                        .employmentDate(date)
                        .salary(Integer.parseInt(salary))
                        .build()
        );
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(BigInteger.valueOf(id)).get();
    }

    public void deleteStaff(String id) throws SQLIntegrityConstraintViolationException {
        staffRepository.deleteById(BigInteger.valueOf(Long.parseLong(id)));
    }

    public Vector<Vector<Object>> staffHiredMonthAgo(Integer month) {
        var staffVector = new Vector<Vector<Object>>();
        var staff = staffRepository.findStaffHiredMonthAgo(month);

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
