package com.orehorez.poe_agent.controller;


import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.dto.CurrencyDTO;
import com.orehorez.poe_agent.dto.CurrencyDateDTO;
import com.orehorez.poe_agent.repository.CurrencyRepository;
import com.orehorez.poe_agent.service.CurrencyDateService;
import com.orehorez.poe_agent.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class CurrencyController {

    private final CurrencyDateService currencyDateService;
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyDateService currencyDateService, CurrencyService currencyService) {
        this.currencyDateService = currencyDateService;
        this.currencyService = currencyService;
    }





    @GetMapping("/")
    public String showHomePage(Model model){
/*        List<CurrencyDateDTO> currencyList1 = currencyDateService.getCurrencyDateWithJoins();
        model.addAttribute("currencyList", currencyList1);*/

        List<Currency> currencyList = currencyService.findAll();
        model.addAttribute("currencyList" , currencyList);

        List<CurrencyDTO> currencyDTOList = currencyService.getCurrencyDTO();
        model.addAttribute("currencyDTOList" , currencyDTOList);


        return "index";
    }

    @GetMapping("/details")
    public String detailPage(@RequestParam Long id, Model model){
        model.addAttribute("currencyDate", currencyDateService.createCurrencyDateDTO(id));

        return "details";
    }
}
