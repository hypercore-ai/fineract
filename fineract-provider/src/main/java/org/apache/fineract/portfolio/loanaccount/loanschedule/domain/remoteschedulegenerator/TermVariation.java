package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTermVariationType;

public class TermVariation {

    private String id;
    private LoanTermVariationType type;
    private LocalDate startDate;
    private LocalDate endDate;
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

    public LoanTermVariationType getType() {
        return type;
    }

    public void setType(LoanTermVariationType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }
}
