package com.hospital.patterns.Adapter;

import java.math.BigDecimal;

public interface PaymentSystem {
  boolean processPayment(BigDecimal amount, int patientId);
  boolean refundPayment(String transactionId);
}
