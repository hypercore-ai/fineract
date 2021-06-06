package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.fineract.portfolio.loanproduct.domain.AmortizationMethod;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteScheduleRequest {
  private LocalDate startDate;
  private double approvedAmount;
  private AmortizationMethod amortization;
  private Installment[] disbursements;
  private Frequency principalRepaymentFrequency;
  private Frequency interestRepaymentFrequency;
  private Period[] principalGracePeriods;
  private Period[] interestGracePeriods;
  private Rate[] annualInterestRate;
  private String daysInYear;
  private String daysInMonth;
  private InterestCalculationMethod interestCalculationMethod;
  private Fee[] fees;
  private PaymentStrategy[] paymentStrategies;
  private Transaction[] transactions;
  private TermVariation[] termVariations;
  private Charge[] charges;

  public LocalDate getStartDate() {
    return startDate;
  }

  public RemoteScheduleRequest() {
  }

  public Charge[] getCharges() {
    return charges;
  }

  public void setCharges(Charge[] charges) {
    this.charges = charges;
  }

  public TermVariation[] getTermVariations() {
    return termVariations;
  }

  public void setTermVariations(TermVariation[] termVariations) {
    this.termVariations = termVariations;
  }

  public Transaction[] getTransactions() {
    return transactions;
  }

  public void setTransactions(Transaction[] transactions) {
    this.transactions = transactions;
  }

  public PaymentStrategy[] getPaymentStrategies() {
    return paymentStrategies;
  }

  public void setPaymentStrategies(PaymentStrategy[] paymentStrategies) {
    this.paymentStrategies = paymentStrategies;
  }

  public Fee[] getFees() {
    return fees;
  }

  public void setFees(Fee[] fees) {
    this.fees = fees;
  }

  public InterestCalculationMethod getInterestCalculationMethod() {
    return interestCalculationMethod;
  }

  public void setInterestCalculationMethod(InterestCalculationMethod interestCalculationMethod) {
    this.interestCalculationMethod = interestCalculationMethod;
  }

  public String getDaysInMonth() {
    return daysInMonth;
  }

  public void setDaysInMonth(String daysInMonth) {
    this.daysInMonth = daysInMonth;
  }

  public String getDaysInYear() {
    return daysInYear;
  }

  public void setDaysInYear(String daysInYear) {
    this.daysInYear = daysInYear;
  }

  public Rate[] getAnnualInterestRate() {
    return annualInterestRate;
  }

  public void setAnnualInterestRate(Rate[] annualInterestRate) {
    this.annualInterestRate = annualInterestRate;
  }

  public Period[] getInterestGracePeriods() {
    return interestGracePeriods;
  }

  public void setInterestGracePeriods(Period[] interestGracePeriods) {
    this.interestGracePeriods = interestGracePeriods;
  }

  public Period[] getPrincipalGracePeriods() {
    return principalGracePeriods;
  }

  public void setPrincipalGracePeriods(Period[] principalGracePeriods) {
    this.principalGracePeriods = principalGracePeriods;
  }

  public Frequency getInterestRepaymentFrequency() {
    return interestRepaymentFrequency;
  }

  public void setInterestRepaymentFrequency(Frequency interestRepaymentFrequency) {
    this.interestRepaymentFrequency = interestRepaymentFrequency;
  }

  public Frequency getPrincipalRepaymentFrequency() {
    return principalRepaymentFrequency;
  }

  public void setPrincipalRepaymentFrequency(Frequency principalRepaymentFrequency) {
    this.principalRepaymentFrequency = principalRepaymentFrequency;
  }

  public Installment[] getDisbursements() {
    return disbursements;
  }

  public void setDisbursements(Installment[] disbursements) {
    this.disbursements = disbursements;
  }

  public AmortizationMethod getAmortization() {
    return amortization;
  }

  public void setAmortization(AmortizationMethod amortization) {
    this.amortization = amortization;
  }

  public double getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(double approvedAmount) {
    this.approvedAmount = approvedAmount;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }
}
