package org.denys.hudymov.repository;

import java.math.BigInteger;
import java.util.List;
import org.denys.hudymov.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository  extends JpaRepository<People, BigInteger> {

    @Query("SELECT p.passportData FROM People p")
    List<String> findAllPassportCode();
}
