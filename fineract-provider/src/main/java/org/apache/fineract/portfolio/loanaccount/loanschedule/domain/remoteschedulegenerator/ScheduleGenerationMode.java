package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ScheduleGenerationMode {

    Expected("Expected"), Actual("Actual");

    private final String value;

    private ScheduleGenerationMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
