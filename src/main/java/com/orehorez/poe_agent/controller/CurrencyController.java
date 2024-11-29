package com.orehorez.poe_agent.controller;


import com.orehorez.poe_agent.dto.CurrencyDateDTO;
import com.orehorez.poe_agent.service.CurrencyDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyController {

    private final CurrencyDateService currencyDateService;

    @Autowired
    public CurrencyController(CurrencyDateService currencyDateService) {
        this.currencyDateService = currencyDateService;
    }

    @GetMapping
    public void initDB() throws Exception {
        currencyDateService.fetchJsonFromApi();
        currencyDateService.saveCurrencyToDb();
        currencyDateService.saveSampleDateToDb();
        currencyDateService.saveJsonToDb();

    }

    @GetMapping("/currencyPrice")
    public List<CurrencyDateDTO> getCurrencyPrice() { return currencyDateService.getCurrencyDateWithJoins(); }


}
