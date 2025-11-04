package com.hospital.patterns.Adapter;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AdapterTest {
  @Test
  void testAdapterPattern() {
    PaymentSystem adapter = new PaymentAdapter(new LegacyPaymentSystem());
    assertTrue(adapter.processPayment(new BigDecimal("100.00"), 101));
    System.out.println("✓ Adapter: PaymentAdapter");
  }
}
