package org.denys.hudymov.repository;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Room;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoomDao implements Dao<Room> {

    private final String INSERT_SQL =
            "INSERT INTO Rooms " +
                    "VALUES(?,?,?,?,?)";
    private final String UPDATE_SQL = "Update Rooms " +
            "SET room_number=?, seats_number=?, comfort=?, price=?, occupied=? " +
            "WHERE room_id=?";

    private final String DELETE_SQL = "DELETE FROM  Rooms " +
            "WHERE room_id=?";

    @Override
    public void create(Room entity) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            setParameters(preparedStatement, entity);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
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
    public Optional<Room> get(long id) {
        return Optional.empty();
    }

    @Override
    public void update(Room entity) {

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(UPDATE_SQL)) {

            setParameters(preparedStatement, entity);
            preparedStatement.setLong(6, entity.getRoomId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) throws SQLIntegrityConstraintViolationException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        }
    }

    private void setParameters(@NotNull PreparedStatement preparedStatement, @NotNull Room entity) throws SQLException {
        preparedStatement.setString(1, entity.getRoomNumber());
        preparedStatement.setInt(2, entity.getSeatsNumber());
        preparedStatement.setString(3, entity.getComfort());
        preparedStatement.setString(4, entity.getPrice());
        preparedStatement.setBoolean(5, entity.getOccupied());
    }

    public Optional<Room> getByRoomNumber(String number) {
        Optional<Room> room = Optional.empty();
        try (Connection connection = DataSource.getConnection()) {
            String SelectQuery = "SELECT * FROM Rooms WHERE room_number=?";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(SelectQuery);
            preparedStatement.setString(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Check if there is a result

                room = Optional.of(Room
                        .builder()
                        .roomId(resultSet.getLong("room_id"))
                        .roomNumber(resultSet.getString("room_number"))
                        .seatsNumber(resultSet.getInt("seats_number"))
                        .comfort(resultSet.getString("comfort"))
                        .price(resultSet.getString("price"))
                        .occupied(resultSet.getBoolean("occupied"))
                        .build());
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public List<Long> getAllId() {
        List<Long> roomsId = new ArrayList<>();
        String selectSQL = "SELECT room_id FROM rooms " +
                "ORDER BY room_id";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                roomsId.add(resultSet.getLong("room_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomsId;
    }

    public List<String> getAllRoomNumber() {
        List<String> roomsNumber = new ArrayList<>();
        String selectSQL = "SELECT room_number FROM rooms " +
                "ORDER BY room_number";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                roomsNumber.add(resultSet.getString("room_number"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomsNumber;
    }

    public List<String> getAllFreeRoomNumber() {
        List<String> roomsId = new ArrayList<>();
        String selectSQL = "SELECT room_number FROM rooms " +
                "WHERE occupied=0 " +
                "ORDER BY room_number";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                roomsId.add(resultSet.getString("room_number"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomsId;
    }

    public void updateRoomOccupancy(String roomNumber) {
        List<Long> roomsId = new ArrayList<>();
        String updateSQL = "Update Rooms " +
                "Set occupied=1 " +
                "WHERE room_number=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1,roomNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
