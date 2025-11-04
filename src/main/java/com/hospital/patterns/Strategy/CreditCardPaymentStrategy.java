package com.hospital.patterns.Strategy;

import java.math.BigDecimal;

public class CreditCardPaymentStrategy implements PaymentStrategy {
  private final String cardNumber;
  private final String cardHolder;

  public CreditCardPaymentStrategy(String cardNumber, String cardHolder) {
    this.cardNumber = cardNumber;
    this.cardHolder = cardHolder;
  }

  @Override
  public boolean pay(BigDecimal amount) {
    System.out.println("Paid " + amount + " with credit card " + cardNumber + " by " + cardHolder);
    return true;
  }

  @Override
  public String getPaymentMethod() {
    return "Credit Card";
  }
}

