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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.charge.domain.ChargeCalculationType;
import org.apache.fineract.portfolio.common.domain.PeriodFrequencyType;
import org.apache.fineract.portfolio.loanaccount.data.HolidayDetailDTO;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCharge;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.domain.transactionprocessor.LoanRepaymentScheduleTransactionProcessor;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleDTO;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Fee;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.FeeCalculation;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Frequency;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Installment;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InstallmentType;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InterestCalculationMethod;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleRequest;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleResponse;
import org.apache.fineract.portfolio.loanproduct.domain.InterestMethod;
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
    RemoteScheduleResponse response = builder.build().postForObject(
        "https://testep.free.beeceptor.com/generate-schedule", createRequest(loanApplicationTerms, loanCharges),
        RemoteScheduleResponse.class);

    if (response != null) {
      LOG.info("Got remote generate response: " + response);
      return this.parseRemoteSchedule(response, loanApplicationTerms);
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

  private LoanScheduleModel parseRemoteSchedule(RemoteScheduleResponse remoteSchedule,
      LoanApplicationTerms loanApplicationTerms) {
    MonetaryCurrency currency = loanApplicationTerms.getCurrency();

    Collection<LoanScheduleModelPeriod> periods = Arrays.stream(remoteSchedule.getInstallments()).map(installment -> {
      if (installment.getType() == InstallmentType.DISBURSEMENT) {
        Money principalDisbursed = Money.of(currency, BigDecimal.valueOf(installment.getAmount()));
        BigDecimal disbursementFees = BigDecimal.valueOf(installment.getDisbursementFees());

        return LoanScheduleModelDisbursementPeriod.disbursement(installment.getDate(), principalDisbursed,
            disbursementFees);

      } else if (installment.getType() == InstallmentType.INSTALLMENT) {
        Money outstandingLoanBalance = Money.of(currency,
            BigDecimal.valueOf(installment.getOutstandingPrincipalBalance()));
        Money principalDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getPrincipal()));
        Money interestDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getInterest()));
        Money feeChargesDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getFee()));
        Money penaltyChargesDue = Money.of(currency, BigDecimal.valueOf(installment.getDue().getPenalty()));
        Money totalDue = principalDue.plus(interestDue).plus(feeChargesDue).plus(penaltyChargesDue);
        boolean recalculatedInterestComponent = false; // TODO validate (seems it is related to compelete payment)

        return LoanScheduleModelRepaymentPeriod.repayment(installment.getPeriod(), installment.getStartDate(),
            installment.getDueDate(), principalDue, outstandingLoanBalance, interestDue, feeChargesDue,
            penaltyChargesDue, totalDue, recalculatedInterestComponent);
      }

      return null;
    }).collect(Collectors.toList());
    int loanTermInDays = periods.stream()
        .mapToInt(period -> Math.toIntExact(ChronoUnit.DAYS.between(period.periodFromDate(), period.periodDueDate())))
        .sum();

    Money principalDisbursed = Money.of(currency, BigDecimal.valueOf(remoteSchedule.getTotalPrincipalDisbursed()));
    BigDecimal totalPrincipalExpected = BigDecimal.valueOf(remoteSchedule.getTotalPrincipalExpected());
    BigDecimal totalPrincipalPaid = BigDecimal.valueOf(remoteSchedule.getTotalPrincipalPaid());
    BigDecimal totalInterestCharged = BigDecimal.valueOf(remoteSchedule.getTotalInterestCharged());
    BigDecimal totalFeeChargesCharged = BigDecimal.valueOf(remoteSchedule.getTotalFeeChargesCharged());
    BigDecimal totalPenaltyChargesCharged = BigDecimal.valueOf(remoteSchedule.getTotalPenaltyChargesCharged());
    BigDecimal totalRepaymentExpected = BigDecimal.valueOf(remoteSchedule.getTotalRepaymentExpected());
    BigDecimal totalOutstanding = BigDecimal.valueOf(remoteSchedule.getTotalOutstanding());
    return LoanScheduleModel.from(periods, loanApplicationTerms.getApplicationCurrency(), loanTermInDays,
        principalDisbursed, totalPrincipalExpected, totalPrincipalPaid, totalInterestCharged, totalFeeChargesCharged,
        totalPenaltyChargesCharged, totalRepaymentExpected, totalOutstanding);

  }

  private RemoteScheduleRequest createRequest(LoanApplicationTerms loanApplicationTerms, Set<LoanCharge> loanCharges) {
    RemoteScheduleRequest request = new RemoteScheduleRequest();

    LocalDate startDate = loanApplicationTerms.getExpectedDisbursementDate();
    request.setStartDate(startDate);
    request.setApprovedAmount(loanApplicationTerms.getApprovedPrincipal().getAmount().doubleValue());
    request.setAmortization(loanApplicationTerms.getAmortizationMethod());
    request.setDisbursements(loanApplicationTerms.getDisbursementDatas().stream().map(datum -> {
      Installment disbursement = new Installment();
      disbursement.setType(InstallmentType.DISBURSEMENT);
      disbursement.setAmount(datum.amount().doubleValue());
      disbursement.setDate(datum.getExpectedDisbursementDate());
      return disbursement;
    }).toArray(Installment[]::new));

    Frequency repaymentFrequency = createRepaymentFrequency(loanApplicationTerms, startDate);
    request.setPrincipalRepaymentFrequency(repaymentFrequency);
    request.setInterestRepaymentFrequency(repaymentFrequency);

    // request.setAnnualInterestRate(loanApplicationTerms.getIntere);
    Integer daysInYear = loanApplicationTerms.getDaysInYearType().getValue();
    request.setDaysInYear(daysInYear == 1 ? "actual" : Integer.toString(daysInYear));
    request.setDaysInMonth("30"); // TODO: Get days in the month from loan product

    request.setInterestCalculationMethod(loanApplicationTerms.getInterestMethod() == InterestMethod.DECLINING_BALANCE
        ? InterestCalculationMethod.DecliningBalance
        : InterestCalculationMethod.Flat);

    request.setFees(loanCharges.stream().filter(charge -> !charge.isPenaltyCharge()).map(charge -> {
      Fee fee = new Fee();
      if (charge.getId() != null) {
        fee.setId(charge.getId().toString());
      }
      fee.setPenalty(false);
      fee.setCalculationType(this.chargeCalculationTypeToFeeCalculation(charge.getChargeCalculation()));
      fee.setValue(charge.amount().doubleValue());
      return fee;
    }).toArray(Fee[]::new));

    return request;
  }

  private Frequency createRepaymentFrequency(LoanApplicationTerms loanApplicationTerms, LocalDate startDate) {
    Frequency repaymentFrequency = new Frequency();
    repaymentFrequency.setStartDate(startDate);
    repaymentFrequency
        .setEvery(this.frequencyEveryFromPeriodFrequencyType(loanApplicationTerms.getRepaymentPeriodFrequencyType()));
    repaymentFrequency.setEveryMultiplier(loanApplicationTerms.getRepaymentEvery());

    Date repaymentStartDate = loanApplicationTerms.getRepaymentStartFromDate();
    if (repaymentStartDate != null) {
      repaymentFrequency.setDaysInEvery(Integer
          .toString(repaymentStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()));
    }
    repaymentFrequency.setRepetitions(1);
    return repaymentFrequency;
  }

  private String frequencyEveryFromPeriodFrequencyType(PeriodFrequencyType frequencyType) {
    switch (frequencyType) {
      case DAYS:
        return "day";
      case WEEKS:
        return "week";
      case MONTHS:
        return "month";
      case YEARS:
        return "year";
      default:
        return "";
    }
  }

  private FeeCalculation chargeCalculationTypeToFeeCalculation(ChargeCalculationType calcType) {
    switch (calcType) {
      case FLAT:
        return FeeCalculation.Flat;
      case PERCENT_OF_AMOUNT:
        return FeeCalculation.PercentageOfApprovedAmount;
      case PERCENT_OF_INTEREST:
        return FeeCalculation.PercentageOfInstallmentInterest;
      case PERCENT_OF_AMOUNT_AND_INTEREST:
        return FeeCalculation.PercentageOfInstallmentPrincipalAndInterest;
      case PERCENT_OF_UNUTILIZED_AMOUNT:
        return FeeCalculation.AnnualPercentageOfUnutilizedAmount;
      default:
        return null;
    }
  }
}
