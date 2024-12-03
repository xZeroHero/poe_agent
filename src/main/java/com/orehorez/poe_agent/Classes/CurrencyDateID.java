package com.orehorez.poe_agent.Classes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class CurrencyDateID
        implements Serializable {

    @Column(name = "currency_currency_id")
    private Long currencyId;

    @Column(name = "sample_date_date_id")
    private Long dateId;

    


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CurrencyDateID that = (CurrencyDateID) o;
        return Objects.equals(currencyId, that.currencyId) &&
                Objects.equals(dateId, that.dateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyId, dateId);
    }
}
