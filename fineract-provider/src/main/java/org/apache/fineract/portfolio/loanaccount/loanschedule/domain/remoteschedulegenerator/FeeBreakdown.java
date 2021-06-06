package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class FeeBreakdown {
  private String externalId;
  private float amountDue;
  private float amountPaid;
  
  public FeeBreakdown() {
    super();
  }

  public String getExternalId() {
    return externalId;
  }

  public float getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(float amountPaid) {
    this.amountPaid = amountPaid;
  }

  public float getAmountDue() {
    return amountDue;
  }

  public void setAmountDue(float amountDue) {
    this.amountDue = amountDue;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }
}
