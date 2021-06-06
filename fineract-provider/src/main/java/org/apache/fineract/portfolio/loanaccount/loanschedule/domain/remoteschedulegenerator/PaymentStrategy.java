package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

public class PaymentStrategy {

  private LocalDate fromDate;
  private Strategy[] strategy;

  public PaymentStrategy() {
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public Strategy[] getStrategy() {
    return strategy;
  }

  public void setStrategy(Strategy[] strategy) {
    this.strategy = strategy;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

}
