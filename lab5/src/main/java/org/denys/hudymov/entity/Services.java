package org.denys.hudymov.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @Column(name = "PRICE")
    private Integer price;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "CATEGORY", referencedColumnName = "CATEGORY",nullable = false)
    private ServicesCategory servicesCategory;
}
