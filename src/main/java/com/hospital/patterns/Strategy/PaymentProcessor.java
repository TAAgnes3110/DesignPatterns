package com.hospital.patterns.Strategy;

public class PaymentProcessor {
  private PaymentStrategy strategy;

  public PaymentProcessor(PaymentStrategy strategy) {
    this.strategy = strategy;
  }

  public void setStrategy(PaymentStrategy strategy) {
    this.strategy = strategy;
  }

  public boolean processPayment(Billing billing) {
    return strategy.pay(billing.getTotalAmount());
  }
}

