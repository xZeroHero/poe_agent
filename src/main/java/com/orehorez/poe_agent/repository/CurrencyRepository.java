package com.orehorez.poe_agent.repository;


import com.orehorez.poe_agent.Classes.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> findCurrencyByCurrencyId(Long currency_id);

}
