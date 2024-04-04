package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.denys.hudymov.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository  extends JpaRepository<Client, BigInteger> {
    @Query("SELECT c.clientId FROM Client c")
    List<Long> findAllId();

    Optional<Client> findClientByPeoplePassportData(String passport);

    @Query("SELECT c.people.passportData, c.people.name, c.people.surname, c.people.patronymic, " +
            "DAY(accom.departureDate ) - DAY(accom.arrivalDate ) AS daysSpent  " +
            "FROM Client c " +
            "INNER JOIN HotelAccommodation accom ON c.clientId=accom.client.clientId" +
            " WHERE c.people.passportData = ?1")
    List<Object[]> findClientHotelStayByPassportData(String passportData);

    @Query("SELECT c.people.surname, c.people.name, c.people.patronymic, c.people.passportData, c.userComment " +
            "FROM Client c " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM HotelAccommodation a " +
            "    WHERE a.client.clientId = c.clientId " +
            "    AND a.departureDate > CURRENT_TIMESTAMP" +
            "    AND a.arrivalDate < CURRENT_TIMESTAMP" +
            ")")
    List<Object[]> findCurrentlyStayingClients();

    @Query("SELECT c.people.passportData, c.people.surname, c.people.name, c.people.patronymic, " +
            "AVG (DAY(a.departureDate)) " +
            "FROM Client c " +
            "LEFT JOIN HotelAccommodation a ON c.clientId = a.client.clientId " +
            "GROUP BY c.people.passportData, c.people.surname, c.people.name, c.people.patronymic " +
            "ORDER BY c.people.passportData")
    List<Object[]> findAverageStayDurationByClient();

    @Query("SELECT c.people.passportData, c.people.surname, c.people.name, c.people.patronymic, " +
            "COUNT(a) AS activeAccommodations " +
            "FROM Client c " +
            "JOIN HotelAccommodation a ON c.clientId = a.client.clientId " +
            "WHERE a.arrivalDate >= FUNCTION('add_months', CURRENT_TIMESTAMP, -12) " +  // Minus 12 months
            "GROUP BY c.people.passportData, c.clientId, c.people.surname, c.people.name, c.people.patronymic")
    List<Object[]> findActiveAccommodationsByClientInLastYear();

    @Query("SELECT c.people.passportData, c.people.surname,  c.people.name,  c.people.patronymic, c.userComment " +
            "FROM Client c " +
            "WHERE ( " +
            "    SELECT COUNT(*) " +
            "    FROM HotelAccommodation a " +
            "    WHERE a.client.clientId = c.clientId " +
            ") >= ?1")
    List<Object[]> findClientsByNumberOfHotelStays(Integer minAccommodationCount);

/*    @Query(value = "INSERT INTO Clients(passport_data, user_comment) VALUES(?1,?2)",
        nativeQuery = true)
    void saveClient(String passport, String userComment);*/
}
