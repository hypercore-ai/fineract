package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTermVariationType;

public class TermVariation {

    private String id;
    private LoanTermVariationType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private LocalDateTime createdDate;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
