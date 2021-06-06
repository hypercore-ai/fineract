package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class InstallmentComponent {
  private float principal;
  private float interest;
  private float fee;
  private float penalty;

  public InstallmentComponent() {
    super();
  }

  public float getPrincipal() {
    return principal;
  }

  public float getPenalty() {
    return penalty;
  }

  public void setPenalty(float penalty) {
    this.penalty = penalty;
  }

  public float getFee() {
    return fee;
  }

  public void setFee(float fee) {
    this.fee = fee;
  }

  public float getInterest() {
    return interest;
  }

  public void setInterest(float interest) {
    this.interest = interest;
  }

  public void setPrincipal(float principal) {
    this.principal = principal;
  }

  @Override
  public String toString() {
    return "{" + " principal='" + getPrincipal() + "'" + ", interest='" + getInterest() + "'" + ", fee='" + getFee()
        + "'" + ", penalty='" + getPenalty() + "'" + "}";
  }
}
