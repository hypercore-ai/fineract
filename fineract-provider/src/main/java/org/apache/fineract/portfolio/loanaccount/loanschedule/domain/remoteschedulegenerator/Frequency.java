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
import org.apache.fineract.portfolio.common.domain.PeriodFrequencyType;

public class Frequency {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private PeriodFrequencyType every;
    private int everyMultiplier;
    private String daysInEvery;
    private int repetitions;

    public Frequency() {}

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String getDaysInEvery() {
        return daysInEvery;
    }

    public void setDaysInEvery(String daysInEvery) {
        this.daysInEvery = daysInEvery;
    }

    public int getEveryMultiplier() {
        return everyMultiplier;
    }

    public void setEveryMultiplier(int everyMultiplier) {
        this.everyMultiplier = everyMultiplier;
    }

    public PeriodFrequencyType getEvery() {
        return every;
    }

    public void setEvery(PeriodFrequencyType every) {
        this.every = every;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

}
