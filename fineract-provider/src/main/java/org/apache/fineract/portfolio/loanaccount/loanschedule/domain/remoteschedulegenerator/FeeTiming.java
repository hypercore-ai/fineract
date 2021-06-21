package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FeeTiming {

    Frequency("Frequency"), //
    OnFirstDisbursement("OnFirstDisbursement"), //
    OnEveryDisbursement("OnEveryDisbursement"), //
    OnInstallment("OnInstallment"), //
    SpecificDate("SpecificDate");

    private final String value;

    private FeeTiming(String value) {
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
