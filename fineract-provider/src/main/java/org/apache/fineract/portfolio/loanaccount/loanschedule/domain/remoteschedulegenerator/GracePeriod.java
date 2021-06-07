package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class GracePeriod {
  private int startPeriod; // Starts from 1
  private int endPeriod; // Starts from 1

  public GracePeriod() {
  }

  public int getStartPeriod() {
    return startPeriod;
  }

  public int getEndPeriod() {
    return endPeriod;
  }

  public void setEndPeriod(int endPeriod) {
    this.endPeriod = endPeriod;
  }

  public void setStartPeriod(int startPeriod) {
    this.startPeriod = startPeriod;
  }
}
