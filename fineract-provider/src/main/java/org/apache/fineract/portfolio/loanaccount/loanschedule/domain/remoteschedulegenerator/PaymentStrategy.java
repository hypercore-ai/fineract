package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class PaymentStrategy {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    private Strategy[] strategy;

    public PaymentStrategy() {}

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Strategy[] getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy[] strategy) {
        this.strategy = strategy;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

}
