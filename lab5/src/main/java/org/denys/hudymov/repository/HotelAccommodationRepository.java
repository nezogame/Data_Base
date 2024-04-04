package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import org.denys.hudymov.entity.HotelAccommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelAccommodationRepository extends JpaRepository<HotelAccommodation, BigInteger> {
    @Query("SELECT YEAR(ha.departureDate) AS year, MONTH(ha.departureDate) AS month, " +
            "SUM(r.price) AS monthlyRevenue, " +
            "(CASE WHEN LEAD(SUM(r.price)) OVER (ORDER BY YEAR(ha.departureDate), MONTH(ha.departureDate)) = 0 THEN 0.0 " +
            "ELSE (SUM(r.price) / LEAD(SUM(r.price)) " +
            "OVER (ORDER BY year(ha.departureDate), MONTH(ha.departureDate))) * 100.0 END) AS percentGrowth " +
            "FROM HotelAccommodation ha " +
            "JOIN Room r ON ha.room.roomId=r.roomId " +
            "GROUP BY YEAR(ha.departureDate), MONTH(ha.departureDate) " +
            "ORDER BY YEAR(ha.departureDate), MONTH(ha.departureDate)")
    List<Object[]> findMonthlyRevenueWithPercentGrowth();

}
