package org.denys.hudymov.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID")
    private BigInteger clientId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PASSPORT_DATA", referencedColumnName = "PASSPORT_DATA",nullable = false)
    private People people;

    @Column(name = "USER_COMMENT")
    private String userComment;

    @OneToMany(mappedBy = "client")
    private List<HotelAccommodation> accommodations;
}
