package com.orehorez.poe_agent.Classes;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "currency")
public class CurrencyOldNtoM {

    @Id
    private Long currency_id;
    private String name;
    private String icon;
    @ManyToMany
    @JoinTable(name = "currency_date",
            joinColumns = @JoinColumn(name = "date_id"),
            inverseJoinColumns = @JoinColumn(name = "currency_id"))
    private List<SampleDate> sample_date = new ArrayList<>();






}
