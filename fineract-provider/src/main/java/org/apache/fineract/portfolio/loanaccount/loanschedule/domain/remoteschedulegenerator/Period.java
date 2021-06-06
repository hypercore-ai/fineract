package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

import java.time.LocalDate;

public class Period {
  private LocalDate startDate;
  private LocalDate endDate;

  public Period() {
    super();
  }

  public LocalDate getStartDate() {
    return startDate;
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
