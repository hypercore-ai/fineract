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
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;

@Entity
@Table(name = "m_loan_disbursement_detail_history")
public class LoanDisbursementDetailsHistory extends AbstractPersistableCustom {

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Temporal(TemporalType.DATE)
    @Column(name = "expected_disburse_date")
    private Date expectedDisbursementDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "disbursedon_date")
    private Date actualDisbursementDate;

    @Column(name = "principal", scale = 6, precision = 19, nullable = false)
    private BigDecimal principal;

    @Column(name = "version")
    private Integer version;

    protected LoanDisbursementDetailsHistory() {

    }

    public LoanDisbursementDetailsHistory(final Loan loan, final Integer version, final Date expectedDisbursementDate,
            final Date actualDisbursementDate, final BigDecimal principal) {
        this.expectedDisbursementDate = expectedDisbursementDate;
        this.actualDisbursementDate = actualDisbursementDate;
        this.principal = principal;
        this.loan = loan;
        this.version = version;
    }

    public void updateLoan(final Loan loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof LoanDisbursementDetailsHistory)) {
            return false;
        }
        final LoanDisbursementDetailsHistory loanDisbursementDetails = (LoanDisbursementDetailsHistory) obj;
        if (loanDisbursementDetails.principal.equals(this.principal)
                && loanDisbursementDetails.expectedDisbursementDate.compareTo(this.expectedDisbursementDate) == 0 ? Boolean.TRUE
                        : Boolean.FALSE) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedDisbursementDate, principal);
    }

    public void copy(final LoanDisbursementDetailsHistory disbursementDetails) {
        this.principal = disbursementDetails.principal;
        this.expectedDisbursementDate = disbursementDetails.expectedDisbursementDate;
        this.actualDisbursementDate = disbursementDetails.actualDisbursementDate;
    }

    public Date expectedDisbursementDate() {
        return this.expectedDisbursementDate;
    }

    public LocalDate expectedDisbursementDateAsLocalDate() {
        LocalDate expectedDisburseDate = null;
        if (this.expectedDisbursementDate != null) {
            expectedDisburseDate = LocalDate.ofInstant(this.expectedDisbursementDate.toInstant(), ZoneId.systemDefault());
        }
        return expectedDisburseDate;
    }

    public Date actualDisbursementDate() {
        return this.actualDisbursementDate;
    }

    public BigDecimal principal() {
        return this.principal;
    }

    public Date getDisbursementDate() {
        Date disbursementDate = this.expectedDisbursementDate;
        if (this.actualDisbursementDate != null) {
            disbursementDate = this.actualDisbursementDate;
        }
        return disbursementDate;
    }
}
