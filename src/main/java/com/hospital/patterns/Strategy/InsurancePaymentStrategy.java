package com.hospital.patterns.Strategy;

import java.math.BigDecimal;

public class InsurancePaymentStrategy implements PaymentStrategy {
  private final String insuranceProvider;
  private final String policyNumber;

  public InsurancePaymentStrategy(String insuranceProvider, String policyNumber) {
    this.insuranceProvider = insuranceProvider;
    this.policyNumber = policyNumber;
  }

  @Override
  public boolean pay(BigDecimal amount) {
    System.out.println("Paid " + amount + " via insurance " + insuranceProvider + " (Policy: " + policyNumber + ")");
    return true;
  }

  @Override
  public String getPaymentMethod() {
    return "Insurance";
  }
}

