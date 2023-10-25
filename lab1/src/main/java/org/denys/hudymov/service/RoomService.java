package org.denys.hudymov.service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Room;
import org.denys.hudymov.repository.RoomDao;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoomService {
    private static final RoomDao ROOM_DAO = RoomDao.builder().build();

    public Vector<Vector<Object>> displayRooms() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = ROOM_DAO.read();
        clients.forEach(room -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomId());
            row.add(room.getRoomNumber());
            row.add(room.getSeatsNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() != null ? room.getPrice() + "$" : "NULL");
            row.add(room.getOccupied());
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public void addRoom(String roomNumber, Integer seatsNumber, String comfort, String price) throws SQLException {
        ROOM_DAO.create(
                Room.builder()
                        .roomNumber(roomNumber)
                        .seatsNumber(seatsNumber)
                        .comfort(comfort)
                        .price(price)
                        .occupied(false)
                        .build()
        );
    }

    public Room getRoomByNumber(String number) {
        return ROOM_DAO.getByRoomNumber(number).get();
    }

    public void updateRoom(Long id, String roomNumber, Integer seatsNumber, String comfort, String price,
                           Boolean occupied) {

        ROOM_DAO.update(
                Room.builder()
                        .roomId(id)
                        .roomNumber(roomNumber)
                        .seatsNumber(seatsNumber)
                        .comfort(comfort)
                        .price(price)
                        .occupied(occupied)
                        .build()
        );
    }

    public List<Long> getId() {
        return ROOM_DAO.getAllId();
    }

    public List<String> getRoomNumbers() {
        return ROOM_DAO.getAllRoomNumber();
    }

    public void deleteRoom(String id) throws SQLIntegrityConstraintViolationException {
        ROOM_DAO.delete(Integer.parseInt(id));
    }

    public List<String> getFreeRooms() {
        return ROOM_DAO.getAllFreeRoomNumber();
    }

    public void updateReservation(String roomNumber) {
        ROOM_DAO.updateRoomOccupancy(roomNumber);
    }

    public Vector<Vector<Object>> computePopularity() {
        var popularRooms = new Vector<Vector<Object>>();
        var rooms = ROOM_DAO.computePopularity();
        rooms.forEach((room, pair) -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() + "$");
            row.add(pair.getKey());
            row.add(pair.getValue());


            popularRooms.add(row);
        });
        return popularRooms;
    }

    public Vector<Vector<Object>> findSuitableRooms(Integer seats_number, String price,
                                                    Timestamp arrival, Integer days) {

        var suitableRooms = new Vector<Vector<Object>>();
        var rooms = ROOM_DAO.findAvailable(seats_number, price, arrival, days);
        rooms.forEach((room) -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomNumber());
            row.add(room.getSeatsNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() + "$");
            suitableRooms.add(row);
        });
        return suitableRooms;
    }
}
