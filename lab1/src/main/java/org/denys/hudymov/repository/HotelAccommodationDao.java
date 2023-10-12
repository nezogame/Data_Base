package org.denys.hudymov.repository;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.*;
import org.jetbrains.annotations.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class HotelAccommodationDao implements Dao<HotelAccommodation> {

    private final String INSERT_SQL =
            "INSERT INTO HotelAccommodations " +
                    "VALUES(?,?,?,?,?)";

    private final String UPDATE_SQL = "UPDATE HotelAccommodations SET client_id=?, room_id=?, arrival_date=?, " +
            "departure_date=?, note=? " +
            "WHERE accommodation_id=?";

    private final String DELETE_SQL = "DELETE FROM  HotelAccommodations " +
            "WHERE accommodation_id=?";
    @Override
    public void create(HotelAccommodation entity) throws SQLException{
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_SQL)) {

            setParameters(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public List<HotelAccommodation> read() {
        List<HotelAccommodation> hotelAccommodations = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM HotelAccommodations ORDER BY arrival_date DESC");
            while (resultSet.next()) {
                HotelAccommodation hotelAccommodation = HotelAccommodation
                        .builder()
                        .accommodationId(resultSet.getLong("accommodation_id"))
                        .clientId(resultSet.getLong("client_id"))
                        .roomId(resultSet.getLong("room_id"))
                        .arrivalDate(resultSet.getTimestamp("arrival_date"))
                        .departureDate(resultSet.getTimestamp("departure_date"))
                        .note(resultSet.getString("note"))
                        .build();
                hotelAccommodations.add(hotelAccommodation);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelAccommodations;
    }

    @Override
    public Optional<HotelAccommodation> get(long id) {
        Optional<HotelAccommodation> accommodation = Optional.ofNullable(null);
        String SelectQuery = "SELECT * FROM HotelAccommodations WHERE accommodation_id=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SelectQuery);
        ) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Check if there is a result
                accommodation = Optional.of(HotelAccommodation
                        .builder()
                        .accommodationId(resultSet.getLong("accommodation_id"))
                        .clientId(resultSet.getLong("client_id"))
                        .roomId(resultSet.getLong("room_id"))
                        .arrivalDate(resultSet.getTimestamp("arrival_date"))
                        .departureDate(resultSet.getTimestamp("departure_date"))
                        .note(resultSet.getString("note"))
                        .build());
            }

            // Don't close the resultSet here.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accommodation;
    }

    @Override
    public void update(HotelAccommodation entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            setParameters(preparedStatement, entity);
            preparedStatement.setLong(6, entity.getAccommodationId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setParameters(@NotNull PreparedStatement preparedStatement, @NotNull HotelAccommodation entity) throws SQLException {
        preparedStatement.setLong(1, entity.getClientId());
        preparedStatement.setLong(2, entity.getRoomId());
        preparedStatement.setTimestamp(3, entity.getArrivalDate());
        preparedStatement.setTimestamp(4, entity.getDepartureDate());
        preparedStatement.setString(5, entity.getNote());
    }

    public List<Long> getAllId() {
        List<Long> accommodationsId = new ArrayList<>();
        String selectSQL = "SELECT accommodation_id FROM HotelAccommodations " +
                "ORDER BY accommodation_id DESC ";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                accommodationsId.add(resultSet.getLong("accommodation_id"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accommodationsId;
    }
}
