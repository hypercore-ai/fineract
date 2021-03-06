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

public class PaymentStrategy {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    private boolean allowPrincipalRestructuring;
    private PaymentComponent[] componentsOrder;

    public PaymentStrategy() {}

    public PaymentComponent[] getComponentsOrder() {
        return componentsOrder;
    }

    public void setComponentsOrder(PaymentComponent[] componentsOrder) {
        this.componentsOrder = componentsOrder;
    }

    public boolean isAllowPrincipalRestructuring() {
        return allowPrincipalRestructuring;
    }

    public void setAllowPrincipalRestructuring(boolean allowInterestRecalculation) {
        this.allowPrincipalRestructuring = allowInterestRecalculation;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

}
