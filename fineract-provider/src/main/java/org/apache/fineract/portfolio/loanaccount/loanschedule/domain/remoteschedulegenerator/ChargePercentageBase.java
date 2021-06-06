package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChargePercentageBase {
  Flat("Flat"), ApprovedAmount("ApprovedAmount"), Interest("Interest"),
  UnutilizedPrincipalAmount("UnutilizedPrincipalAmount"), OverDueTotalAmount("OverDueTotalAmount");

  private final String value;

  private ChargePercentageBase(String value) {
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
