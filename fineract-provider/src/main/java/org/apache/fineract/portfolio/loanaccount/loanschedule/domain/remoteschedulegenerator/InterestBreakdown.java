package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

public class InterestBreakdown {

    private int subPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private double principalBalance;
    private double interestDue;
    private double interestPercentage;

    public InterestBreakdown() {
        super();
    }

    public int getSubPeriod() {
        return subPeriod;
    }

    public double getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(double interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public double getInterestDue() {
        return interestDue;
    }

    public void setInterestDue(double interestDue) {
        this.interestDue = interestDue;
    }

    public double getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(double principalBalance) {
        this.principalBalance = principalBalance;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setSubPeriod(int subPeriod) {
        this.subPeriod = subPeriod;
    }
}
