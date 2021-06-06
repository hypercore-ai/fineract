package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public enum TermVariationType {
  UpdateInterest("UpdateInterest"), AddGrace("AddGrace"), SetTotalInstallment("SetTotalInstallment");

  private final String value;

  private TermVariationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
