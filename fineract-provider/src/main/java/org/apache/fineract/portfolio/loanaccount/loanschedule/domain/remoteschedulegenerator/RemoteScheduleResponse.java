package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteScheduleResponse {
  private Installment[] installments;
  private Transaction[] transactions;
  private float totalPrincipalDisbursed;
  private float totalPrincipalExpected;
  private float totalPrincipalPaid;
  private float totalInterestCharged;
  private float totalFeeChargesCharged;
  private float totalPenaltyChargesCharged;
  private float totalRepaymentExpected;
  private float totalOutstanding;

  @Override
  public String toString() {
    return "{" +
      " installments='" + Arrays.toString(getInstallments()) + "'" +
      ", transactions='" + Arrays.toString(getTransactions()) + "'" +
      ", totalPrincipalDisbursed='" + getTotalPrincipalDisbursed() + "'" +
      ", totalPrincipalExpected='" + getTotalPrincipalExpected() + "'" +
      ", totalPrincipalPaid='" + getTotalPrincipalPaid() + "'" +
      ", totalInterestCharged='" + getTotalInterestCharged() + "'" +
      ", totalFeeChargesCharged='" + getTotalFeeChargesCharged() + "'" +
      ", totalPenaltyChargesCharged='" + getTotalPenaltyChargesCharged() + "'" +
      ", totalRepaymentExpected='" + getTotalRepaymentExpected() + "'" +
      ", totalOutstanding='" + getTotalOutstanding() + "'" +
      "}";
  }

  public float getTotalOutstanding() {
    return totalOutstanding;
  }

  public void setTotalOutstanding(float totalOutstanding) {
    this.totalOutstanding = totalOutstanding;
  }

  public float getTotalRepaymentExpected() {
    return totalRepaymentExpected;
  }

  public void setTotalRepaymentExpected(float totalRepaymentExpected) {
    this.totalRepaymentExpected = totalRepaymentExpected;
  }

  public float getTotalPenaltyChargesCharged() {
    return totalPenaltyChargesCharged;
  }

  public void setTotalPenaltyChargesCharged(float totalPenaltyChargesCharged) {
    this.totalPenaltyChargesCharged = totalPenaltyChargesCharged;
  }

  public float getTotalFeeChargesCharged() {
    return totalFeeChargesCharged;
  }

  public void setTotalFeeChargesCharged(float totalFeeChargesCharged) {
    this.totalFeeChargesCharged = totalFeeChargesCharged;
  }

  public float getTotalInterestCharged() {
    return totalInterestCharged;
  }

  public void setTotalInterestCharged(float totalInterestCharged) {
    this.totalInterestCharged = totalInterestCharged;
  }

  public float getTotalPrincipalPaid() {
    return totalPrincipalPaid;
  }

  public void setTotalPrincipalPaid(float totalPrincipalPaid) {
    this.totalPrincipalPaid = totalPrincipalPaid;
  }

  public float getTotalPrincipalExpected() {
    return totalPrincipalExpected;
  }

  public void setTotalPrincipalExpected(float totalPrincipalExpected) {
    this.totalPrincipalExpected = totalPrincipalExpected;
  }

  public float getTotalPrincipalDisbursed() {
    return totalPrincipalDisbursed;
  }

  public void setTotalPrincipalDisbursed(float totalPrincipalDisbursed) {
    this.totalPrincipalDisbursed = totalPrincipalDisbursed;
  }

  public Transaction[] getTransactions() {
    return transactions;
  }

  public void setTransactions(Transaction[] transactions) {
    this.transactions = transactions;
  }

  public Installment[] getInstallments() {
    return installments;
  }

  public void setInstallments(Installment[] installments) {
    this.installments = installments;
  }

}
