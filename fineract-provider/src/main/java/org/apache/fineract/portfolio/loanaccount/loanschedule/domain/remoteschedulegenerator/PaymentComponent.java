package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentComponent {

    Penalty("Penalty"), Fee("Fee"), Interest("Interest"), Principal("Principal");

    private final String value;

    private PaymentComponent(String value) {
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