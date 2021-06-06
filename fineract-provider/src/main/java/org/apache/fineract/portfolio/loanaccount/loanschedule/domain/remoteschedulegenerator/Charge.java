package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.util.Date;

public class Charge {
  private String id;
  private Date createdTimestamp;
  private float amount;
  private float percentage;
  private ChargePercentageBase percentageBase;
  private Frequency chargeFrequency;
  private ChargeAppliesOn appliesOn;
}
