package com.orehorez.poe_agent.service;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.Classes.CurrencyDate;
import com.orehorez.poe_agent.dto.CurrencyDTO;
import com.orehorez.poe_agent.repository.CurrencyDateRepository;
import com.orehorez.poe_agent.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyDateRepository currencyDateRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyDateRepository currencyDateRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyDateRepository = currencyDateRepository;
    }


    public List<CurrencyDTO> getCurrencyDTO() {
        List<Currency> currencyList = currencyRepository.findAll();
        List<CurrencyDTO> dto = new ArrayList<>();

        for (Currency currency : currencyList) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(currency.getCurrencyId());
            currencyDTO.setCurrencyName(currency.getName());
            currencyDTO.setLatestPayChaos(findLatestPayChaos(currency));
            currencyDTO.setLatestReceiveChaos(1/findLatestReceiveChaos(currency));
            currencyDTO.setIconURL(currency.getIcon());
            dto.add(currencyDTO);
        }
        return dto;


    }


    public double findLatestPayChaos(Currency currency) {
        Double d =  currencyDateRepository.findLatestPayChaosByCurrency(currency.getCurrencyId());
        if (d == null){
            return 0;
        }
        return  d;
    }

    public double findLatestReceiveChaos(Currency currency) {
        Double d =  currencyDateRepository.findLatestReceiveChaosByCurrency(currency.getCurrencyId());
        if (d == null){
            return 0;
        }
        return  d;
    }


    public List<Currency> findAll() {
        List<Currency> currencyList = currencyRepository.findAll();
        return currencyList;
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
