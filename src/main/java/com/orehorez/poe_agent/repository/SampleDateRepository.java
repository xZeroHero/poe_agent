package com.orehorez.poe_agent.repository;

import com.orehorez.poe_agent.Classes.SampleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public interface SampleDateRepository extends JpaRepository<SampleDate, Integer> {

    Optional<SampleDate> findSampleDateByDate(LocalDateTime localDateTime);


}
