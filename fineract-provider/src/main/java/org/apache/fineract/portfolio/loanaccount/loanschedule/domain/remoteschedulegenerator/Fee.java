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

public class Fee {

    private String id;
    private FeeTiming timing;
    private Frequency frequency;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private FeeCalculation calculationType;
    private double value;
    private Period period;
    private boolean isPenalty;

    public Fee() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getId() {
        return id;
    }

    public boolean isPenalty() {
        return isPenalty;
    }

    public void setPenalty(boolean isPenalty) {
        this.isPenalty = isPenalty;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public FeeCalculation getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(FeeCalculation calculationType) {
        this.calculationType = calculationType;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency timingFrequency) {
        this.frequency = timingFrequency;
    }

    public FeeTiming getTiming() {
        return timing;
    }

    public void setTiming(FeeTiming timing) {
        this.timing = timing;
    }

    public void setId(String id) {
        this.id = id;
    }

}
