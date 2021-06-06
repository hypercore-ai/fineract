package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.util.Date;

public class Charge {
  private String id;
  private Date createdTimestamp;
  private double amount;
  private double percentage;
  private ChargePercentageBase percentageBase;
  private Frequency chargeFrequency;
  private ChargeAppliesOn appliesOn;
}
