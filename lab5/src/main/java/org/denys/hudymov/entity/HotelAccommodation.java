package org.denys.hudymov.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HOTELACCOMMODATIONS")
public class HotelAccommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMMODATION_ID")
    private BigInteger accommodationId;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "CLIENT_ID", nullable = false)
    private Client client;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID", nullable = false)
    private Room room;

    @Column(name = "PASSPORT_DATA")
    private String passportData;

    @Column(name = "ROOM_NUMBER")
    private String roomNumber;

    @Column(name = "ARRIVAL_DATE")
    private Timestamp arrivalDate;

    @Column(name = "DEPARTURE_DATE")
    private Timestamp departureDate;

    @Column(name = "NOTE")
    private String note;
}
