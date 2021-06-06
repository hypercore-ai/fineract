/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.loanaccount.loanschedule.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.loanaccount.data.HolidayDetailDTO;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCharge;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.domain.transactionprocessor.LoanRepaymentScheduleTransactionProcessor;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleDTO;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InstallmentType;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class RemoteLoanScheduleGenerator implements LoanScheduleGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteLoanScheduleGenerator.class);

    @Override
    public LoanScheduleModel generate(MathContext mc, LoanApplicationTerms loanApplicationTerms,
                                      Set<LoanCharge> loanCharges, HolidayDetailDTO holidayDetailDTO, boolean scheduleWithNoDisbursements) {
        // TODO Auto-generated method stub
        RestTemplateBuilder builder = new RestTemplateBuilder();
        LOG.info("Calling");
        RemoteScheduleResponse response = builder.build().getForObject("https://testep.free.beeceptor.com/generate-schedule",
                RemoteScheduleResponse.class);

        if (response != null) {
          LOG.info("Got remote generate response: " + response);
          return this.parseRemoteSchedule(response,loanApplicationTerms);
        } else {
          LOG.error("Error getting remote generate response");
          return null;
        }

    }

    @Override
    public LoanScheduleDTO rescheduleNextInstallments(MathContext mc, LoanApplicationTerms loanApplicationTerms,
                                                      Loan loan, HolidayDetailDTO holidayDetailDTO,
                                                      LoanRepaymentScheduleTransactionProcessor loanRepaymentScheduleTransactionProcessor, LocalDate rescheduleFrom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LoanRepaymentScheduleInstallment calculatePrepaymentAmount(MonetaryCurrency currency, LocalDate onDate,
                                                                      LoanApplicationTerms loanApplicationTerms, MathContext mc, Loan loan, HolidayDetailDTO holidayDetailDTO,
                                                                      LoanRepaymentScheduleTransactionProcessor loanRepaymentScheduleTransactionProcessor) {
        // TODO Auto-generated method stub
        return null;
    }

    private LoanScheduleModel parseRemoteSchedule(RemoteScheduleResponse remoteSchedule, LoanApplicationTerms loanApplicationTerms) {
      MonetaryCurrency currency = loanApplicationTerms.getCurrency();

      Collection<LoanScheduleModelPeriod> periods = Arrays.stream(remoteSchedule.getInstallments()).map(installment -> {
            if (installment.getType() == InstallmentType.DISBURSEMENT) {
              Money principalDisbursed = Money.of(currency, BigDecimal.valueOf(installment.getAmount()));
              BigDecimal disbursementFees =  BigDecimal.valueOf(installment.getDisbursementFees());

              return LoanScheduleModelDisbursementPeriod.disbursement(installment.getDate(), principalDisbursed, disbursementFees);

            } else if (installment.getType() == InstallmentType.INSTALLMENT) {
                Money outstandingLoanBalance = Money.of(currency, BigDecimal.valueOf(installment.getOutstandingPrincipalBalance()));
                Money principalDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getPrincipal()));
                Money interestDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getInterest()));
                Money feeChargesDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getFee()));
                Money penaltyChargesDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getPenalty()));
                Money totalDue = principalDue.plus(interestDue).plus(feeChargesDue).plus(penaltyChargesDue);
                boolean recalculatedInterestComponent = false; // TODO validate what it does

                return LoanScheduleModelRepaymentPeriod.repayment(installment.getPeriod(), installment.getStartDate(), installment.getDueDate(), principalDue, outstandingLoanBalance,
                        interestDue, feeChargesDue, penaltyChargesDue, totalDue, recalculatedInterestComponent);
            }

            return null;
        }).collect(Collectors.toList());
        int loanTermInDays = periods.stream().mapToInt(period -> Math.toIntExact(ChronoUnit.DAYS.between(period.periodFromDate(), period.periodDueDate()))).sum();

        Money principalDisbursed = Money.of(currency, BigDecimal.valueOf(remoteSchedule.getTotalPrincipalDisbursed()));
        BigDecimal totalPrincipalExpected = BigDecimal.valueOf(remoteSchedule.getTotalPrincipalExpected());
        BigDecimal totalPrincipalPaid = BigDecimal.valueOf(remoteSchedule.getTotalPrincipalPaid());
        BigDecimal totalInterestCharged = BigDecimal.valueOf(remoteSchedule.getTotalInterestCharged());
        BigDecimal totalFeeChargesCharged = BigDecimal.valueOf(remoteSchedule.getTotalFeeChargesCharged());
        BigDecimal totalPenaltyChargesCharged = BigDecimal.valueOf(remoteSchedule.getTotalPenaltyChargesCharged());
        BigDecimal totalRepaymentExpected = BigDecimal.valueOf(remoteSchedule.getTotalRepaymentExpected());
        BigDecimal totalOutstanding = BigDecimal.valueOf(remoteSchedule.getTotalOutstanding());
        return LoanScheduleModel.from(periods, loanApplicationTerms.getApplicationCurrency(), loanTermInDays, principalDisbursed,
                totalPrincipalExpected, totalPrincipalPaid, totalInterestCharged, totalFeeChargesCharged, totalPenaltyChargesCharged, totalRepaymentExpected, totalOutstanding);

    }

}
