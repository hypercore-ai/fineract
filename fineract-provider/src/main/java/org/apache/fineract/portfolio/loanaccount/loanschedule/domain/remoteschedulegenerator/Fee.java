package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class Fee {

    private String id;
    private FeeTiming timing;
    private Frequency timingFrequency;
    private FeeCalculation calculationType;
    private double value;
    private Period period;
    private boolean isPenalty;

    public Fee() {}

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

    public Frequency getTimingFrequency() {
        return timingFrequency;
    }

    public void setTimingFrequency(Frequency timingFrequency) {
        this.timingFrequency = timingFrequency;
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
