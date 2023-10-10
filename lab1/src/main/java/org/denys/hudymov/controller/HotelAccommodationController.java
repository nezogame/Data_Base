package org.denys.hudymov.controller;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.repository.HotelAccommodationDao;

import java.util.Vector;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class HotelAccommodationController {
    private static final HotelAccommodationDao HOTEL_ACCOMMODATION_DAO = HotelAccommodationDao.builder().build();

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
}
