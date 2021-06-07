package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public enum ChargeAppliesOn {

    Disbursement("Disbursement"), Overdue("Overdue"), RevolvingPeriodInstallment("RevolvingPeriodInstallment");

    private final String value;

    private ChargeAppliesOn(String value) {
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
