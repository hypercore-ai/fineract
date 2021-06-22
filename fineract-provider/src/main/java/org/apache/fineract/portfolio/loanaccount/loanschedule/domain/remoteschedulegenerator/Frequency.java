package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;
import org.apache.fineract.portfolio.common.domain.PeriodFrequencyType;

public class Frequency {

    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodFrequencyType every;
    private int everyMultiplier;
    private String daysInEvery;
    private int repetitions;

    public Frequency() {}

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String getDaysInEvery() {
        return daysInEvery;
    }

    public void setDaysInEvery(String daysInEvery) {
        this.daysInEvery = daysInEvery;
    }

    public int getEveryMultiplier() {
        return everyMultiplier;
    }

    public void setEveryMultiplier(int everyMultiplier) {
        this.everyMultiplier = everyMultiplier;
    }

    public PeriodFrequencyType getEvery() {
        return every;
    }

    public void setEvery(PeriodFrequencyType every) {
        this.every = every;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

}
