package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class Strategy {
  private PaymentComponent component;
  private ValuationTime valuationTime;

  public Strategy() {
    super();
  }

  public PaymentComponent getComponent() {
    return component;
  }

  public ValuationTime getValuationTime() {
    return valuationTime;
  }

  public void setValuationTime(ValuationTime valuationTime) {
    this.valuationTime = valuationTime;
  }

  public void setComponent(PaymentComponent component) {
    this.component = component;
  }
}