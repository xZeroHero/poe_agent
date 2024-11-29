package com.orehorez.poe_agent.repository;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.Classes.CurrencyDate;
import com.orehorez.poe_agent.Classes.SampleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public interface CurrencyDateRepository extends JpaRepository<CurrencyDate, Integer> {

    Optional<CurrencyDate> findCurrencyDateByCurrencyAndSampleDate(Currency currency, SampleDate sampleDate);

}
