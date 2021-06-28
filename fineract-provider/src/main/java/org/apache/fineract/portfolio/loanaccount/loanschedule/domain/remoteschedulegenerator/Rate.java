package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Rate {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private double value;

    public Rate() {
        super();
    }

    public LocalDate getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
