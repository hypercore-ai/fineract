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

import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.LoanDisbursementDetailsHistory;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.LoanRepaymentScheduleHistory;
import org.apache.fineract.portfolio.loanaccount.rescheduleloan.domain.LoanRescheduleRequest;

import java.util.List;

public interface LoanScheduleHistoryWritePlatformService {

    List<LoanDisbursementDetailsHistory> createDisbursementsArchive(Loan loan, Integer version);

    void createAndSaveLoanDisbursementArchive(Loan loan, Integer version);

    List<LoanRepaymentScheduleHistory> createLoanScheduleArchive(List<LoanRepaymentScheduleInstallment> repaymentScheduleInstallments,
            Loan loan, LoanRescheduleRequest loanRescheduleRequest);

    void createAndSaveLoanScheduleArchive(List<LoanRepaymentScheduleInstallment> repaymentScheduleInstallments, Loan loan,
            LoanRescheduleRequest loanRescheduleRequest);

    void saveScheduleAndDisbursementsArchive(List<LoanRepaymentScheduleHistory> loanRepaymentScheduleHistoryList,
                                             List<LoanDisbursementDetailsHistory> loanDisbursementDetailsHistoryList);

}
