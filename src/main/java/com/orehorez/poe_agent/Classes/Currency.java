package com.orehorez.poe_agent.Classes;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Getter
@Setter

@Entity(name = "Currency")
@Table(name = "currency")
/*@NaturalIdCache
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)*/
public class Currency {

    @Id
    private Long currencyId;
    private String name;
    private String icon;
    @OneToMany(
            mappedBy = "currency",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CurrencyDate> currencyDate = new ArrayList<>();

    public void addSampleDate(SampleDate sampleDate) {
        CurrencyDate currencyDate = new CurrencyDate(this, sampleDate);
        this.currencyDate.add(currencyDate );
        sampleDate.getCurrencyDate().add(currencyDate);
    }

    public void removeSampleDate(SampleDate sampleDate) {
        for (Iterator<CurrencyDate> iterator = currencyDate.iterator();
             iterator.hasNext(); ) {
            CurrencyDate currencyDate = iterator.next();

            if (currencyDate.getCurrency().equals(this) &&
                    currencyDate.getSampleDate().equals(sampleDate)) {
                iterator.remove();
                currencyDate.getSampleDate().getCurrencyDate().remove(currencyDate);
                currencyDate.setCurrency(null);
                currencyDate.setSampleDate(null);
            }
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(name, currency.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}



