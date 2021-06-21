package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

public class Fee {

    private String id;
    private FeeTiming timing;
    private Frequency frequency;
    private LocalDate date;
    private FeeCalculation calculationType;
    private double value;
    private Period period;
    private boolean isPenalty;

    public Fee() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getId() {
        return id;
    }

    public boolean isPenalty() {
        return isPenalty;
    }

    public void setPenalty(boolean isPenalty) {
        this.isPenalty = isPenalty;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public FeeCalculation getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(FeeCalculation calculationType) {
        this.calculationType = calculationType;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency timingFrequency) {
        this.frequency = timingFrequency;
    }

    public FeeTiming getTiming() {
        return timing;
    }

    public void setTiming(FeeTiming timing) {
        this.timing = timing;
    }

    public void setId(String id) {
        this.id = id;
    }

}
