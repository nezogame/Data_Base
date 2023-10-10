package org.denys.hudymov.repository;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.denys.hudymov.entity.Room;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoomDao implements Dao<Room>{
    @Override
    public void create(Room entity) {

    }

    @Override
    public void create(List<Room> entities) {

    }

    @Override
    public List<Room> read() {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM rooms");
            while (resultSet.next()) {
                Room room = Room
                        .builder()
                        .roomId(resultSet.getLong("room_id"))
                        .roomNumber(resultSet.getString("room_number"))
                        .seatsNumber(resultSet.getInt("seats_number"))
                        .comfort(resultSet.getString("comfort"))
                        .price(resultSet.getString("price"))
                        .occupied(resultSet.getBoolean("occupied"))
                        .build();
                rooms.add(room);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public Optional<List<Room>> get(long id) {
        return Optional.empty();
    }

    @Override
    public void update(Room entity) {

    }

    @Override
    public void update(List<Room> entities) {

    }

    @Override
    public void delete(int entityId) {

    }
}
