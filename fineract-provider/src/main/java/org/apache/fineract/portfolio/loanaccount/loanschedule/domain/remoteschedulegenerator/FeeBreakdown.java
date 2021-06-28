package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class FeeBreakdown {

    private String externalId;
    private double amountDue;
    private double amountPaid;

    public FeeBreakdown() {
        super();
    }

    public String getExternalId() {
        return externalId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
