package com.hospital.patterns.Strategy;

import java.math.BigDecimal;

public class CashPaymentStrategy implements PaymentStrategy {
  @Override
  public boolean pay(BigDecimal amount) {
    System.out.println("Paid " + amount + " in cash");
    return true;
  }

  @Override
  public String getPaymentMethod() {
    return "Cash";
  }
}

