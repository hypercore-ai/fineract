package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class Rate {
  private String date;
  private double value;

  public Rate() {
    super();
  }

  public String getDate() {
    return date;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
