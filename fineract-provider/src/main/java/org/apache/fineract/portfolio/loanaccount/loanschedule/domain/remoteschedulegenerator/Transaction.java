package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionType;

public class Transaction {

    private String id;
    private LocalDate date;
    private double amount;
    private LoanTransactionType type;
    private InstallmentComponent distribution;
    private boolean canceled;
    private boolean isWaive;
    private FeeBreakdown[] feesBreakdown;

    public Transaction() {
        super();
    }

    public FeeBreakdown[] getFeesBreakdown() {
        return feesBreakdown;
    }

    public void setFeesBreakdown(FeeBreakdown[] feesBreakdown) {
        this.feesBreakdown = feesBreakdown;
    }

    public String getId() {
        return id;
    }

    public boolean isWaive() {
        return isWaive;
    }

    public void setWaive(boolean isWaive) {
        this.isWaive = isWaive;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public InstallmentComponent getDistribution() {
        return distribution;
    }

    public void setDistribution(InstallmentComponent distribution) {
        this.distribution = distribution;
    }

    public LoanTransactionType getType() {
        return type;
    }

    public void setType(LoanTransactionType type) {
        this.type = type;
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

    public void setId(String id) {
        this.id = id;
    }
}
