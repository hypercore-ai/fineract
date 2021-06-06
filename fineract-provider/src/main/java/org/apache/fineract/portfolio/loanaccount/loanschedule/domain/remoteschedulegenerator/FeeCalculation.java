package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FeeCalculation {
  Flat("Flat"), PercentageOfApprovedAmount("PercentageOfApprovedAmount"),
  PercentageOfInstallmentPrincipal("PercentageOfInstallmentPrincipal"),
  PercentageOfInstallmentInterest("PercentageOfInstallmentInterest"),
  PercentageOfInstallmentPrincipalAndInterest("PercentageOfInstallmentPrincipalAndInterest"),
  AnnualPercentageOfUnutilizedAmount("AnnualPercentageOfUnutilizedAmount"), PercentageOfOverdue("PercentageOfOverdue");

  private final String value;

  private FeeCalculation(String value) {
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
