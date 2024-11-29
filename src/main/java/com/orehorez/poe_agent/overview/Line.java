package com.orehorez.poe_agent.overview;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Line {
  private String currencyTypeName;
  private Pay pay;
  private Receive receive;
  private PaySparkLine paySparkLine;
  private ReceiveSparkLine receiveSparkLine;
  private double chaosEquivalent;
  private LowConfidencePaySparkLine lowConfidencePaySparkLine;
  private LowConfidenceReceiveSparkLine lowConfidenceReceiveSparkLine;
  private String detailsId;
}
