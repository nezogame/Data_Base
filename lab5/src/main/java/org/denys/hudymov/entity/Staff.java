package org.denys.hudymov.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigInteger;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STAFF_ID")
    private BigInteger staffId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SALARY")
    private Integer salary;

    @Column(name = "EMPLOYMENT_DATE")
    private Date employmentDate;

    @Column(name = "EMAIL")
    private String email;
}
