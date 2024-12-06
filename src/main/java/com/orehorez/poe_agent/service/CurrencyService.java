package com.orehorez.poe_agent.service;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.dto.CurrencyDTO;
import com.orehorez.poe_agent.repository.CurrencyDateRepository;
import com.orehorez.poe_agent.repository.CurrencyRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    String jsonString = JsonFetcher.getJsonFromUrl();
    private final CurrencyDateRepository currencyDateRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyDateRepository currencyDateRepository) throws Exception {
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
            currencyDTO.setLatestReceiveChaos(1 / findLatestReceiveChaos(currency));
            currencyDTO.setIconURL(currency.getIcon());
            currencyDTO.setPaySparkLine(getDoubleArrayFromString(findLatestPaySparkLine(currency)));
            currencyDTO.setReceiveSparkLine(getDoubleArrayFromString(findLatestReceiveSparkLine(currency)));
            currencyDTO.setPayTotalChange(findLatestPayTotalChange(currency));
            currencyDTO.setReceiveTotalChange(findLatestReceiveTotalChange(currency));
            dto.add(currencyDTO);
        }
        return dto;

    }

    public Double[] getDoubleArrayFromString(String input) {
        if (input != null && !input.equals("[]")) {
            JSONArray jsonArray = new JSONArray(input);
            Double[] doubleArray = new Double[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                if (!jsonArray.isNull(i)) {
                    doubleArray[i] = jsonArray.optDouble(i);
                } else {
                    if (i == 0) {
                        doubleArray[i] = jsonArray.optDouble(i + 1);
                    } else {
                        if (i == jsonArray.length() - 1) {
                            doubleArray[i] = jsonArray.optDouble(i - 1);
                        } else {
                            doubleArray[i] = (jsonArray.optDouble(i - 1) + jsonArray.optDouble(i + 1)) / 2;
                        }
                    }
                }
            }
            return doubleArray;
        } else {
            // Handle the case where the input string is null
            return null;
        }

    }


    public double findLatestPayChaos(Currency currency) {

        Double d = currencyDateRepository.findLatestPayChaosByCurrency(currency.getCurrencyId());
        if (d == null) {
            return 0;
        }
        return d;
    }

    public double findLatestReceiveChaos(Currency currency) {
        Double d = currencyDateRepository.findLatestReceiveChaosByCurrency(currency.getCurrencyId());
        if (d == null) {
            return 0;
        }
        return d;
    }

    public String findLatestPaySparkLine(Currency currency) {
        return currencyDateRepository.findLatestPaySparkLineByCurrency(currency.getCurrencyId());
    }

    public String findLatestReceiveSparkLine(Currency currency) {
        return currencyDateRepository.findLatestReceiveSparkLineByCurrency(currency.getCurrencyId());
    }

    public Double findLatestPayTotalChange(Currency currency) {
        return currencyDateRepository.findLatestPayTotalChangeByCurrency(currency.getCurrencyId());
    }

    public Double findLatestReceiveTotalChange(Currency currency) {
        return currencyDateRepository.findLatestReceiveTotalChangeByCurrency(currency.getCurrencyId());
    }


    public List<Currency> findAll() {
        return currencyRepository.findAll();

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


    public void saveCurrencyToDb() throws Exception {

        JSONObject json = new JSONObject(jsonString);
        JSONArray currencyDetails = json.getJSONArray("currencyDetails");
        JSONArray lines = json.getJSONArray("lines");


        //Loop for the data from "currencyDetails"
        for (int i = 0; i < currencyDetails.length(); i++) {
            JSONObject currencyDetail = currencyDetails.getJSONObject(i);
            Currency currency = new Currency();
            currency.setCurrencyId(currencyDetail.getLong("id"));
            currency.setName(currencyDetail.getString("name"));
            if (currencyDetail.has("tradeId")) {
                currency.setTradeId(currencyDetail.getString("tradeId"));
            }
            if (currencyDetail.has("icon")) {
                currency.setIcon(currencyDetail.getString("icon"));
            }


            currencyRepository.save(currency);

        }


    }
}

