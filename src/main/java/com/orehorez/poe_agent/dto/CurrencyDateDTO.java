package com.orehorez.poe_agent.dto;


import com.orehorez.poe_agent.Classes.CurrencyDateID;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class CurrencyDateDTO {
    private CurrencyDateID id;
    private String currencyName;
    private LocalDateTime sampleDate;
    private double payChaos;
    private double receiveChaos;
    private String iconURL;
    private List<Double> payChaosList;
    private String polyline;

    @Override
    public String toString() {
/*        return "CurrencyDateDTO{" +
                "id=" + id +
                ", currencyName='" + currencyName + '\'' +
                ", sampleDate=" + sampleDate +
                ", payChaos=" + payChaos +
                ", receiveChaos=" + receiveChaos +
                ", iconURL='" + iconURL + '\'' +
                ", payChaosList=" + payChaosList +
                '}';*/

        return "payChaosList=" + payChaosList;
    }

    public void setPayChaosList(List<Double> payChaosList) {
        setPolyline(payChaosList);
        this.payChaosList = payChaosList;
    }

    public void setPolyline(List<Double> payChaosList) {

        int currentStep = 0;
        int stepWidth = 1000 / payChaosList.size();
        StringBuilder polyline = new StringBuilder();

        double minPayChaos = payChaosList.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
        double maxPayChaos = payChaosList.stream().mapToDouble(Double::doubleValue).max().getAsDouble();

        double scaleFactor = 500 / (maxPayChaos - minPayChaos);

        for (Double payChaos : payChaosList) {
            double normalizedPayChaos = (payChaos - minPayChaos) * scaleFactor;
            polyline.append(currentStep)
                    .append(", ")
                    .append(500 - normalizedPayChaos)
                    .append("\n");
            currentStep += stepWidth;
        }

        this.polyline = polyline.toString();

    }

}
