package com.orehorez.poe_agent.controller;

import com.orehorez.poe_agent.dto.CurrencyDTO;
import com.orehorez.poe_agent.dto.CurrencyDateDTO;
import com.orehorez.poe_agent.service.CurrencyDateService;
import com.orehorez.poe_agent.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyRestController {

    private final CurrencyDateService currencyDateService;
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyRestController(CurrencyDateService currencyDateService, CurrencyService currencyService) {
        this.currencyDateService = currencyDateService;
        this.currencyService = currencyService;
    }

    @GetMapping("/currencyPrice")
    public List<CurrencyDateDTO> getCurrencyPrice() { return currencyDateService.getCurrencyDateWithJoins(); }

    @GetMapping("/currencyTest")
    public List<CurrencyDTO> getCurrencyTest(){
        return currencyService.getCurrencyDTO();
    }

    @GetMapping("db_refresh")
    public void initDB() throws Exception {
        currencyDateService.fetchJsonFromApi();
        currencyDateService.saveCurrencyToDb();
        currencyDateService.saveSampleDateToDb();
        currencyDateService.saveJsonToDb();
    }



}
