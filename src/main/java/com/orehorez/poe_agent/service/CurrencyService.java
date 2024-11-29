package com.orehorez.poe_agent.service;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.Classes.SampleDate;
import com.orehorez.poe_agent.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }



public Currency findCurrencyByCurrencyId(Long id) {
    return currencyRepository
            .findCurrencyByCurrencyId(id)
            .orElseThrow(() -> new IllegalStateException("Currency with id " + id + " does not exist"));
}

    public Currency addNewCurrency(Currency currency) {
        Optional<Currency> currencyOptional = currencyRepository
                .findCurrencyByCurrencyId(currency.getCurrencyId());

        if (currencyOptional.isPresent()) {
            throw new IllegalStateException("Currency already present");
        }
        return currencyRepository.save(currency);
    }
}
