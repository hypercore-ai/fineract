package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class GracePeriod {
  private int firstPeriod; // Starts from 1
  private int lastPeriod; // Starts from 1

  public GracePeriod() {
  }

  public int getFirstPeriod() {
    return firstPeriod;
  }

  public int getLastPeriod() {
    return lastPeriod;
  }

  public void setLastPeriod(int lastPeriod) {
    this.lastPeriod = lastPeriod;
  }

  public void setFirstPeriod(int firstPeriod) {
    this.firstPeriod = firstPeriod;
  }
}
