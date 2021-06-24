package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Arrays;

public class Installment {

    private InstallmentType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private double amount;
    private int period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private InterestBreakdown[] breakdown;
    private InstallmentComponent due;
    private InstallmentComponent paid;
    private InstallmentComponent waived;
    private InstallmentComponent writtenOff;
    private InstallmentComponent early;
    private InstallmentComponent late;
    private FeeBreakdown[] feesBreakdown;
    private FeeBreakdown[] penaltyBreakdown;
    private String[] transactionsId;
    private boolean obligationMet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate obligationMetOnDate;
    private double disbursementFees;

    public double getOutstandingPrincipalBalance() {
        return outstandingPrincipalBalance;
    }

    public void setOutstandingPrincipalBalance(double outstandingPrincipalBalance) {
        this.outstandingPrincipalBalance = outstandingPrincipalBalance;
    }

    private double outstandingPrincipalBalance;

    public Installment() {
        super();
    }

    public InstallmentType getType() {
        return type;
    }

    public LocalDate getObligationMetOnDate() {
        return obligationMetOnDate;
    }

    public void setObligationMetOnDate(LocalDate obligationMetOnDate) {
        this.obligationMetOnDate = obligationMetOnDate;
    }

    public boolean isObligationMet() {
        return obligationMet;
    }

    public void setObligationMet(boolean obligationMet) {
        this.obligationMet = obligationMet;
    }

    public String[] getTransactionsId() {
        return transactionsId;
    }

    public void setTransactionsId(String[] transactionsId) {
        this.transactionsId = transactionsId;
    }

    public FeeBreakdown[] getPenaltyBreakdown() {
        return penaltyBreakdown;
    }

    public void setPenaltyBreakdown(FeeBreakdown[] penaltyBreakdown) {
        this.penaltyBreakdown = penaltyBreakdown;
    }

    public FeeBreakdown[] getFeesBreakdown() {
        return feesBreakdown;
    }

    public void setFeesBreakdown(FeeBreakdown[] feesBreakdown) {
        this.feesBreakdown = feesBreakdown;
    }

    public InstallmentComponent getLate() {
        return late;
    }

    public void setLate(InstallmentComponent late) {
        this.late = late;
    }

    public InstallmentComponent getEarly() {
        return early;
    }

    public void setEarly(InstallmentComponent early) {
        this.early = early;
    }

    public InstallmentComponent getWrittenOff() {
        return writtenOff;
    }

    public void setWrittenOff(InstallmentComponent writtenOff) {
        this.writtenOff = writtenOff;
    }

    public InstallmentComponent getWaived() {
        return waived;
    }

    public void setWaived(InstallmentComponent waived) {
        this.waived = waived;
    }

    public InstallmentComponent getPaid() {
        return paid;
    }

    public void setPaid(InstallmentComponent paid) {
        this.paid = paid;
    }

    public InstallmentComponent getDue() {
        return due;
    }

    public void setDue(InstallmentComponent due) {
        this.due = due;
    }

    public InterestBreakdown[] getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(InterestBreakdown[] breakdown) {
        this.breakdown = breakdown;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(InstallmentType type) {
        this.type = type;
    }

    public double getDisbursementFees() {
        return disbursementFees;
    }

    public void setDisbursementFees(double disbursementFees) {
        this.disbursementFees = disbursementFees;
    }

    @Override
    public String toString() {
        return "Installment{" + "type=" + type + ", date=" + date + ", amount=" + amount + ", period=" + period + ", startDate=" + startDate
                + ", dueDate=" + dueDate + ", breakdown=" + Arrays.toString(breakdown) + ", due=" + due + ", paid=" + paid + ", waived="
                + waived + ", writtenOff=" + writtenOff + ", early=" + early + ", late=" + late + ", feesBreakdown="
                + Arrays.toString(feesBreakdown) + ", penaltyBreakdown=" + Arrays.toString(penaltyBreakdown) + ", transactionsId="
                + Arrays.toString(transactionsId) + ", obligationMet=" + obligationMet + ", obligationMetOnDate=" + obligationMetOnDate
                + ", disbursementFees=" + disbursementFees + ", outstandingPrincipalBalance=" + outstandingPrincipalBalance + '}';
    }

}
