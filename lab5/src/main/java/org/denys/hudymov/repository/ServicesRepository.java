package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.util.List;
import org.denys.hudymov.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Services, String> {

    @Query(value = "SELECT sc.Category, s.service_name, s.price\n" +
            "FROM Services s\n" +
            "JOIN ServicesCategory sc ON s.category = sc.Category\n" +
            "ORDER BY sc.Category, s.price desc",
    nativeQuery = true)
    List<Services> findServicesWithinCategory();

    @Query("SELECT s " +
            "FROM Services s " +
            "WHERE s.price > (SELECT AVG(se.price) FROM Services se)")
    List<Services> findServicesAboveAveragePrice();

    @Query(value = "SELECT sc.Category, COUNT(*) AS ServiceCount\n" +
            "FROM Services s\n" +
            "JOIN ServicesCategory sc ON s.category = sc.Category\n" +
            "GROUP BY sc.Category",
    nativeQuery = true)
    List<Object[]> findServicesInEachCategory();
}
