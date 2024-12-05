package com.orehorez.poe_agent.service;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.Classes.CurrencyDate;
import com.orehorez.poe_agent.dto.CurrencyDTO;
import com.orehorez.poe_agent.repository.CurrencyDateRepository;
import com.orehorez.poe_agent.repository.CurrencyRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyDateRepository currencyDateRepository;
    String jsonString = JsonFetcher.getJsonFromUrl();

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyDateRepository currencyDateRepository) throws Exception {
        this.currencyRepository = currencyRepository;
        this.currencyDateRepository = currencyDateRepository;
    }


    public String dataFromSparkline(JSONObject sparkLine) {
        JSONArray data = sparkLine.getJSONArray("data");
        String output = "";
        output += data.toString();

        return output;
    }

    public void saveCurrencyToDb() throws Exception {

        JSONObject json = new JSONObject(jsonString);
        JSONArray currencyDetails = json.getJSONArray("currencyDetails");
        JSONArray lines = json.getJSONArray("lines");
        Map<Long, Currency> currencyMap = new HashMap<>();


        //Loop for the data in "lines"
        for (int i = 0; i < lines.length(); i++) {
            Currency currency = new Currency();
            JSONObject line = lines.getJSONObject(i);
            if (line.has("pay")) {
                JSONObject pay = line.getJSONObject("pay");
                currency.setCurrencyId(pay.getLong("pay_currency_id"));
            }
            JSONObject paySparkLine = line.getJSONObject("paySparkLine");
            JSONObject receiveSparkLine = line.getJSONObject("receiveSparkLine");

            currency.setPaySparkLine(dataFromSparkline(paySparkLine));
            currency.setReceiveSparkLine(dataFromSparkline(receiveSparkLine));
            currency.setPayTotalChange(paySparkLine.getDouble("totalChange"));
            currency.setReceiveTotalChange(receiveSparkLine.getDouble("totalChange"));


            //Saves the Currency Object with only the contents from "lines"
            currencyMap.put(currency.getCurrencyId(), currency);

            /*currencyRepository.save(currency);*/
        }

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

            // Loads and adds the missing data from "currencyDetails" to the data from "lines"
            Currency currencyFromMap = currencyMap.get(currency.getCurrencyId());
            if (currencyFromMap != null) {
                currency.setPaySparkLine(currencyFromMap.getPaySparkLine());
                currency.setPayTotalChange(currencyFromMap.getPayTotalChange());
                currency.setReceiveSparkLine(currencyFromMap.getReceiveSparkLine());
                currency.setReceiveTotalChange(currencyFromMap.getReceiveTotalChange());
            }
/*            try {
                currencyService.addNewCurrency(currency);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }*/

            currencyRepository.save(currency);

        }

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
            currencyDTO.setPaySparkLine(getDoubleArrayFromString(currency.getPaySparkLine()));
            currencyDTO.setReceiveSparkLine(getDoubleArrayFromString(currency.getReceiveSparkLine()));
            currencyDTO.setPayTotalChange(currency.getPayTotalChange());
            currencyDTO.setReceiveTotalChange(currency.getReceiveTotalChange());
            dto.add(currencyDTO);
        }
        return dto;

    }

    public Double[] getDoubleArrayFromString(String input) {
        if (input != null) {
            JSONArray jsonArray = new JSONArray(input);
            Double[] doubleArray = new Double[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                doubleArray[i] = jsonArray.optDouble(i);
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
