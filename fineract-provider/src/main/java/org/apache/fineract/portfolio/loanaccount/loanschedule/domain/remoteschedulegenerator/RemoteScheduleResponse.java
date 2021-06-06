package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteScheduleResponse {
  private Installment[] installments;
  private Transaction[] transactions;
  private double totalPrincipalDisbursed;
  private double totalPrincipalExpected;
  private double totalPrincipalPaid;
  private double totalInterestCharged;
  private double totalFeeChargesCharged;
  private double totalPenaltyChargesCharged;
  private double totalRepaymentExpected;
  private double totalOutstanding;

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

  public double getTotalOutstanding() {
    return totalOutstanding;
  }

  public void setTotalOutstanding(double totalOutstanding) {
    this.totalOutstanding = totalOutstanding;
  }

  public double getTotalRepaymentExpected() {
    return totalRepaymentExpected;
  }

  public void setTotalRepaymentExpected(double totalRepaymentExpected) {
    this.totalRepaymentExpected = totalRepaymentExpected;
  }

  public double getTotalPenaltyChargesCharged() {
    return totalPenaltyChargesCharged;
  }

  public void setTotalPenaltyChargesCharged(double totalPenaltyChargesCharged) {
    this.totalPenaltyChargesCharged = totalPenaltyChargesCharged;
  }

  public double getTotalFeeChargesCharged() {
    return totalFeeChargesCharged;
  }

  public void setTotalFeeChargesCharged(double totalFeeChargesCharged) {
    this.totalFeeChargesCharged = totalFeeChargesCharged;
  }

  public double getTotalInterestCharged() {
    return totalInterestCharged;
  }

  public void setTotalInterestCharged(double totalInterestCharged) {
    this.totalInterestCharged = totalInterestCharged;
  }

  public double getTotalPrincipalPaid() {
    return totalPrincipalPaid;
  }

  public void setTotalPrincipalPaid(double totalPrincipalPaid) {
    this.totalPrincipalPaid = totalPrincipalPaid;
  }

  public double getTotalPrincipalExpected() {
    return totalPrincipalExpected;
  }

  public void setTotalPrincipalExpected(double totalPrincipalExpected) {
    this.totalPrincipalExpected = totalPrincipalExpected;
  }

  public double getTotalPrincipalDisbursed() {
    return totalPrincipalDisbursed;
  }

  public void setTotalPrincipalDisbursed(double totalPrincipalDisbursed) {
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
