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

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionType;

public class Transaction {

    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private double amount;
    private LoanTransactionType type;
    private InstallmentComponent distribution;
    private boolean canceled;
    private boolean isWaive;
    private FeeBreakdown[] feesBreakdown;

    public Transaction() {
        super();
    }

    public FeeBreakdown[] getFeesBreakdown() {
        return feesBreakdown;
    }

    public void setFeesBreakdown(FeeBreakdown[] feesBreakdown) {
        this.feesBreakdown = feesBreakdown;
    }

    public String getId() {
        return id;
    }

    public boolean isWaive() {
        return isWaive;
    }

    public void setWaive(boolean isWaive) {
        this.isWaive = isWaive;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public InstallmentComponent getDistribution() {
        return distribution;
    }

    public void setDistribution(InstallmentComponent distribution) {
        this.distribution = distribution;
    }

    public LoanTransactionType getType() {
        return type;
    }

    public void setType(LoanTransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }
}
