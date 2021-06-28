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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleDTO;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InstallmentType;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InterestBreakdown;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleResponse;

public class RemoteGeneratorResultConverter {

    private static Money money(MonetaryCurrency currency, double amount) {
        return Money.of(currency, BigDecimal.valueOf(amount));
    }

    public static LoanScheduleModel remoteToScheduleModel(RemoteScheduleResponse remoteSchedule,
            LoanApplicationTerms loanApplicationTerms) {
        MonetaryCurrency currency = loanApplicationTerms.getCurrency();
        Collection<LoanScheduleModelPeriod> periods = new ArrayList<>();

        Arrays.stream(remoteSchedule.getInstallments()).forEach((installment) -> {
            if (installment.getType() == InstallmentType.DISBURSEMENT) {
                Money principalDisbursed = Money.of(currency, BigDecimal.valueOf(installment.getAmount()));
                BigDecimal disbursementFees = BigDecimal.valueOf(installment.getDisbursementFees());

                periods.add(LoanScheduleModelDisbursementPeriod.disbursement(installment.getDate(), principalDisbursed, disbursementFees));

            } else if (installment.getType() == InstallmentType.INSTALLMENT) {
                Money outstandingLoanBalance = money(currency, installment.getOutstandingPrincipalBalance());
                Money principalDue = money(currency, installment.getDue().getPrincipal());
                Money interestDue = money(currency, installment.getDue().getInterest());
                Money feeChargesDue = money(currency, installment.getDue().getFee());
                Money penaltyChargesDue = money(currency, installment.getDue().getPenalty());
                Money totalDue = principalDue.plus(interestDue).plus(feeChargesDue).plus(penaltyChargesDue);
                boolean recalculatedInterestComponent = false; // TODO validate (seems it is related to compelete
                // payment)

                LoanScheduleModelRepaymentPeriod repayment = LoanScheduleModelRepaymentPeriod.repayment(installment.getPeriod(),
                        installment.getStartDate(), installment.getDueDate(), principalDue, outstandingLoanBalance, interestDue,
                        feeChargesDue, penaltyChargesDue, totalDue, recalculatedInterestComponent);

                periods.add(repayment);

                InterestBreakdown[] interestBreakdowns = installment.getBreakdown();
                if (interestBreakdowns != null) {
                    Collection<LoanScheduleModelPeriod> interestSubPeriods = IntStream.range(0, interestBreakdowns.length)
                            .mapToObj(interestIndex -> {
                                InterestBreakdown interestBreakdown = interestBreakdowns[interestIndex];
                                // TODO validate with tomer
                                double outstandingBalance = interestBreakdown.getPrincipalBalance();
                                return LoanScheduleModelRepaymentSubPeriod.repayment(installment.getPeriod(), interestIndex + 1,
                                        interestBreakdown.getStartDate(), interestBreakdown.getEndDate(),
                                        money(currency, outstandingBalance), money(currency, interestBreakdown.getInterestDue()));
                            }).collect(Collectors.toList());

                    // TODO validate - this code is in Abstract Generator
                    // for (LoanScheduleModelPeriod variationSubPeriod : variationSubPeriods) {
                    // addLoanRepaymentScheduleInstallment(scheduleParams.getInstallments(), variationSubPeriod);
                    // }
                    periods.addAll(interestSubPeriods);
                }
            }
        });

        int loanTermInDays = periods.stream().filter(period -> period instanceof LoanScheduleModelRepaymentPeriod)
                .mapToInt(period -> Math.toIntExact(ChronoUnit.DAYS.between(period.periodFromDate(), period.periodDueDate()))).sum();

        Money principalDisbursed = money(currency, remoteSchedule.getTotalPrincipalDisbursed());
        BigDecimal totalPrincipalExpected = BigDecimal.valueOf(remoteSchedule.getTotalPrincipalExpected());
        BigDecimal totalPrincipalPaid = BigDecimal.valueOf(remoteSchedule.getTotalPrincipalPaid());
        BigDecimal totalInterestCharged = BigDecimal.valueOf(remoteSchedule.getTotalInterestCharged());
        BigDecimal totalFeeChargesCharged = BigDecimal.valueOf(remoteSchedule.getTotalFeeChargesCharged());
        BigDecimal totalPenaltyChargesCharged = BigDecimal.valueOf(remoteSchedule.getTotalPenaltyChargesCharged());
        BigDecimal totalRepaymentExpected = BigDecimal.valueOf(remoteSchedule.getTotalRepaymentExpected());
        BigDecimal totalOutstanding = BigDecimal.valueOf(remoteSchedule.getTotalOutstanding());

        return LoanScheduleModel.from(periods, loanApplicationTerms.getApplicationCurrency(), loanTermInDays, principalDisbursed,
                totalPrincipalExpected, totalPrincipalPaid, totalInterestCharged, totalFeeChargesCharged, totalPenaltyChargesCharged,
                totalRepaymentExpected, totalOutstanding);

    }

    public static LoanScheduleDTO remoteToLoanSchedule(RemoteScheduleResponse response, LoanApplicationTerms loanApplicationTerms) {
        LoanScheduleModel loanScheduleModel = remoteToScheduleModel(response, loanApplicationTerms);
        final List<LoanRepaymentScheduleInstallment> installments = loanScheduleModel.getPeriods().stream()
                .filter(LoanScheduleModelPeriod::isRepaymentPeriod)
                .map(period -> new LoanRepaymentScheduleInstallment(null, period.periodNumber(), period.subPeriodNumber(),
                        period.periodFromDate(), period.periodDueDate(), period.principalDue(), period.interestDue(),
                        period.feeChargesDue(), period.penaltyChargesDue(), period.isRecalculatedInterestComponent(),
                        period.getLoanCompoundingDetails(), period.rescheduleInterestPortion()))
                .collect(Collectors.toList());

        return LoanScheduleDTO.from(installments, loanScheduleModel);
    }
}
