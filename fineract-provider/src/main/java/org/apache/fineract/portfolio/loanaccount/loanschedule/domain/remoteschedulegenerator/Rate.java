package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class Rate {
  private String date;
  private float value;

  public Rate() {
    super();
  }

  public String getDate() {
    return date;
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
