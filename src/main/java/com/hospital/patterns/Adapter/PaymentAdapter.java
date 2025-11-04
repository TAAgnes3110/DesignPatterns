package com.hospital.patterns.Adapter;

import java.math.BigDecimal;

public class PaymentAdapter implements PaymentSystem {
  private final LegacyPaymentSystem legacyPaymentSystem;

  public PaymentAdapter(LegacyPaymentSystem legacyPaymentSystem) {
    this.legacyPaymentSystem = legacyPaymentSystem;
  }

  @Override
  public boolean processPayment(BigDecimal amount, int patientId) {
    String patientName = "Patient " + patientId;
    Double amountDouble = amount.doubleValue();
    return legacyPaymentSystem.pay(amountDouble, patientName);
  }

  @Override
  public boolean refundPayment(String transactionId) {
    System.out.println("Refunding transaction: " + transactionId + " using legacy system");
    return true;
  }
}
