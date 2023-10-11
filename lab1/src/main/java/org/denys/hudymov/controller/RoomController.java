package org.denys.hudymov.controller;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Room;
import org.denys.hudymov.repository.ClientDao;
import org.denys.hudymov.repository.RoomDao;

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoomController {
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
}
