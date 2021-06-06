package org.apache.fineract.portfolio.loanaccount.loanschedule.domain.remoteschedulegenerator;

public class PrincipalChange {
  private PrincipalChangeType type;
  private float balanceChange;
  private boolean isActual;

  public PrincipalChange() {
  }

  public PrincipalChangeType getType() {
    return type;
  }

  public boolean isActual() {
    return isActual;
  }

  public void setActual(boolean isActual) {
    this.isActual = isActual;
  }

  public float getBalanceChange() {
    return balanceChange;
  }

  public void setBalanceChange(float balanceChange) {
    this.balanceChange = balanceChange;
  }

  public void setType(PrincipalChangeType type) {
    this.type = type;
  }
}
