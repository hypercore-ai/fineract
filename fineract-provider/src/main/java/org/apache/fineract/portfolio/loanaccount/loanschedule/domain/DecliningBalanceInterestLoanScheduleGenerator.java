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
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.loanaccount.data.LoanTermVariationsData;
import org.apache.fineract.portfolio.loanproduct.domain.AmortizationMethod;

/**
 * <p>
 * Declining balance can be amortized (see {@link AmortizationMethod}) in two ways at present:
 * <ol>
 * <li>Equal principal payments</li>
 * <li>Equal installment payments</li>
 * </ol>
 * <p>
 * </p>
 *
 * <p>
 * When amortized using <i>equal principal payments</i>, the <b>principal component</b> of each installment is fixed and
 * <b>interest due</b> is calculated from the <b>outstanding principal balance</b> resulting in a different <b>total
 * payment due</b> for each installment.
 * </p>
 *
 * <p>
 * When amortized using <i>equal installments</i>, the <b>total payment due</b> for each installment is fixed and is
 * calculated using the excel like <code>pmt</code> function. The <b>interest due</b> is calculated from the
 * <b>outstanding principal balance</b> which results in a <b>principal component</b> that is <b>total payment due</b>
 * minus <b>interest due</b>.
 * </p>
 */
public class DecliningBalanceInterestLoanScheduleGenerator extends AbstractLoanScheduleGenerator {

