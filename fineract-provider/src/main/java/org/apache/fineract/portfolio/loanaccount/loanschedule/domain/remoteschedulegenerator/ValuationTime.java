package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ValuationTime {
  PaymentDate("PaymentDate"), NextInstallmentDate("NextInstallmentDate");

  private final String value;

  private ValuationTime(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
