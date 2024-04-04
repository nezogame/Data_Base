package org.denys.hudymov.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigInteger;
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
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private BigInteger roomId;

    @Column(name = "ROOM_NUMBER")
    private String roomNumber;

    @Column(name = "SEATS_NUMBER")
    private Integer seatsNumber;

    @Column(name = "COMFORT")
    private String comfort;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "OCCUPIED")
    private boolean occupied;

    @OneToMany(mappedBy = "room")
    private List<HotelAccommodation> accommodations;
}
