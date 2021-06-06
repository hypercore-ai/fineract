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
    return "{" + " principal='" + getPrincipal() + "'" + ", interest='" + getInterest() + "'" + ", fee='" + getFee()
        + "'" + ", penalty='" + getPenalty() + "'" + "}";
  }
}
