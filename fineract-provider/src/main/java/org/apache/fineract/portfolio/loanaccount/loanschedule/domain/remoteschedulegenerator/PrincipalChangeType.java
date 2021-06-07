package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PrincipalChangeType {

    LoanStart("LoanStart"), Disbursement("Disbursement"), Repayment("Repayment");

    private final String value;

    private PrincipalChangeType(String value) {
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
