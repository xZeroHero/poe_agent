package com.orehorez.poe_agent.Classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "SampleDate")
@Table(name = "sample_date")
public class SampleDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long dateId;
    private LocalDateTime date;
    @Column(name = "league_id")
    private Integer leagueId;

    @OneToMany(
            mappedBy = "sampleDate",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CurrencyDate> currencyDate = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleDate that = (SampleDate) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }
}
