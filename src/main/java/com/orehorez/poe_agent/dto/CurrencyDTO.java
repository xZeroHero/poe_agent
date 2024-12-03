package com.orehorez.poe_agent.dto;

import com.orehorez.poe_agent.Classes.CurrencyDateID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor

public class CurrencyDTO {

    private Long id;
    private String currencyName;
    private double latestPayChaos;
    private double latestReceiveChaos;
    private String iconURL;


    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "id=" + id +
                ", currencyName='" + currencyName + '\'' +
                ", latestPayChaos=" + latestPayChaos +
                ", latestReceiveChaos=" + latestReceiveChaos +
                ", iconURL='" + iconURL + '\'' +
                '}';
    }
}
