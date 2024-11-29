package com.orehorez.poe_agent.overview;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Receive {
  private int id;
  private int leagueId;
  private int payCurrencyId;
  private int getCurrencyId;
  private String sampleTimeUtc;
  private int count;
  private double value;
  private int dataPointCount;
  private boolean includesSecondary;
  private int listingCount;
}
