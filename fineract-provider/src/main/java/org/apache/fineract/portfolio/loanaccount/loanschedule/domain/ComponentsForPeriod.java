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

import java.util.Collection;
import org.apache.fineract.organisation.monetary.domain.Money;

public class ComponentsForPeriod {

    private final Money principal;
    private final Money interest;
    private final Money interestPaymentDueToGrace;
    private final Collection<LoanScheduleModelPeriod> variationSubPeriods;

    public ComponentsForPeriod(final Money principal, final Money interest, final Money interestPaymentDueToGrace) {
        this(principal, interest, interestPaymentDueToGrace, null);
    }

    public ComponentsForPeriod(final Money principal, final Money interest, final Money interestPaymentDueToGrace,
            final Collection<LoanScheduleModelPeriod> variationSubPeriods) {
        this.principal = principal;
        this.interest = interest;
        this.interestPaymentDueToGrace = interestPaymentDueToGrace;
        this.variationSubPeriods = variationSubPeriods;
    }

    public Money principal() {
        return this.principal;
    }

    public Money interest() {
        return this.interest;
    }

    public Money interestPaymentDueToGrace() {
        return this.interestPaymentDueToGrace;
    }

    public Collection<LoanScheduleModelPeriod> variationSubPeriods() {
        return this.variationSubPeriods;
    }
}
