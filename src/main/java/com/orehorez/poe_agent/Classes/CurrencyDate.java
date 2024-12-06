package com.orehorez.poe_agent.Classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor


@Entity(name = "CurrencyDate")
@Table(name = "currency_Date")
public class CurrencyDate {

    @EmbeddedId
    private CurrencyDateID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @MapsId("currencyId")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @MapsId("dateId")
    private SampleDate sampleDate;

    @Column(name = "pay_Chaos")
    private double payChaos;

    @Column(name = "receive_Chaos")
    private double receiveChaos;

    private String paySparkLine;
    private Double payTotalChange;
    private String receiveSparkLine;
    private Double receiveTotalChange;



    public CurrencyDate(Currency currency, SampleDate sampleDate) {
        this.currency = currency;
        this.sampleDate = sampleDate;
//        this.id = new CurrencyDateID(currency.getCurrencyId(), sampleDate.getDateId());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CurrencyDate that = (CurrencyDate) o;
        return Objects.equals(currency, that.currency) &&
                Objects.equals(sampleDate, that.sampleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, sampleDate);
    }
}