/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.loanaccount.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTermVariationType;

public class LoanTermVariationsData implements Comparable<LoanTermVariationsData> {

    @SuppressWarnings("unused")
    private final Long id;
    private final EnumOptionData termType;
    private LocalDate termVariationApplicableFrom;
    private LocalDate endDate;
    private LocalDateTime createdDate;
    private final BigDecimal decimalValue;
    private final LocalDate dateValue;
    private final boolean isSpecificToInstallment;
    private Boolean isProcessed;

    public LoanTermVariationsData(final Long id, final EnumOptionData termType, final LocalDate termVariationApplicableFrom,
            final BigDecimal decimalValue, final LocalDate dateValue, final boolean isSpecificToInstallment, final LocalDate endDate,
            final LocalDateTime createdDate) {
        this.id = id;
        this.termType = termType;
        this.termVariationApplicableFrom = termVariationApplicableFrom;
        this.decimalValue = decimalValue;
        this.dateValue = dateValue;
        this.isSpecificToInstallment = isSpecificToInstallment;
        this.endDate = endDate;
        this.createdDate = createdDate;
    }

    public LoanTermVariationsData(final EnumOptionData termType, final LocalDate termVariationApplicableFrom, final BigDecimal decimalValue,
            LocalDate dateValue, final boolean isSpecificToInstallment) {
        this.id = null;
        this.termType = termType;
        this.termVariationApplicableFrom = termVariationApplicableFrom;
        this.decimalValue = decimalValue;
        this.dateValue = dateValue;
        this.isSpecificToInstallment = isSpecificToInstallment;
    }

    public EnumOptionData getTermType() {
        return this.termType;
    }

    public LoanTermVariationType getTermVariationType() {
        return LoanTermVariationType.fromInt(this.termType.getId().intValue());
    }

    public LocalDate getTermApplicableFrom() {
        return this.termVariationApplicableFrom;
    }

    public BigDecimal getDecimalValue() {
        return this.decimalValue;
    }

    public boolean isApplicable(final LocalDate fromDate, final LocalDate dueDate) {
        return occursOnDayFromAndUpTo(fromDate, dueDate);
    }

    public boolean isDateRangeContained(LocalDate startDate, LocalDate endDate) {
        if (this.endDate == null || this.termVariationApplicableFrom == null) {
            return false;
        }

        return !this.termVariationApplicableFrom.isAfter(startDate) && !this.endDate.isBefore(endDate);
    }

    public boolean isDateContained(LocalDate date) {
        if (this.endDate == null || this.termVariationApplicableFrom == null) {
            return false;
        }

        return !this.termVariationApplicableFrom.isAfter(date) && !this.endDate.isBefore(date);
    }

    private boolean occursOnDayFromAndUpTo(final LocalDate fromNotInclusive, final LocalDate upToInclusive) {
        return this.termVariationApplicableFrom != null && this.termVariationApplicableFrom.isAfter(fromNotInclusive)
                && !this.termVariationApplicableFrom.isAfter(upToInclusive)
                && (this.endDate == null || (this.endDate.isAfter(fromNotInclusive) && !this.endDate.isAfter(upToInclusive)));
    }

    public boolean isApplicable(final LocalDate fromDate) {
        return occursBefore(fromDate);
    }

    private boolean occursBefore(final LocalDate date) {
        return this.termVariationApplicableFrom != null && !this.termVariationApplicableFrom.isAfter(date);
    }

    public LocalDate getDateValue() {
        return this.dateValue;
    }

    public boolean isSpecificToInstallment() {
        return this.isSpecificToInstallment;
    }

    public Boolean isProcessed() {
        return this.isProcessed == null ? false : this.isProcessed;
    }

    public void setProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    @Override
    public int compareTo(LoanTermVariationsData o) {
        int comparsion = getTermApplicableFrom().compareTo(o.getTermApplicableFrom());
        if (comparsion == 0) {
            if (o.getTermVariationType().isDueDateVariation() || o.getTermVariationType().isInsertInstallment()) {
                comparsion = 1;
            }
        }
        return comparsion;
    }

    public void setApplicableFromDate(final LocalDate applicableFromDate) {
        this.termVariationApplicableFrom = applicableFromDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

}
