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

public class PrincipalChange {

    private PrincipalChangeType type;
    private double balanceChange;
    private boolean isActual;

    public PrincipalChange() {}

    public PrincipalChangeType getType() {
        return type;
    }

    public boolean isActual() {
        return isActual;
    }

    public void setActual(boolean isActual) {
        this.isActual = isActual;
    }

    public double getBalanceChange() {
        return balanceChange;
    }

    public void setBalanceChange(double balanceChange) {
        this.balanceChange = balanceChange;
    }

    public void setType(PrincipalChangeType type) {
        this.type = type;
    }
}
