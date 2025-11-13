package com.hospital.patterns.Adapter;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AdapterTest {
  @Test
  void testAdapterPattern() {
    LegacyPaymentSystem legacySystem = new LegacyPaymentSystem();
    PaymentSystem adapter = new PaymentAdapter(legacySystem);

    BigDecimal amount = new BigDecimal("100.00");
    int patientId = 101;
    String transactionId = "TXN123";

    System.out.println("  → LegacyPaymentSystem: pay(Double, String)");
    System.out.println("  → PaymentAdapter: processPayment(BigDecimal, int)");

    assertTrue(adapter.processPayment(amount, patientId));
    System.out.println("  → processPayment: " + amount + " cho patient ID " + patientId);

    assertTrue(adapter.refundPayment(transactionId));
    System.out.println("  → refundPayment: transaction " + transactionId);

    System.out.println("✓ Adapter: PaymentAdapter chuyển đổi LegacyPaymentSystem sang PaymentSystem thành công");
  }
}
