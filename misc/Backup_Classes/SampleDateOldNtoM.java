package com.orehorez.poe_agent.Classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "sample_date")
public class SampleDateOldNtoM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long date_id;
    private LocalDate date;
    private Integer league_id;
    @ManyToMany(mappedBy = "sample_date")

    private List<Currency> currency = new ArrayList<>();



}
