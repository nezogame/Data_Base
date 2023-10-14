package org.denys.hudymov.controller;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.HotelAccommodation;
import org.denys.hudymov.repository.ClientDao;
import org.denys.hudymov.repository.HotelAccommodationDao;
import org.denys.hudymov.repository.RoomDao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class HotelAccommodationController {
    private static final HotelAccommodationDao HOTEL_ACCOMMODATION_DAO = HotelAccommodationDao.builder().build();
    private static final ClientDao CLIENT_DAO = ClientDao.builder().build();
    private static final RoomDao ROOM_DAO = RoomDao.builder().build();

    public Vector<Vector<Object>> displayAccommodationForAllTime() {
        var accommodationVector = new Vector<Vector<Object>>();
        var hotelAccommodations = HOTEL_ACCOMMODATION_DAO.read();
        hotelAccommodations.forEach(accommodation -> {
            Vector<Object> row = new Vector<>();
            row.add(accommodation.getAccommodationId());
            row.add(accommodation.getClientId());
            row.add(accommodation.getRoomId());
            row.add(accommodation.getArrivalDate());
            row.add(accommodation.getDepartureDate());
            row.add(accommodation.getNote());
            accommodationVector.add(row);
        });
        return accommodationVector;
    }

    public List<Long> getListOfId() {
        return HOTEL_ACCOMMODATION_DAO.getAllId();
    }

    public void addReservation(String clientPassport, String roomNumber, Timestamp arrival, Timestamp depart, String note) throws SQLException, NoSuchElementException {
        var clientId = CLIENT_DAO.getByPassport(clientPassport);
        var roomId = ROOM_DAO.getByRoomNumber(roomNumber);

        HOTEL_ACCOMMODATION_DAO.create(
                HotelAccommodation.builder()
                        .clientId(clientId.orElseThrow().getClientId())
                        .roomId(roomId.orElseThrow().getRoomId())
                        .arrivalDate(arrival)
                        .departureDate(depart)
                        .note(note)
                        .build()
        );
    }

    public void deleteReservation(String id) {
        HOTEL_ACCOMMODATION_DAO.delete(Integer.parseInt(id));
    }

    public void updateReservation(Long id, Long clientId, Long roomId, String arrival, String depart, String note) {

        HOTEL_ACCOMMODATION_DAO.update(
                HotelAccommodation.builder()
                        .accommodationId(id)
                        .clientId(clientId)
                        .roomId(roomId)
                        .arrivalDate(Timestamp.valueOf(arrival))
                        .departureDate(Timestamp.valueOf(depart))
                        .note(note)
                        .build()
        );
    }

    public HotelAccommodation getById(Long id) throws IllegalArgumentException {
        var accommodationEntity = HOTEL_ACCOMMODATION_DAO.get(id);
        if (accommodationEntity.isEmpty()) {
            throw new IllegalArgumentException("This id " + id + " doesn't exist in the table!");
        }
        return accommodationEntity.get();
    }
}
