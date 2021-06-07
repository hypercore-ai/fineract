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

import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
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
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Rate;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleRequest;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleResponse;
import org.apache.fineract.portfolio.loanproduct.domain.InterestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.math.MathContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

public class RemoteLoanScheduleGenerator implements LoanScheduleGenerator {
  private static final Logger LOG = LoggerFactory.getLogger(RemoteLoanScheduleGenerator.class);

  @Override
  public LoanScheduleModel generate(MathContext mc, LoanApplicationTerms loanApplicationTerms,
      Set<LoanCharge> loanCharges, HolidayDetailDTO holidayDetailDTO, boolean scheduleWithNoDisbursements) {
    // TODO Auto-generated method stub
    RestTemplateBuilder builder = new RestTemplateBuilder();
    LOG.info("Calling");
    RemoteScheduleResponse response = builder.build().postForObject("http://localhost:5000/generateSchedule",
        createRequest(loanApplicationTerms, loanCharges), RemoteScheduleResponse.class);

    if (response != null) {
      LOG.info("Got remote generate response: " + response);
      return RemoteGeneratorResultParser.parseRemoteSchedule(response, loanApplicationTerms);
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

  private RemoteScheduleRequest createRequest(LoanApplicationTerms loanApplicationTerms, Set<LoanCharge> loanCharges) {
    RemoteScheduleRequest request = new RemoteScheduleRequest();

    LocalDate startDate = loanApplicationTerms.getExpectedDisbursementDate();
    request.setStartDate(startDate);
    request.setApprovedAmount(loanApplicationTerms.getApprovedPrincipal().getAmount().doubleValue());
    request.setAmortization(loanApplicationTerms.getAmortizationMethod());
    
    if (loanApplicationTerms.isMultiDisburseLoan()) {
      request.setDisbursements(loanApplicationTerms.getDisbursementDatas().stream().map(datum -> {
        Installment disbursement = new Installment();
        disbursement.setType(InstallmentType.DISBURSEMENT);
        disbursement.setAmount(datum.amount().doubleValue());
        disbursement.setDate(datum.getExpectedDisbursementDate());
        return disbursement;
      }).toArray(Installment[]::new));
    } else {
      Installment disbursement = new Installment();
      disbursement.setType(InstallmentType.DISBURSEMENT);
      Money principal = loanApplicationTerms.getApprovedPrincipal() != null
          ? loanApplicationTerms.getApprovedPrincipal()
          : loanApplicationTerms.getPrincipal();
      disbursement.setAmount(principal.getAmount().doubleValue());
      disbursement.setDate(loanApplicationTerms.getExpectedDisbursementDate());
      request.setDisbursements(new Installment[] { disbursement });
    }

    Frequency repaymentFrequency = createRepaymentFrequency(loanApplicationTerms, startDate);
    request.setPrincipalRepaymentFrequency(repaymentFrequency);
    request.setInterestRepaymentFrequency(repaymentFrequency);

    request
        .setInterestRates(loanApplicationTerms.getLoanTermVariations().getInterestRateChanges().stream().map(change -> {
          Rate rate = new Rate();
          rate.setDate(change.getTermApplicableFrom());
          rate.setValue(change.getDecimalValue().doubleValue());
          return rate;
        }).toArray(Rate[]::new));
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
    repaymentFrequency.setRepetitions(loanApplicationTerms.getActualNoOfRepaymnets());
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
