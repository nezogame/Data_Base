package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, BigInteger> {
    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findAllByOccupiedFalseOrderByRoomNumber();

    @Modifying
    @Query(value = "Update Rooms " +
            "Set occupied=1 " +
            "WHERE room_number=?",
    nativeQuery = true)
    void updateRoomOccupancy(String roomNumber);

    @Query(value = "SELECT DISTINCT room_p.room_number, room_p.comfort, room_p.price, popularity, " +
            "DENSE_RANK() OVER (ORDER BY popularity desc) AS rank " +
            "FROM ( " +
            "  SELECT r.room_number, r.comfort, r.price, COUNT(r.room_number) " +
            "    OVER (PARTITION BY r.room_number) AS popularity " +
            "  FROM Rooms r " +
            "  INNER JOIN HotelAccommodations accom ON r.room_id = accom.room_id " +
            ") room_p " +
            "ORDER BY rank",
    nativeQuery = true)
    List<Object[]> findPopularRoomsWithRank();

    @Query(value = "SELECT DISTINCT r.room_number, r.seats_number, r.comfort, r.price " +
            "FROM rooms r  " +
            "LEFT JOIN HotelAccommodations a " +
            "ON r.room_id = a.room_id " +
            "WHERE r.seats_number >= ?1 AND r.price <= ?2 " +
            " AND (a.departure_date<?3+?4 OR a.arrival_date>?4) " +
            "ORDER BY r.room_number",
    nativeQuery = true)
    List<Object[]> findAvailableRoom(Integer seatsNumber, String price,Integer days, Timestamp arrival );

    @Query("SELECT r.comfort, AVG(r.price) AS avgPrice " +  // Assuming you prefer 'avgPrice' alias
            "FROM Room r " +
            "GROUP BY r.comfort")
    List<Object[]> findAveragePricePerComfortLevel();

    @Query(value = "SELECT r.room_number, r.seats_number, r.comfort, r.price, " +
            "   a.arrival_date, a.departure_date " +
            "FROM HotelAccommodations a " +
            "JOIN Rooms r ON a.room_id = r.room_id " +
            "WHERE a.arrival_date >= ?1 AND a.departure_date <= ?2",
    nativeQuery = true)
    List<Object[]> findAccommodationsByDateRange(Date start, Date end);

    @Query(value = "SELECT r.room_number, r.comfort, r.price, SUM(r.price) AS room_income " +
            "FROM Rooms r " +
            "INNER JOIN HotelAccommodations a ON r.room_id = a.room_id " +
            "WHERE a.arrival_date >= ?1 AND a.departure_date <= ?2 " +
            "GROUP BY r.room_number, r.comfort, r.price " +
            "ORDER BY r.room_number",
    nativeQuery = true)
    List<Object[]> findRoomsIncomeInDateRange(Date start, Date end);

    @Query(value = "SELECT r.room_number, r.comfort, r.price, COUNT(*) AS booking_count " +
            "        FROM Rooms r " +
            "        INNER JOIN HotelAccommodations a ON r.room_id = a.room_id " +
            "        WHERE EXTRACT(YEAR FROM a.departure_date) = ?1 " +
            "GROUP BY r.room_number, r.comfort, r.price",
            nativeQuery = true)
    List<Object[]> findRoomsComfortsAndNumberOfStayed(Integer year);
}