    @Override
    public ComponentsForPeriod calculatePrincipalInterestComponentsForPeriod(final PaymentPeriodsInOneYearCalculator calculator,
            final double interestCalculationGraceOnRepaymentPeriodFraction, final Money totalCumulativePrincipal,
            @SuppressWarnings("unused") final Money totalCumulativeInterest,
            @SuppressWarnings("unused") final Money totalInterestDueForLoan, final Money cumulatingInterestPaymentDueToGrace,
            final Money outstandingBalance, final LoanApplicationTerms loanApplicationTerms, final int periodNumber, final MathContext mc,
            final TreeMap<LocalDate, Money> principalVariation, final Map<LocalDate, Money> compoundingMap, final LocalDate periodStartDate,
            final LocalDate periodEndDate, final Collection<LoanTermVariationsData> termVariations) {

        LocalDate interestStartDate = periodStartDate;
        Money interestForThisInstallment = totalCumulativePrincipal.zero();
        Money compoundedInterest = totalCumulativePrincipal.zero();
        Money balanceForInterestCalculation = outstandingBalance;
        Money cumulatingInterestDueToGrace = cumulatingInterestPaymentDueToGrace;
        Map<LocalDate, BigDecimal> interestRates = new HashMap<>(termVariations.size());

        for (LoanTermVariationsData loanTermVariation : termVariations) {
            if (loanTermVariation.getTermVariationType().isInterestRateVariation()
                    && loanTermVariation.isApplicable(periodStartDate, periodEndDate)) {
                LocalDate fromDate = loanTermVariation.getTermApplicableFrom();
                if (fromDate == null) {
                    fromDate = periodStartDate;
                }
                interestRates.put(fromDate, loanTermVariation.getDecimalValue());
                if (!principalVariation.containsKey(fromDate)) {
                    principalVariation.put(fromDate, balanceForInterestCalculation.zero());
                }
            }
        }

        final List<Map.Entry<LocalDate, Money>> relevantPrincipalVariations = principalVariation.entrySet().stream()
                .filter(principal -> !principal.getKey().isAfter(periodEndDate)).sorted((v1, v2) -> v1.getKey().compareTo(v2.getKey()))
                .collect(Collectors.toList());
        final Collection<LoanScheduleModelPeriod> variationPeriods = new ArrayList<>();
        int subPeriod = 1;
        if (principalVariation != null) {
            for (Map.Entry<LocalDate, Money> principal : relevantPrincipalVariations) {

                int interestForDays = Math.toIntExact(ChronoUnit.DAYS.between(interestStartDate, principal.getKey()));
                if (interestForDays > 0) {
                    final ComponentsForPeriod result = loanApplicationTerms.calculateTotalInterestForPeriod(calculator,
                            interestCalculationGraceOnRepaymentPeriodFraction, periodNumber, mc, cumulatingInterestDueToGrace,
                            balanceForInterestCalculation, interestStartDate, principal.getKey());

                    variationPeriods.add(LoanScheduleModelRepaymentSubPeriod.repayment(periodNumber, subPeriod++, interestStartDate,
                            principal.getKey(), balanceForInterestCalculation, result.interest()));

                    interestForThisInstallment = interestForThisInstallment.plus(result.interest());
                    cumulatingInterestDueToGrace = result.interestPaymentDueToGrace();
                    interestStartDate = principal.getKey();

                }
                Money compoundFee = totalCumulativePrincipal.zero();
                if (compoundingMap.containsKey(principal.getKey())) {
                    Money interestToBeCompounded = totalCumulativePrincipal.zero();
                    // for interest compounding
                    if (loanApplicationTerms.getInterestRecalculationCompoundingMethod().isInterestCompoundingEnabled()) {
                        interestToBeCompounded = interestForThisInstallment.minus(compoundedInterest);
                        balanceForInterestCalculation = balanceForInterestCalculation.plus(interestToBeCompounded);
                        compoundedInterest = interestForThisInstallment;
                    }
                    // fee compounding will be done after calculation
                    compoundFee = compoundingMap.get(principal.getKey());
                    compoundingMap.put(principal.getKey(), interestToBeCompounded.plus(compoundFee));
                }
                balanceForInterestCalculation = balanceForInterestCalculation.plus(principal.getValue()).plus(compoundFee);
                if (interestRates.containsKey(principal.getKey())) {
                    loanApplicationTerms.updateAnnualNominalInterestRate(interestRates.get(principal.getKey()));
                }
            }
        }

        final ComponentsForPeriod result = loanApplicationTerms.calculateTotalInterestForPeriod(calculator,
                interestCalculationGraceOnRepaymentPeriodFraction, periodNumber, mc, cumulatingInterestDueToGrace,
                balanceForInterestCalculation, interestStartDate, periodEndDate);

        if (relevantPrincipalVariations != null && !relevantPrincipalVariations.isEmpty()) {
            variationPeriods.add(LoanScheduleModelRepaymentSubPeriod.repayment(periodNumber, subPeriod++, interestStartDate, periodEndDate,
                    balanceForInterestCalculation, result.interest()));
        }

        interestForThisInstallment = interestForThisInstallment.plus(result.interest());
        cumulatingInterestDueToGrace = result.interestPaymentDueToGrace();

        Money interestTobeApproppriated = loanApplicationTerms.getInterestTobeApproppriated() == null
                ? Money.zero(interestForThisInstallment.getCurrency())
                : loanApplicationTerms.getInterestTobeApproppriated();

        if (loanApplicationTerms.getFixedEmiAmount() != null
                && loanApplicationTerms.isInterestToBeAppropriatedEquallyWhenGreaterThanEMIEnabled() && interestForThisInstallment
                        .isGreaterThan(Money.of(interestForThisInstallment.getCurrency(), loanApplicationTerms.getFixedEmiAmount()))) {
            LocalDate actualPeriodEndDate = this.scheduledDateGenerator.generateNextRepaymentDate(interestStartDate, loanApplicationTerms,
                    false);
            ComponentsForPeriod tempInterest = loanApplicationTerms.calculateTotalInterestForPeriod(calculator,
                    interestCalculationGraceOnRepaymentPeriodFraction, periodNumber, mc, cumulatingInterestDueToGrace,
                    balanceForInterestCalculation, interestStartDate, actualPeriodEndDate);

            Money fixedEmi = Money.of(interestForThisInstallment.getCurrency(), loanApplicationTerms.getFixedEmiAmount());

            if (tempInterest.interest().isGreaterThan(fixedEmi)) {
                loanApplicationTerms
                        .setInterestTobeApproppriated(interestTobeApproppriated.plus(interestForThisInstallment.minus(fixedEmi)));
                interestForThisInstallment = fixedEmi;
            } else {
                loanApplicationTerms.setInterestTobeApproppriated(
                        interestTobeApproppriated.plus(interestForThisInstallment.minus(tempInterest.interest())));
                interestForThisInstallment = tempInterest.interest();
            }
        }

        cumulatingInterestDueToGrace = result.interestPaymentDueToGrace();

        Money interestForPeriod = interestForThisInstallment;
        if (interestForPeriod.isGreaterThanZero()) {
            interestForPeriod = interestForPeriod.minus(cumulatingInterestPaymentDueToGrace);
        } else {
            interestForPeriod = cumulatingInterestDueToGrace.minus(cumulatingInterestPaymentDueToGrace);
        }
        Money principalForThisInstallment = loanApplicationTerms.calculateTotalPrincipalForPeriod(calculator, outstandingBalance,
                periodNumber, mc, interestForPeriod);

        // update cumulative fields for principal & interest
        final Money interestBroughtFowardDueToGrace = cumulatingInterestDueToGrace;
        final Money totalCumulativePrincipalToDate = totalCumulativePrincipal.plus(principalForThisInstallment);

        // adjust if needed
        principalForThisInstallment = loanApplicationTerms.adjustPrincipalIfLastRepaymentPeriod(principalForThisInstallment,
                totalCumulativePrincipalToDate, periodNumber);

        return new ComponentsForPeriod(principalForThisInstallment, interestForThisInstallment, interestBroughtFowardDueToGrace,
                variationPeriods);
    }
}
