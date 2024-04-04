package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.util.List;
import org.denys.hudymov.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, BigInteger> {
    @Query(value = "SELECT *\n" +
            "FROM Staff\n" +
            "WHERE employment_date >= add_months(TRUNC(SYSDATE, 'MONTH'), -?)\n" +
            "AND employment_date < TRUNC(SYSDATE, 'MONTH')",
    nativeQuery = true)
    List<Staff> findStaffHiredMonthAgo(Integer month);
}
