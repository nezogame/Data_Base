package org.denys.hudymov.repository;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.HotelAccommodation;

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
public class HotelAccommodationDao implements Dao<HotelAccommodation> {
    @Override
    public void create(HotelAccommodation entity) {

    }

    @Override
    public List<HotelAccommodation> read() {
        List<HotelAccommodation> hotelAccommodations = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM HotelAccommodations");
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
        return Optional.empty();
    }

    @Override
    public void update(HotelAccommodation entity) {

    }

    @Override
    public void delete(int entityId) {

    }
}
