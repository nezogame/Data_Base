package org.denys.hudymov.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.util.Pair;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.HotelAccommodation;
import org.denys.hudymov.entity.Room;
import org.jetbrains.annotations.NotNull;

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

            preparedStatement.setString(1, roomNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Room, Pair<Integer, Integer>> computePopularity() {
        Map<Room, Pair<Integer, Integer>> roomsPopularity = new LinkedHashMap<>();
        String selectPopularity = "SELECT DISTINCT room_p.room_number, room_p.comfort, room_p.price, popularity, " +
                "DENSE_RANK() OVER (ORDER BY popularity desc) AS rank " +
                "FROM ( " +
                "  SELECT r.room_number, r.comfort, r.price, COUNT(r.room_number) " +
                "    OVER (PARTITION BY r.room_number) AS popularity " +
                "  FROM Rooms r " +
                "  INNER JOIN HotelAccommodations accom ON r.room_id = accom.room_id " +
                ") room_p " +
                "ORDER BY rank";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectPopularity)) {

            while (resultSet.next()) {
                roomsPopularity.put(Room
                                .builder()
                                .roomNumber(resultSet.getString("room_number"))
                                .comfort(resultSet.getString("comfort"))
                                .price(resultSet.getString("price"))
                                .build(),
                        new Pair(resultSet.getString("popularity"),
                                resultSet.getString("rank")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomsPopularity;
    }

    public List<Room> findAvailable(Integer seats_number, String price, Timestamp arrival_date, Integer days) {
        List<Room> suitableRooms = new ArrayList<>();
        String selectPopularity = "SELECT DISTINCT r.room_number, r.seats_number, r.comfort, r.price " +
                "FROM rooms r  " +
                "LEFT JOIN HotelAccommodations a " +
                "ON r.room_id = a.room_id " +
                "WHERE r.seats_number >= ? AND r.price <= ? " +
                " AND (a.departure_date<DATEADD(day,?,?) OR a.arrival_date>?) " +
                "ORDER BY r.room_number";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectPopularity)) {
            preparedStatement.setInt(1, seats_number);
            preparedStatement.setString(2, price);
            preparedStatement.setInt(3, days);
            preparedStatement.setTimestamp(4, arrival_date);
            preparedStatement.setTimestamp(5, arrival_date);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                suitableRooms.add(Room
                        .builder()
                        .roomNumber(resultSet.getString("room_number"))
                        .seatsNumber(resultSet.getInt("seats_number"))
                        .comfort(resultSet.getString("comfort"))
                        .price(resultSet.getString("price"))
                        .build());
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suitableRooms;
    }

    public Map<String, Integer> findAvgPriceForComfort() {
        Map<String, Integer> comfortAndPrice = new LinkedHashMap<>();
        String selectAvgPrice = "SELECT r.comfort, AVG(r.price) AS avg_price " +
                "FROM Rooms r " +
                "GROUP BY r.comfort";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectAvgPrice)) {
            while (resultSet.next()) {
                comfortAndPrice.put(
                        resultSet.getString("comfort"),
                        resultSet.getInt("avg_price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comfortAndPrice;
    }

    public List<Room> findRoomsOccupiedBetweenDate(Date start, Date end) {
        List<Room> roomOccupiedBetweenDate = new ArrayList<>();
        String selectAvgPriceBetweenDate = "SELECT r.room_number, r.seats_number, r.comfort, r.price, " +
                "   a.arrival_date, a.departure_date " +
                "FROM HotelAccommodations a " +
                "JOIN Rooms r ON a.room_id = r.room_id " +
                "WHERE a.arrival_date >= ? AND a.departure_date <= ?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAvgPriceBetweenDate)) {

            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roomOccupiedBetweenDate.add(
                        Room.builder()
                                .roomNumber(resultSet.getString("room_number"))
                                .seatsNumber(resultSet.getInt("seats_number"))
                                .comfort(resultSet.getString("comfort"))
                                .price(resultSet.getString("price"))
                                .accommodations(List.of(HotelAccommodation.builder()
                                        .arrivalDate(resultSet.getTimestamp("arrival_date"))
                                        .departureDate(resultSet.getTimestamp("departure_date"))
                                        .build()))
                                .build()
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomOccupiedBetweenDate;
    }

    public Map<Room,Float> findRoomsIncomeInDateRange(Date start, Date end) {
        Map<Room,Float> roomIncomeInDateRange = new LinkedHashMap<>();
        String selectRoomIncomeInDateRange = "SELECT r.room_number, r.comfort, r.price, SUM(r.price) AS room_income " +
                "FROM Rooms r " +
                "INNER JOIN HotelAccommodations a ON r.room_id = a.room_id " +
                "WHERE a.arrival_date >= ? AND a.departure_date <= ? " +
                "GROUP BY r.room_number, r.comfort, r.price " +
                "ORDER BY r.room_number";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectRoomIncomeInDateRange)) {

            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roomIncomeInDateRange.put(
                        Room.builder()
                                .roomNumber(resultSet.getString("room_number"))
                                .comfort(resultSet.getString("comfort"))
                                .price(resultSet.getString("price"))
                                .build(),
                        resultSet.getFloat("room_income")
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomIncomeInDateRange;
    }

    public Map<Room,Integer> findRoomsComfortsAndNumberOfStayed  (Integer year) {
        Map<Room,Integer> roomComfortsAndNumberOfStayed = new LinkedHashMap<>();
        String selectSQL = "SELECT r.room_number, r.comfort, r.price, COUNT(*) AS booking_count " +
                "        FROM Rooms r " +
                "        INNER JOIN HotelAccommodations a ON r.room_id = a.room_id " +
                "        WHERE DATEPART(YEAR, a.arrival_date) = ? " +
                "GROUP BY r.room_number, r.comfort, r.price";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, year);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roomComfortsAndNumberOfStayed.put(
                        Room.builder()
                                .roomNumber(resultSet.getString("room_number"))
                                .comfort(resultSet.getString("comfort"))
                                .price(resultSet.getString("price"))
                                .build(),
                        resultSet.getInt("booking_count")
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomComfortsAndNumberOfStayed;
    }
}
