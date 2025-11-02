package com.hospital.patterns.Adapter;

import java.math.BigDecimal;

public class PaymentAdapter implements PaymentSystem {
  private final LegacyPaymentSystem legacyPaymentSystem;

  public PaymentAdapter(LegacyPaymentSystem legacyPaymentSystem) {
    this.legacyPaymentSystem = legacyPaymentSystem;
  }

  @Override
  public boolean processPayment(BigDecimal amount, int patientId) {
    // Adapt: Convert BigDecimal to Double and int patientId to String patientName
    // In a real system, we would look up patient name by ID
    String patientName = "Patient " + patientId;
    Double amountDouble = amount.doubleValue();
    return legacyPaymentSystem.pay(amountDouble, patientName);
  }

  @Override
  public boolean refundPayment(String transactionId) {
    // Legacy system doesn't support refund by transaction ID
    // In a real adapter, we might need to implement this differently
    System.out.println("Refunding transaction: " + transactionId + " using legacy system");
    return true;
  }
}
