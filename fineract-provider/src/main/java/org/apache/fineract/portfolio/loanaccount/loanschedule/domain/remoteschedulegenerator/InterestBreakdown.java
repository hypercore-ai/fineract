package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

public class InterestBreakdown {
  private int subPeriod;
  private LocalDate startDate;
  private LocalDate endDate;
  private float principalBalance;
  private float interestDue;
  private float interestPercentage;

  public InterestBreakdown() {
    super();
  }

  public int getSubPeriod() {
    return subPeriod;
  }

  public float getInterestPercentage() {
    return interestPercentage;
  }

  public void setInterestPercentage(float interestPercentage) {
    this.interestPercentage = interestPercentage;
  }

  public float getInterestDue() {
    return interestDue;
  }

  public void setInterestDue(float interestDue) {
    this.interestDue = interestDue;
  }

  public float getPrincipalBalance() {
    return principalBalance;
  }

  public void setPrincipalBalance(float principalBalance) {
    this.principalBalance = principalBalance;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void setSubPeriod(int subPeriod) {
    this.subPeriod = subPeriod;
  }
}
