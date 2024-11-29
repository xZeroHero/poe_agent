package com.orehorez.poe_agent.service;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.Classes.CurrencyDate;
import com.orehorez.poe_agent.Classes.CurrencyDateID;
import com.orehorez.poe_agent.Classes.SampleDate;
import com.orehorez.poe_agent.dto.CurrencyDateDTO;
import com.orehorez.poe_agent.repository.CurrencyDateRepository;
import com.orehorez.poe_agent.repository.SampleDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CurrencyDateService {


    private final SampleDateService sampleDateService;
    private final CurrencyService currencyService;
    private final CurrencyDateRepository currencyDateRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    @Autowired
    public CurrencyDateService(SampleDateService sampleDateService, CurrencyService currencyService, CurrencyDateRepository currencyDateRepository) {
        this.sampleDateService = sampleDateService;
        this.currencyService = currencyService;
        this.currencyDateRepository = currencyDateRepository;
    }

    public String fetchJsonFromApi() throws Exception {

        String apiUrl = "https://poe.ninja/api/data/currencyoverview?league=Settlers&type=Currency";
        String json = JsonFetcher.getJsonFromUrl(apiUrl);
        /*        System.out.println("API response(RAW): " + json);*/
        return json;
    }

    public void saveCurrencyToDb() throws Exception {
        String jsonString = fetchJsonFromApi();
        JSONObject json = new JSONObject(jsonString);
        JSONArray currencyDetails = json.getJSONArray("currencyDetails");

        for (int i = 0; i < currencyDetails.length(); i++) {
            JSONObject currencyDetail = currencyDetails.getJSONObject(i);
            Currency currency = new Currency();
            currency.setCurrencyId(currencyDetail.getLong("id"));
            currency.setName(currencyDetail.getString("name"));
            if (currencyDetail.has("icon")) {
                currency.setIcon(currencyDetail.getString("icon"));
            }

            currencyService.addNewCurrency(currency);

        }

    }

    public void saveSampleDateToDb() throws Exception {

        String jsonString = fetchJsonFromApi();
        JSONObject json = new JSONObject(jsonString);
        JSONArray lines = json.getJSONArray("lines");
        SampleDate sampleDate = new SampleDate();
        for (int i = 0; i < lines.length(); i++) {
            JSONObject line = lines.getJSONObject(i);

            if (line.has("pay")) {
                JSONObject pay = line.getJSONObject("pay");

                sampleDate.setLeagueId(pay.getInt("league_id"));
                sampleDate.setDate(LocalDateTime.parse(pay.getString("sample_time_utc"), formatter));
                break;
            }
        }

        sampleDateService.addNewSampleDate(sampleDate);


    }

    public void saveJsonToDb() throws Exception {

        String jsonString = fetchJsonFromApi();
        JSONObject json = new JSONObject(jsonString);
        JSONArray lines = json.getJSONArray("lines");


        for (int i = 0; i < lines.length(); i++) {
            JSONObject line = lines.getJSONObject(i);

            if (line.has("pay") && line.has("receive")) {
                CurrencyDate currencyDate = new CurrencyDate();


                JSONObject pay = line.getJSONObject("pay");

                //Sample Date
                SampleDate sampleDate = sampleDateService.findSampleDateByDateString(pay.getString("sample_time_utc"));

                currencyDate.setSampleDate(sampleDate);

                //Get Currency Object based of the ID and saves it in the Bi-Directional relationship table
                Currency currency = currencyService.findCurrencyByCurrencyId(pay.getLong("pay_currency_id"));
                currencyDate.setCurrency(currency);


                currencyDate.setReceiveChaos(pay.getDouble("value"));

                JSONObject receive = line.getJSONObject("receive");
                currencyDate.setPayChaos(receive.getDouble("value"));
                currencyDate.setId(new CurrencyDateID(currency.getCurrencyId(), sampleDate.getDateId()));
                this.addNewCurrencyDate(currencyDate);


            }

        }


    }

    public CurrencyDate addNewCurrencyDate(CurrencyDate currencyDate) {
        Optional<CurrencyDate> currencyDateOptional = currencyDateRepository
                .findCurrencyDateByCurrencyAndSampleDate(currencyDate.getCurrency(), currencyDate.getSampleDate());

        if (currencyDateOptional.isPresent()) {
            throw new IllegalStateException("Sample Date already present");
        }

        return currencyDateRepository.save(currencyDate);
    }


    public List<CurrencyDateDTO> getCurrencyDateWithJoins(){
        List<CurrencyDate> currencyDates = currencyDateRepository.findAll();
        List<CurrencyDateDTO> dto = new ArrayList<>();

        for (CurrencyDate currencyDate : currencyDates) {
            CurrencyDateDTO currencyDateDTO = new CurrencyDateDTO();
            currencyDateDTO.setId(currencyDate.getId());
            currencyDateDTO.setCurrencyName(currencyDate.getCurrency().getName());
            currencyDateDTO.setSampleDate(currencyDate.getSampleDate().getDate());
            currencyDateDTO.setPayChaos(currencyDate.getPayChaos());
            currencyDateDTO.setReceiveChaos(currencyDate.getReceiveChaos());
            dto.add(currencyDateDTO);
        }
        return dto;


    }

}
