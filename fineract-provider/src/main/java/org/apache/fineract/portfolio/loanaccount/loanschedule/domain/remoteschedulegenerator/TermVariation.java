package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

public class TermVariation {

    private String id;
    private TermVariationType type;
    private String startDate;
    private String endDate;
    private LocalDate createdTimestamp;
    private double newValue;

    public TermVariation() {}

    public String getId() {
        return id;
    }

    public double getNewValue() {
        return newValue;
    }

    public void setNewValue(double newValue) {
        this.newValue = newValue;
    }

    public LocalDate getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDate createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public TermVariationType getType() {
        return type;
    }

    public void setType(TermVariationType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }
}
