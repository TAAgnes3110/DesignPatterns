package com.hospital.patterns.Strategy;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class StrategyTest {
  @Test
  void testCashPayment() {
    PaymentStrategy strategy = new CashPaymentStrategy();
    assertTrue(strategy.pay(new BigDecimal("100.00")));
    assertEquals("Cash", strategy.getPaymentMethod());
    System.out.println("✓ Strategy: CashPayment");
  }

  @Test
  void testCreditCardPayment() {
    PaymentStrategy strategy = new CreditCardPaymentStrategy("1234", "Vân Anh");
    assertTrue(strategy.pay(new BigDecimal("200.00")));
    assertEquals("Credit Card", strategy.getPaymentMethod());
    System.out.println("✓ Strategy: CreditCardPayment");
  }

  @Test
  void testInsurancePayment() {
    PaymentStrategy strategy = new InsurancePaymentStrategy("ABC Insurance", "POL123");
    assertTrue(strategy.pay(new BigDecimal("300.00")));
    assertEquals("Insurance", strategy.getPaymentMethod());
    System.out.println("✓ Strategy: InsurancePayment");
  }

  @Test
  void testPaymentProcessor() {
    PaymentProcessor processor = new PaymentProcessor(new CashPaymentStrategy());
    Billing billing = new Billing(1, 101, 201, new BigDecimal("100.00"));
    assertTrue(processor.processPayment(billing));
    System.out.println("✓ Strategy: PaymentProcessor");
  }
}
