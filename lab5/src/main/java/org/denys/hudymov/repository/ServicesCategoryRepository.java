package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.util.List;
import org.denys.hudymov.entity.ServicesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesCategoryRepository extends JpaRepository<ServicesCategory, String> {

    @Query(value = "SELECT sc.Category, sc.description\n" +
            "FROM ServicesCategory sc\n" +
            "LEFT JOIN Services s ON sc.Category = s.category\n" +
            "WHERE s.category IS NULL",
    nativeQuery = true)
    List<ServicesCategory> findAllByServicesCategoryEmpty();
}
