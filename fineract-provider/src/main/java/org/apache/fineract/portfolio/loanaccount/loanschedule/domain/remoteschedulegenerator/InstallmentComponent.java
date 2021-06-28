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

public class InstallmentComponent {

    private double principal;
    private double interest;
    private double fee;
    private double penalty;

    public InstallmentComponent() {
        super();
    }

    public double getPrincipal() {
        return principal;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    @Override
    public String toString() {
        return "{" + " principal='" + getPrincipal() + "'" + ", interest='" + getInterest() + "'" + ", fee='" + getFee() + "'"
                + ", penalty='" + getPenalty() + "'" + "}";
    }
}
