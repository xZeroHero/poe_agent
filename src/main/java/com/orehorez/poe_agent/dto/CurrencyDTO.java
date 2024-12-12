package com.orehorez.poe_agent.dto;

import com.orehorez.poe_agent.Classes.CurrencyDateID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class CurrencyDTO {

    private Long id;
    private String currencyName;
    private double latestPayChaos;
    private double latestReceiveChaos;
    private String iconURL;
    private Double[] paySparkLine;
    private Double payTotalChange;
    private Double[] receiveSparkLine;
    private Double receiveTotalChange;
    private String tradeId;

    private String payPolylineString;
    private String receivePolylineString;


    public Double[] normalizeSparkline(Double[] sparkLine) {
        if (sparkLine != null) {
            for (int i = 0; i < sparkLine.length; i++) {
                if (sparkLine[i] == null) {
                    sparkLine[i] = 0.001;
                } else {
                    sparkLine[i] = (sparkLine[i] * -1) + 70;
                }
            }

        }
        return sparkLine;
    }

    public void setPaySparkLine(Double[] paySparkLine) {
        this.paySparkLine = normalizeSparkline(paySparkLine);
        setPayPolylineString(getPolylineString(this.paySparkLine));
    }

    public void setReceiveSparkLine(Double[] receiveSparkLine) {
        this.receiveSparkLine = normalizeSparkline(receiveSparkLine);
        setReceivePolylineString(getPolylineString(this.receiveSparkLine));

    }


    public String getPolylineString(Double[] sparkLine) {
        final Integer[] sparkLinePoints = {0, 33, 66, 99, 132, 165, 198};
        StringBuilder output = new StringBuilder();
        if (sparkLine != null){
        for (int i = 0; i < sparkLine.length; i++) {
            output.append(sparkLinePoints[i])
                    .append(", ")
                    .append(sparkLine[i])
                    .append("\n");
        }}
        return output.toString();
    }


}
