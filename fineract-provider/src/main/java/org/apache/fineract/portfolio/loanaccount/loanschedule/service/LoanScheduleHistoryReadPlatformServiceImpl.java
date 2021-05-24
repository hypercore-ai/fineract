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
package org.apache.fineract.portfolio.loanaccount.loanschedule.service;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.monetary.data.CurrencyData;
import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.loanaccount.data.DisbursementData;
import org.apache.fineract.portfolio.loanaccount.data.RepaymentScheduleRelatedLoanData;
import org.apache.fineract.portfolio.loanaccount.exception.LoanNotFoundException;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleData;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanSchedulePeriodData;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.LoanDisbursementDetailsHistory;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.LoanDisbursementDetailsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanScheduleHistoryReadPlatformServiceImpl implements LoanScheduleHistoryReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;
    private final LoanDisbursementDetailsHistoryRepository loanDisbursementDetailsHistoryRepository;

    @Autowired
    public LoanScheduleHistoryReadPlatformServiceImpl(final RoutingDataSource dataSource, final PlatformSecurityContext context,
            final LoanDisbursementDetailsHistoryRepository loanDisbursementDetailsHistoryRepository) {
        this.context = context;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.loanDisbursementDetailsHistoryRepository = loanDisbursementDetailsHistoryRepository;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Integer fetchCurrentVersionNumber(Long loanId) {
        final String sql = "select MAX(lrs.version) from m_loan_repayment_schedule_history lrs where lrs.loan_id = ?";
        Integer max = this.jdbcTemplate.queryForObject(sql, new Object[] { loanId }, Integer.class);
        return ObjectUtils.defaultIfNull(max, 0);
    }

    public Integer fetchFirstVersionNumber(Long loanId) {
        final String sql = "select MIN(lrs.version) from m_loan_repayment_schedule_history lrs where lrs.loan_id = ?";
        Integer max = this.jdbcTemplate.queryForObject(sql, new Object[] { loanId }, Integer.class);
        return ObjectUtils.defaultIfNull(max, 0);
    }

    @Override
    public LoanScheduleData retrieveRepaymentArchiveSchedule(final Long loanId,
            final RepaymentScheduleRelatedLoanData repaymentScheduleRelatedLoanData, Collection<DisbursementData> disbursementData) {
        return this.retrieveRepaymentArchiveSchedule(loanId, repaymentScheduleRelatedLoanData, disbursementData, false);
    }

    @Override
    public LoanScheduleData retrieveRepaymentInitialArchivedSchedule(Long loanId,
            RepaymentScheduleRelatedLoanData repaymentScheduleRelatedLoanData) {

        Integer versionNumber = fetchFirstVersionNumber(loanId);
        List<LoanDisbursementDetailsHistory> disbursementDetailsHistoryList = this.loanDisbursementDetailsHistoryRepository
                .findByLoanIdAndVersion(loanId, versionNumber);
        List<DisbursementData> disbursementDataList = disbursementDetailsHistoryList.stream().map(disbursement -> {
            LocalDate expectedDisbursementDate = LocalDate.ofInstant(disbursement.expectedDisbursementDate().toInstant(),
                    ZoneId.systemDefault());
            LocalDate actualDisbursementDate = disbursement.actualDisbursementDate() != null ? LocalDate.ofInstant(disbursement.actualDisbursementDate().toInstant(),
                    ZoneId.systemDefault()) : null;
            return new DisbursementData(disbursement.getId(), expectedDisbursementDate, actualDisbursementDate, disbursement.principal(),
                    null, null, null);
        }).collect(Collectors.toList());
        return this.retrieveRepaymentArchiveSchedule(loanId, repaymentScheduleRelatedLoanData, disbursementDataList, true);
    }

    public LoanScheduleData retrieveRepaymentArchiveSchedule(final Long loanId,
            final RepaymentScheduleRelatedLoanData repaymentScheduleRelatedLoanData, Collection<DisbursementData> disbursementData,
            boolean fetchInitialExpectedSchedule) {

        try {
            this.context.authenticatedUser();
            Integer versionNumber = fetchInitialExpectedSchedule ? fetchFirstVersionNumber(loanId) : fetchCurrentVersionNumber(loanId);
            if (versionNumber == 0) {
                return null;
            }
            final LoanScheduleArchiveResultSetExtractor fullResultsetExtractor = new LoanScheduleArchiveResultSetExtractor(
                    repaymentScheduleRelatedLoanData, disbursementData, fetchInitialExpectedSchedule);
            final String sql = "select " + fullResultsetExtractor.schema()
                    + " where ls.loan_id = ? and ls.version = ? order by ls.loan_id, ls.installment";

            return this.jdbcTemplate.query(sql, fullResultsetExtractor, new Object[] { loanId, versionNumber });
        } catch (final EmptyResultDataAccessException e) {
            throw new LoanNotFoundException(loanId, e);
        }
    }

    private static final class LoanScheduleArchiveResultSetExtractor implements ResultSetExtractor<LoanScheduleData> {

        private final CurrencyData currency;
        private final DisbursementData disbursement;
        private final BigDecimal totalFeeChargesDueAtDisbursement;
        private final Collection<DisbursementData> disbursementData;
        private LocalDate lastDueDate;
        private BigDecimal outstandingLoanPrincipalBalance;
        private boolean useExpectedDisbursementDate;

        LoanScheduleArchiveResultSetExtractor(final RepaymentScheduleRelatedLoanData repaymentScheduleRelatedLoanData,
                Collection<DisbursementData> disbursementData, boolean useExpectedDisbursementDate) {
            this.currency = repaymentScheduleRelatedLoanData.getCurrency();
            this.disbursement = repaymentScheduleRelatedLoanData.disbursementData();
            this.totalFeeChargesDueAtDisbursement = repaymentScheduleRelatedLoanData.getTotalFeeChargesAtDisbursement();
            this.lastDueDate = this.disbursement.disbursementDate();
            this.outstandingLoanPrincipalBalance = this.disbursement.amount();
            this.disbursementData = disbursementData;
            this.useExpectedDisbursementDate = useExpectedDisbursementDate;
        }

        public String schema() {
            StringBuilder stringBuilder = new StringBuilder(200);
            stringBuilder.append(" ls.installment as period, ls.installment_sub_period as subPeriod, ls.fromdate as fromDate, ls.duedate as dueDate, ");
            stringBuilder.append(
                    "ls.principal_amount as principalDue, ls.interest_amount as interestDue, ls.fee_charges_amount as feeChargesDue, ls.penalty_charges_amount as penaltyChargesDue ");
            stringBuilder.append(" from m_loan_repayment_schedule_history ls ");
            return stringBuilder.toString();
        }

        @Override
        public LoanScheduleData extractData(final ResultSet rs) throws SQLException, DataAccessException {

            LocalDate firstDisbursementPeriodDate = this.useExpectedDisbursementDate ? this.disbursement.getExpectedDisbursementDate()
                    : this.disbursement.disbursementDate();
            final LoanSchedulePeriodData disbursementPeriod = LoanSchedulePeriodData.disbursementOnlyPeriod(firstDisbursementPeriodDate,
                    this.disbursement.amount(), this.totalFeeChargesDueAtDisbursement, this.disbursement.isDisbursed());

            final Collection<LoanSchedulePeriodData> periods = new ArrayList<>();
            final MonetaryCurrency monCurrency = new MonetaryCurrency(this.currency.code(), this.currency.decimalPlaces(),
                    this.currency.currencyInMultiplesOf());
            BigDecimal totalPrincipalDisbursed = BigDecimal.ZERO;
            if (disbursementData == null || disbursementData.isEmpty()) {
                periods.add(disbursementPeriod);
                totalPrincipalDisbursed = Money.of(monCurrency, this.disbursement.amount()).getAmount();
            } else {
                this.outstandingLoanPrincipalBalance = BigDecimal.ZERO;
            }

            Money totalPrincipalExpected = Money.zero(monCurrency);
            Money totalInterestCharged = Money.zero(monCurrency);
            Money totalFeeChargesCharged = Money.zero(monCurrency);
            Money totalPenaltyChargesCharged = Money.zero(monCurrency);
            Money totalRepaymentExpected = Money.zero(monCurrency);

            // update totals with details of fees charged during disbursement
            totalFeeChargesCharged = totalFeeChargesCharged.plus(disbursementPeriod.feeChargesDue());
            totalRepaymentExpected = totalRepaymentExpected.plus(disbursementPeriod.feeChargesDue());

            Integer loanTermInDays = Integer.valueOf(0);
            while (rs.next()) {
                final Integer period = JdbcSupport.getInteger(rs, "period");
                final Integer subPeriod = JdbcSupport.getInteger(rs, "subPeriod");
                LocalDate fromDate = JdbcSupport.getLocalDate(rs, "fromDate");
                final LocalDate dueDate = JdbcSupport.getLocalDate(rs, "dueDate");
                if (disbursementData != null && subPeriod == null) {
                    BigDecimal principal = BigDecimal.ZERO;
                    for (DisbursementData data : disbursementData) {
                        LocalDate disbursementDate = this.useExpectedDisbursementDate ? data.getExpectedDisbursementDate()
                                : data.disbursementDate();

                        if (fromDate.equals(firstDisbursementPeriodDate) && disbursementDate.equals(fromDate)) {
                            principal = principal.add(data.amount());
                            final LoanSchedulePeriodData periodData = LoanSchedulePeriodData.disbursementOnlyPeriod(disbursementDate,
                                    data.amount(), this.totalFeeChargesDueAtDisbursement, data.isDisbursed());
                            periods.add(periodData);
                            this.outstandingLoanPrincipalBalance = this.outstandingLoanPrincipalBalance.add(data.amount());
                        } else if (useExpectedDisbursementDate ? data.isExpectedDueForDisbursement(fromDate, dueDate)
                                : data.isDueForDisbursement(fromDate, dueDate)) {
                            principal = principal.add(data.amount());
                            final LoanSchedulePeriodData periodData = LoanSchedulePeriodData.disbursementOnlyPeriod(disbursementDate,
                                    data.amount(), BigDecimal.ZERO, data.isDisbursed());
                            periods.add(periodData);
                            this.outstandingLoanPrincipalBalance = this.outstandingLoanPrincipalBalance.add(data.amount());
                        }
                    }
                    totalPrincipalDisbursed = totalPrincipalDisbursed.add(principal);
                }

                Integer daysInPeriod = Integer.valueOf(0);
                if (fromDate != null) {
                    daysInPeriod = Math.toIntExact(ChronoUnit.DAYS.between(fromDate, dueDate));
                    loanTermInDays = Integer.valueOf(loanTermInDays.intValue() + daysInPeriod.intValue());
                }

                final BigDecimal principalDue = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs, "principalDue");
                totalPrincipalExpected = totalPrincipalExpected.plus(principalDue);

                final BigDecimal interestExpectedDue = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs, "interestDue");
                totalInterestCharged = totalInterestCharged.plus(interestExpectedDue);

                final BigDecimal totalInstallmentAmount = totalPrincipalExpected.zero().plus(principalDue).plus(interestExpectedDue)
                        .getAmount();

                final BigDecimal feeChargesExpectedDue = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs, "feeChargesDue");
                totalFeeChargesCharged = totalFeeChargesCharged.plus(feeChargesExpectedDue);

                final BigDecimal penaltyChargesExpectedDue = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs, "penaltyChargesDue");
                totalPenaltyChargesCharged = totalPenaltyChargesCharged.plus(penaltyChargesExpectedDue);

                final BigDecimal totalExpectedCostOfLoanForPeriod = interestExpectedDue.add(feeChargesExpectedDue)
                        .add(penaltyChargesExpectedDue);

                final BigDecimal totalDueForPeriod = principalDue.add(totalExpectedCostOfLoanForPeriod);

                totalRepaymentExpected = totalRepaymentExpected.plus(totalDueForPeriod);

                if (fromDate == null) {
                    fromDate = this.lastDueDate;
                }
                final BigDecimal outstandingPrincipalBalanceOfLoan = this.outstandingLoanPrincipalBalance.subtract(principalDue);

                // update based on current period values
                this.lastDueDate = dueDate;
                this.outstandingLoanPrincipalBalance = this.outstandingLoanPrincipalBalance.subtract(principalDue);


                LoanSchedulePeriodData periodData;
                if (subPeriod != null) {
                    periodData = LoanSchedulePeriodData.repaymentSubPeriod(period, subPeriod, fromDate, dueDate, outstandingPrincipalBalanceOfLoan, interestExpectedDue);
                } else {
                     periodData = LoanSchedulePeriodData.repaymentOnlyPeriod(period, fromDate, dueDate,
                            principalDue, outstandingPrincipalBalanceOfLoan, interestExpectedDue, feeChargesExpectedDue,
                            penaltyChargesExpectedDue, totalDueForPeriod, totalInstallmentAmount);

                }

                periods.add(periodData);
            }

            return new LoanScheduleData(this.currency, periods, loanTermInDays, totalPrincipalDisbursed, totalPrincipalExpected.getAmount(),
                    totalInterestCharged.getAmount(), totalFeeChargesCharged.getAmount(), totalPenaltyChargesCharged.getAmount(),
                    totalRepaymentExpected.getAmount());
        }

    }

}
