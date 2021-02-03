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
package org.apache.fineract.portfolio.loanaccount.loanschedule.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.loanaccount.domain.LoanInterestRecalcualtionAdditionalDetails;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanSchedulePeriodData;

/**
 * Domain representation of a Loan Schedule Repayment Sub Period (not used for persistence)
 */
public final class LoanScheduleModelRepaymentSubPeriod implements LoanScheduleModelPeriod {

    private final int periodNumber;
    private final int subPeriodNumber;
    private final LocalDate fromDate;
    private final LocalDate dueDate;
    private final Money outstandingLoanBalance;
    private Money interestDue;
    private final Set<LoanInterestRecalcualtionAdditionalDetails> loanCompoundingDetails = new HashSet<>();
    private boolean isEMIFixedSpecificToInstallment = false;
    BigDecimal rescheduleInterestPortion;

    public static LoanScheduleModelRepaymentSubPeriod repayment(final int periodNumber, final int subPeriodNumber,
            final LocalDate startDate, final LocalDate scheduledDueDate, final Money outstandingLoanBalance, final Money interestDue) {

        return new LoanScheduleModelRepaymentSubPeriod(periodNumber, subPeriodNumber, startDate, scheduledDueDate, outstandingLoanBalance,
                interestDue);
    }

    public LoanScheduleModelRepaymentSubPeriod(final int periodNumber, final int subPeriodNumber, final LocalDate fromDate,
            final LocalDate dueDate, final Money outstandingLoanBalance, final Money interestDue) {
        this.periodNumber = periodNumber;
        this.subPeriodNumber = subPeriodNumber;
        this.fromDate = fromDate;
        this.dueDate = dueDate;
        this.outstandingLoanBalance = outstandingLoanBalance;
        this.interestDue = interestDue;
    }

    @Override
    public LoanSchedulePeriodData toData() {
        return LoanSchedulePeriodData.repaymentSubPeriod(this.periodNumber, this.subPeriodNumber, this.fromDate, this.dueDate,
                this.outstandingLoanBalance.getAmount(), this.interestDue.getAmount());
    }

    @Override
    public boolean isRepaymentPeriod() {
        return false;
    }

    @Override
    public boolean isRepaymentSubPeriod() {
        return true;
    }

    @Override
    public Integer periodNumber() {
        return this.periodNumber;
    }

    @Override
    public Integer subPeriodNumber() {
        return this.subPeriodNumber;
    }

    @Override
    public LocalDate periodFromDate() {
        return this.fromDate;
    }

    @Override
    public LocalDate periodDueDate() {
        return this.dueDate;
    }

    @Override
    public BigDecimal principalDue() {
        return null;
    }

    @Override
    public BigDecimal interestDue() {
        BigDecimal value = null;
        if (this.interestDue != null) {
            value = this.interestDue.getAmount();
        }

        return value;
    }

    @Override
    public BigDecimal feeChargesDue() {
        return null;
    }

    @Override
    public BigDecimal penaltyChargesDue() {
        return null;
    }

    @Override
    public void addLoanCharges(BigDecimal feeCharge, BigDecimal penaltyCharge) {}

    @Override
    public void addPrincipalAmount(final Money principalDue) {}

    @Override
    public boolean isRecalculatedInterestComponent() {
        return true;
    }

    @Override
    public void addInterestAmount(Money interestDue) {
        this.interestDue = this.interestDue.plus(interestDue);
    }

    @Override
    public Set<LoanInterestRecalcualtionAdditionalDetails> getLoanCompoundingDetails() {
        return this.loanCompoundingDetails;
    }

    @Override
    public boolean isEMIFixedSpecificToInstallment() {
        return this.isEMIFixedSpecificToInstallment;
    }

    @Override
    public void setEMIFixedSpecificToInstallmentTrue() {
        this.isEMIFixedSpecificToInstallment = true;
    }

    @Override
    public void setRescheduleInterestPortion(BigDecimal rescheduleInterestPortion) {
        this.rescheduleInterestPortion = rescheduleInterestPortion;
    }

    @Override
    public BigDecimal rescheduleInterestPortion() {
        return this.rescheduleInterestPortion;
    }
}
