package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import org.apache.fineract.portfolio.loanproduct.domain.AmortizationMethod;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteScheduleRequest {

    private LocalDate startDate;
    private double approvedAmount;
    private AmortizationMethod amortization;
    private Installment[] disbursements;
    private Frequency principalRepaymentFrequency;
    private Frequency interestRepaymentFrequency;
    private GracePeriod[] principalGracePeriods;
    private GracePeriod[] interestGracePeriods;
    private Rate[] interestRates;
    private String daysInYear;
    private String daysInMonth;
    private InterestCalculationMethod interestCalculationMethod;
    private Fee[] fees;
    private PaymentStrategy[] paymentStrategies;
    private Transaction[] transactions;
    private TermVariation[] termVariations;

    public LocalDate getStartDate() {
        return startDate;
    }

    public RemoteScheduleRequest() {}

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

    public Rate[] getInterestRates() {
        return interestRates;
    }

    public void setInterestRates(Rate[] interestRates) {
        this.interestRates = interestRates;
    }

    public GracePeriod[] getInterestGracePeriods() {
        return interestGracePeriods;
    }

    public void setInterestGracePeriods(GracePeriod[] interestGracePeriods) {
        this.interestGracePeriods = interestGracePeriods;
    }

    public GracePeriod[] getPrincipalGracePeriods() {
        return principalGracePeriods;
    }

    public void setPrincipalGracePeriods(GracePeriod[] principalGracePeriods) {
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
