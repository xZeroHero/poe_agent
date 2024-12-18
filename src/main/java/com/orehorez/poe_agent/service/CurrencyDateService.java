package com.orehorez.poe_agent.service;

import com.orehorez.poe_agent.Classes.Currency;

import com.orehorez.poe_agent.Classes.CurrencyDate;
import com.orehorez.poe_agent.Classes.CurrencyDateID;
import com.orehorez.poe_agent.Classes.SampleDate;
import com.orehorez.poe_agent.dto.CurrencyDateDTO;
import com.orehorez.poe_agent.repository.CurrencyDateRepository;
import com.orehorez.poe_agent.repository.CurrencyRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@Service
public class CurrencyDateService {


    private final SampleDateService sampleDateService;
    private final CurrencyService currencyService;
    private final CurrencyDateRepository currencyDateRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
    private final CurrencyRepository currencyRepository;
    String jsonString = JsonFetcher.getJsonFromUrl();

    @Autowired
    public CurrencyDateService(SampleDateService sampleDateService, CurrencyService currencyService, CurrencyDateRepository currencyDateRepository, CurrencyRepository currencyRepository) throws Exception {
        this.sampleDateService = sampleDateService;
        this.currencyService = currencyService;
        this.currencyDateRepository = currencyDateRepository;
        this.currencyRepository = currencyRepository;
    }

/*    public Double findLatestPayChaosByCurrency(Currency currency) {
        return currencyDateRepository.findLatestPayChaosByCurrency(currency);
    }*/


    public CurrencyDateDTO createCurrencyDateDTO(Long id){
        CurrencyDateDTO currencyDateDTO = new CurrencyDateDTO();
        List<Double> payChaosList = new ArrayList<>();
        List<CurrencyDate> currencyDateList = new ArrayList<>();

        currencyDateList = currencyDateRepository.findAllByCurrencyCurrencyId(id);

        for(CurrencyDate currencyDate : currencyDateList){
            payChaosList.add(currencyDate.getPayChaos());
        }
        System.out.println("\n\n#################################################\n\n");
        System.out.println(payChaosList);
        System.out.println("\n\n#################################################\n\n");

        currencyDateDTO.setPayChaosList(payChaosList);

        return currencyDateDTO;
    }




    public void saveSampleDateToDb() throws Exception {

        JSONObject json = new JSONObject(jsonString);
        JSONArray lines = json.getJSONArray("lines");
        SampleDate sampleDate = new SampleDate();
        for (int i = 0; i < lines.length(); i++) {
            JSONObject line = lines.getJSONObject(i);

            if (line.has("pay")) {
                JSONObject pay = line.getJSONObject("pay");

                sampleDate.setLeagueId(pay.getInt("league_id"));
                sampleDate.setDate(LocalDateTime.parse(pay.getString("sample_time_utc"), formatter));
                try {
                    sampleDateService.addNewSampleDate(sampleDate);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }


            }
        }


    }

    public String dataFromSparkline(JSONObject sparkLine) {
        JSONArray data = sparkLine.getJSONArray("data");
        String output = "";
        output += data.toString();

        return output;
    }



    public void saveJsonToDb() throws Exception {

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
                JSONObject paySparkLine = line.getJSONObject("paySparkLine");
                JSONObject receiveSparkLine = line.getJSONObject("receiveSparkLine");

                currencyDate.setPaySparkLine(dataFromSparkline(paySparkLine));
                currencyDate.setReceiveSparkLine(dataFromSparkline(receiveSparkLine));
                currencyDate.setPayTotalChange(paySparkLine.getDouble("totalChange"));
                currencyDate.setReceiveTotalChange(receiveSparkLine.getDouble("totalChange"));


                currencyDate.setReceiveChaos(pay.getDouble("value"));

                JSONObject receive = line.getJSONObject("receive");
                currencyDate.setPayChaos(receive.getDouble("value"));
                currencyDate.setId(new CurrencyDateID(currency.getCurrencyId(), sampleDate.getDateId()));
                addNewCurrencyDate(currencyDate);


            }

        }


    }

    public void addNewCurrencyDate(CurrencyDate currencyDate) {
        Optional<CurrencyDate> currencyDateOptional = currencyDateRepository
                .findCurrencyDateByCurrencyAndSampleDate(currencyDate.getCurrency(), currencyDate.getSampleDate());

        if (currencyDateOptional.isPresent()) {
            throw new IllegalStateException("Sample Date already present");
        }

        currencyDateRepository.save(currencyDate);
    }


    public List<CurrencyDateDTO> getCurrencyDateWithJoins() {
        List<CurrencyDate> currencyDates = currencyDateRepository.findAll();
        List<CurrencyDateDTO> dto = new ArrayList<>();

        for (CurrencyDate currencyDate : currencyDates) {
            CurrencyDateDTO currencyDateDTO = new CurrencyDateDTO();
            currencyDateDTO.setId(currencyDate.getId());
            currencyDateDTO.setCurrencyName(currencyDate.getCurrency().getName());
            currencyDateDTO.setSampleDate(currencyDate.getSampleDate().getDate());
            currencyDateDTO.setPayChaos(currencyDate.getPayChaos());
            currencyDateDTO.setReceiveChaos(currencyDate.getReceiveChaos());
            currencyDateDTO.setIconURL(currencyDate.getCurrency().getIcon());
            dto.add(currencyDateDTO);
        }
        return dto;

    }


}
