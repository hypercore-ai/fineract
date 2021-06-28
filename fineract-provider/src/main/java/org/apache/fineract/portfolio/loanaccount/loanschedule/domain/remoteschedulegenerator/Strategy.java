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

public class Strategy {

    private PaymentComponent component;
    private ValuationTime valuationTime;

    public Strategy() {
        super();
    }

    public PaymentComponent getComponent() {
        return component;
    }

    public ValuationTime getValuationTime() {
        return valuationTime;
    }

    public void setValuationTime(ValuationTime valuationTime) {
        this.valuationTime = valuationTime;
    }

    public void setComponent(PaymentComponent component) {
        this.component = component;
    }
}
