package com.orehorez.poe_agent.repository;

import com.orehorez.poe_agent.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {



}
