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
package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteScheduleResponse {

    private Installment[] installments;
    private Transaction[] transactions;
    private double totalPrincipalDisbursed;
    private double totalPrincipalExpected;
    private double totalPrincipalPaid;
    private double totalInterestCharged;
    private double totalFeeChargesCharged;
    private double totalPenaltyChargesCharged;
    private double totalRepaymentExpected;
    private double totalOutstanding;

    @Override
    public String toString() {
        return "{" + " installments='" + Arrays.toString(getInstallments()) + "'" + ", transactions='" + Arrays.toString(getTransactions())
                + "'" + ", totalPrincipalDisbursed='" + getTotalPrincipalDisbursed() + "'" + ", totalPrincipalExpected='"
                + getTotalPrincipalExpected() + "'" + ", totalPrincipalPaid='" + getTotalPrincipalPaid() + "'" + ", totalInterestCharged='"
                + getTotalInterestCharged() + "'" + ", totalFeeChargesCharged='" + getTotalFeeChargesCharged() + "'"
                + ", totalPenaltyChargesCharged='" + getTotalPenaltyChargesCharged() + "'" + ", totalRepaymentExpected='"
                + getTotalRepaymentExpected() + "'" + ", totalOutstanding='" + getTotalOutstanding() + "'" + "}";
    }

    public double getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(double totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public double getTotalRepaymentExpected() {
        return totalRepaymentExpected;
    }

    public void setTotalRepaymentExpected(double totalRepaymentExpected) {
        this.totalRepaymentExpected = totalRepaymentExpected;
    }

    public double getTotalPenaltyChargesCharged() {
        return totalPenaltyChargesCharged;
    }

    public void setTotalPenaltyChargesCharged(double totalPenaltyChargesCharged) {
        this.totalPenaltyChargesCharged = totalPenaltyChargesCharged;
    }

    public double getTotalFeeChargesCharged() {
        return totalFeeChargesCharged;
    }

    public void setTotalFeeChargesCharged(double totalFeeChargesCharged) {
        this.totalFeeChargesCharged = totalFeeChargesCharged;
    }

    public double getTotalInterestCharged() {
        return totalInterestCharged;
    }

    public void setTotalInterestCharged(double totalInterestCharged) {
        this.totalInterestCharged = totalInterestCharged;
    }

    public double getTotalPrincipalPaid() {
        return totalPrincipalPaid;
    }

    public void setTotalPrincipalPaid(double totalPrincipalPaid) {
        this.totalPrincipalPaid = totalPrincipalPaid;
    }

    public double getTotalPrincipalExpected() {
        return totalPrincipalExpected;
    }

    public void setTotalPrincipalExpected(double totalPrincipalExpected) {
        this.totalPrincipalExpected = totalPrincipalExpected;
    }

    public double getTotalPrincipalDisbursed() {
        return totalPrincipalDisbursed;
    }

    public void setTotalPrincipalDisbursed(double totalPrincipalDisbursed) {
        this.totalPrincipalDisbursed = totalPrincipalDisbursed;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public void setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    public Installment[] getInstallments() {
        return installments;
    }

    public void setInstallments(Installment[] installments) {
        this.installments = installments;
    }

}
