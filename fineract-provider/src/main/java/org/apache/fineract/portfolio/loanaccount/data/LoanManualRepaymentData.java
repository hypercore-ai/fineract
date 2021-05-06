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
package org.apache.fineract.portfolio.loanaccount.data;

import java.math.BigDecimal;
import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.organisation.monetary.domain.Money;

public class LoanManualRepaymentData {

    private Money principalPortion;
    private Money interestPortion;
    private Money feeChargesPortion;
    private Money penaltyChargesPortion;

    public LoanManualRepaymentData(MonetaryCurrency currency, BigDecimal principalPortion, BigDecimal interestPortion,
            BigDecimal feeChargesPortion, BigDecimal penaltyChargesPortion) {
        this.principalPortion = Money.of(currency, principalPortion);
        this.interestPortion = Money.of(currency, interestPortion);
        this.feeChargesPortion = Money.of(currency, feeChargesPortion);
        this.penaltyChargesPortion = Money.of(currency, penaltyChargesPortion);
    }

    public void setPrincipalPortion(Money principalPortion) {
        this.principalPortion = principalPortion;
    }

    public void setInterestPortion(Money interestPortion) {
        this.interestPortion = interestPortion;
    }

    public void setFeeChargesPortion(Money feeChargesPortion) {
        this.feeChargesPortion = feeChargesPortion;
    }

    public void setPenaltyChargesPortion(Money penaltyChargesPortion) {
        this.penaltyChargesPortion = penaltyChargesPortion;
    }

    public Money getPenaltyChargesPortion() {
        return penaltyChargesPortion;
    }

    public Money getFeeChargesPortion() {
        return feeChargesPortion;
    }

    public Money getInterestPortion() {
        return interestPortion;
    }

    public Money getPrincipalPortion() {
        return principalPortion;
    }

    public Money getSum() {
        return principalPortion.plus(interestPortion).plus(feeChargesPortion).plus(penaltyChargesPortion);
    }
}
