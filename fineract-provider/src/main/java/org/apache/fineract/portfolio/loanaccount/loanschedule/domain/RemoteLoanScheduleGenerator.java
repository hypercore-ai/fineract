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

import java.math.MathContext;
import java.time.LocalDate;
import java.util.Set;

import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.portfolio.loanaccount.data.HolidayDetailDTO;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCharge;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.domain.transactionprocessor.LoanRepaymentScheduleTransactionProcessor;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.LoanScheduleDTO;
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

    LOG.info(response.toString());

    return null;
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

}
