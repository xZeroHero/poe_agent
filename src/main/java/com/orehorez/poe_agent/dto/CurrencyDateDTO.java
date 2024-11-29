package com.orehorez.poe_agent.dto;


import com.orehorez.poe_agent.Classes.CurrencyDateID;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class CurrencyDateDTO {
    private CurrencyDateID id;
    private String currencyName;
    private LocalDateTime sampleDate;
    private double payChaos;
    private double receiveChaos;

    @Override
    public String toString() {
        return "CurrencyDateDTO{" +
                "id=" + id +
                ", currencyName='" + currencyName + '\'' +
                ", sampleDate=" + sampleDate +
                ", payChaos=" + payChaos +
                ", receiveChaos=" + receiveChaos +
                '}';
    }
}
