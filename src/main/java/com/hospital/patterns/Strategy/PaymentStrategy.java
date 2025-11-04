package com.hospital.patterns.Strategy;

import java.math.BigDecimal;

public interface PaymentStrategy {
  boolean pay(BigDecimal amount);
  String getPaymentMethod();
}

