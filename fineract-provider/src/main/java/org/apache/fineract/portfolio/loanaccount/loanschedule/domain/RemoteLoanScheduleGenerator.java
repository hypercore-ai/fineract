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

import java.math.MathContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.charge.domain.ChargeCalculationType;
import org.apache.fineract.portfolio.charge.domain.ChargeTimeType;
import org.apache.fineract.portfolio.loanaccount.data.HolidayDetailDTO;
import org.apache.fineract.portfolio.loanaccount.data.LoanTermVariationsData;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCharge;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.domain.transactionprocessor.LoanRepaymentScheduleTransactionProcessor;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleDTO;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Fee;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.FeeCalculation;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.FeeTiming;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Frequency;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.GracePeriod;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Installment;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InstallmentComponent;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InstallmentType;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.InterestCalculationMethod;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Period;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Rate;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleRequest;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.RemoteScheduleResponse;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.TermVariation;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator.Transaction;
import org.apache.fineract.portfolio.loanproduct.domain.InterestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class RemoteLoanScheduleGenerator implements LoanScheduleGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteLoanScheduleGenerator.class);
    private final RestTemplateBuilder builder = new RestTemplateBuilder();

    @Override
    public LoanScheduleModel generate(MathContext mc, LoanApplicationTerms loanApplicationTerms, Set<LoanCharge> loanCharges,
            HolidayDetailDTO holidayDetailDTO, boolean scheduleWithNoDisbursements) {
        LOG.info("Calling");
        RemoteScheduleResponse response = this.builder.build().postForObject("http://localhost:5000/generateSchedule",
                createRequest(loanApplicationTerms, loanCharges), RemoteScheduleResponse.class);

        if (response != null) {
            LOG.info("Got remote generate response: installments:" + response.getInstallments().length);
            return RemoteGeneratorResultConverter.remoteToScheduleModel(response, loanApplicationTerms);
        } else {
            LOG.error("Error getting remote generate response");
            return null;
        }
    }

    @Override
    public LoanScheduleDTO rescheduleNextInstallments(MathContext mc, LoanApplicationTerms loanApplicationTerms, Loan loan,
            HolidayDetailDTO holidayDetailDTO, LoanRepaymentScheduleTransactionProcessor loanRepaymentScheduleTransactionProcessor,
            LocalDate rescheduleFrom) {
        LOG.info("Calling");
        RemoteScheduleRequest request = createRequest(loanApplicationTerms, loan.charges());

        request.setTransactions(loan.getLoanTransactions().stream().map(transaction -> {
            Transaction requestTransaction = new Transaction();

            if (transaction.getId() != null) {
                requestTransaction.setId(transaction.getId().toString());
            }
            requestTransaction.setType(transaction.getTypeOf());
            requestTransaction.setDate(transaction.getDateOf().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            requestTransaction.setAmount(transaction.getAmount(loanApplicationTerms.getCurrency()).getAmount().doubleValue());
            requestTransaction.setCanceled(transaction.isReversed());

            if (transaction.isManualRepayment()) {
                InstallmentComponent distribution = new InstallmentComponent();
                distribution.setPrincipal(transaction.getManualPrincipalPortion().doubleValue());
                distribution.setInterest(transaction.getManualInterestPortion().doubleValue());
                distribution.setFee(transaction.getManualFeeChargesPortion().doubleValue());
                distribution.setPenalty(transaction.getManualPenaltyChargesPortion().doubleValue());
                requestTransaction.setDistribution(distribution);
            }

            requestTransaction.setWaive(transaction.isWaiver());
            return requestTransaction;
        }).toArray(Transaction[]::new));

        if (loan.getLoanTermVariations().size() > 0) {
            Stream<TermVariation> currentTermVariations = Arrays
                    .stream(request.getTermVariations() != null ? request.getTermVariations() : new TermVariation[] {});
            Stream<TermVariation> additionalTermVariations = loan.getLoanTermVariations().stream()
                    .map(termVariation -> termVariation.toData()).map(this::loanTermVariationDataToTermVariation);

            request.setTermVariations(Stream.concat(additionalTermVariations, currentTermVariations).toArray(TermVariation[]::new));
        }

        RemoteScheduleResponse response = this.builder.build().postForObject("http://localhost:5000/generateSchedule", request,
                RemoteScheduleResponse.class);

        if (response != null) {
            LOG.info("Got remote generate response: " + response);
            return RemoteGeneratorResultConverter.remoteToLoanSchedule(response, loanApplicationTerms);
        } else {
            LOG.error("Error getting remote generate response");
            return null;
        }
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
        Money approvedPrincipal = loanApplicationTerms.getApprovedPrincipal().isZero() ? loanApplicationTerms.getOriginalProposedPrincipal()
                : loanApplicationTerms.getApprovedPrincipal();
        request.setApprovedAmount(approvedPrincipal.getAmount().doubleValue());
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
                    && loanApplicationTerms.getApprovedPrincipal().isGreaterThanZero() ? loanApplicationTerms.getApprovedPrincipal()
                            : loanApplicationTerms.getPrincipal();
            disbursement.setAmount(principal.getAmount().doubleValue());
            disbursement.setDate(loanApplicationTerms.getExpectedDisbursementDate());
            request.setDisbursements(new Installment[] { disbursement });
        }

        Frequency repaymentFrequency = createRepaymentFrequency(loanApplicationTerms, startDate);
        request.setPrincipalRepaymentFrequency(repaymentFrequency);
        request.setInterestRepaymentFrequency(repaymentFrequency);

        request.setInterestRates(loanApplicationTerms.getLoanTermVariations().getInterestRateChanges().stream().map(change -> {
            Rate rate = new Rate();
            rate.setDate(change.getTermApplicableFrom());
            rate.setValue(change.getDecimalValue().doubleValue());
            return rate;
        }).toArray(Rate[]::new));
        Integer daysInYear = loanApplicationTerms.getDaysInYearType().getValue();
        request.setDaysInYear(daysInYear == 1 ? "actual" : Integer.toString(daysInYear));
        request.setDaysInMonth("30"); // TODO: Get days in the month from loan product

        request.setInterestCalculationMethod(
                loanApplicationTerms.getInterestMethod() == InterestMethod.DECLINING_BALANCE ? InterestCalculationMethod.DecliningBalance
                        : InterestCalculationMethod.Flat);

        request.setFees(loanCharges.stream().filter(charge -> !charge.isPenaltyCharge()).map(charge -> {
            Fee fee = new Fee();
            if (charge.getId() != null) {
                fee.setId(charge.getId().toString());
            }

            fee.setPenalty(charge.isPenaltyCharge());
            fee.setCalculationType(this.chargeCalculationTypeToFeeCalculation(charge.getChargeCalculation()));
            fee.setValue(charge.amountOrPercentage().doubleValue());

            ChargeTimeType chargeTimeType = ChargeTimeType.fromInt(charge.getCharge().getChargeTimeType());
            FeeTiming timing = chargeTimeTypeToFeeTiming(chargeTimeType);
            fee.setTiming(timing);

            // TODO: handle other frequencies

            if (timing == FeeTiming.SpecificDate) {
                fee.setDate(charge.getDueLocalDate());
            }

            if (charge.getChargeCalculation() == ChargeCalculationType.PERCENT_OF_UNUTILIZED_AMOUNT) {
                Period period = new Period();
                LocalDate revolvingPeriodStartDate = loanApplicationTerms.getRevolvingPeriodStartDate();
                if (revolvingPeriodStartDate == null) {
                    period.setStartDate(loanApplicationTerms.getSeedDate());
                } else {
                    period.setStartDate(revolvingPeriodStartDate);
                    period.setEndDate(loanApplicationTerms.getRevolvingPeriodEndDate());
                }
                fee.setPeriod(period);
            }

            return fee;
        }).toArray(Fee[]::new));

        if (loanApplicationTerms.getPrincipalGrace() != null && loanApplicationTerms.getPrincipalGrace() != 0) {
            GracePeriod principalGrace = new GracePeriod();
            principalGrace.setFirstPeriod(1);
            principalGrace.setLastPeriod(loanApplicationTerms.getPrincipalGrace());
            request.setPrincipalGracePeriods(new GracePeriod[] { principalGrace });
        }

        if (loanApplicationTerms.getInterestPaymentGrace() != null && loanApplicationTerms.getInterestPaymentGrace() != 0) {
            GracePeriod interestGrace = new GracePeriod();
            interestGrace.setFirstPeriod(1);
            interestGrace.setLastPeriod(loanApplicationTerms.getInterestPaymentGrace());
            request.setInterestGracePeriods(new GracePeriod[] { interestGrace });
        }

        List<LoanTermVariationsData> exceptionData = loanApplicationTerms.getLoanTermVariations().getExceptionData();
        if (exceptionData.size() > 0) {
            request.setTermVariations(exceptionData.stream().map(this::loanTermVariationDataToTermVariation).toArray(TermVariation[]::new));
        }

        return request;
    }

    private Frequency createRepaymentFrequency(LoanApplicationTerms loanApplicationTerms, LocalDate startDate) {
        Frequency repaymentFrequency = new Frequency();
        repaymentFrequency.setStartDate(startDate);
        repaymentFrequency.setEvery(loanApplicationTerms.getRepaymentPeriodFrequencyType());
        repaymentFrequency.setEveryMultiplier(loanApplicationTerms.getRepaymentEvery());

        LocalDate repaymentStartDate = loanApplicationTerms.getRepaymentsStartingFromLocalDate();
        if (repaymentStartDate != null) {
            repaymentFrequency.setDaysInEvery(Integer.toString(repaymentStartDate.getDayOfMonth()));
        }
        repaymentFrequency.setRepetitions(loanApplicationTerms.getActualNoOfRepaymnets());
        return repaymentFrequency;
    }

    private TermVariation loanTermVariationDataToTermVariation(LoanTermVariationsData termVariation) {
        TermVariation requestVariation = new TermVariation();

        requestVariation.setType(termVariation.getTermVariationType());
        requestVariation.setStartDate(termVariation.getTermApplicableFrom());
        requestVariation.setEndDate(termVariation.getEndDate());
        requestVariation.setNewValue(termVariation.getDecimalValue().doubleValue());
        return requestVariation;
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

    private FeeTiming chargeTimeTypeToFeeTiming(ChargeTimeType chargeTimeType) {
        switch (chargeTimeType) {
            case DISBURSEMENT:
                return FeeTiming.OnFirstDisbursement;
            case TRANCHE_DISBURSEMENT:
                return FeeTiming.OnEveryDisbursement;
            case INSTALMENT_FEE:
            case REVOLVING_PERIOD_INSTALLMENT_FEE:
                return FeeTiming.OnInstallment;
            case SPECIFIED_DUE_DATE:
                return FeeTiming.SpecificDate;
            default:
                return null;
        }
    }
}
