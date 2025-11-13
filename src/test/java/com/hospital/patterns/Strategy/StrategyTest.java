package com.hospital.patterns.Strategy;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StrategyTest {

    // Helper method để giảm lặp code assert và in ấn
    private void assertPaymentSuccess(PaymentStrategy strategy, String expectedMethod, String amountStr) {
        BigDecimal amount = new BigDecimal(amountStr);
        assertTrue(strategy.pay(amount), "Thanh toán thất bại với " + expectedMethod);
        assertEquals(expectedMethod, strategy.getPaymentMethod());
        System.out.println("✓ " + expectedMethod + ": Thanh toán thành công " + amount);
    }

    @Test
    void testIndividualStrategies() {
        System.out.println("=== Test Các Chiến Lược Thanh Toán Riêng Lẻ ===");
        assertPaymentSuccess(new CashPaymentStrategy(), "Cash", "100.00");
        assertPaymentSuccess(new CreditCardPaymentStrategy("1234", "Vân Anh"), "Credit Card", "200.00");
        assertPaymentSuccess(new InsurancePaymentStrategy("Bảo Việt", "POL123"), "Insurance", "300.00");
    }

    @Test
    void testPaymentProcessorRuntimeChange() {
        System.out.println("\n=== Test Thay Đổi Chiến Lược Runtime (PaymentProcessor) ===");
        PaymentProcessor processor = new PaymentProcessor(new CashPaymentStrategy());
        Billing billing1 = new Billing(1, 101, 201, new BigDecimal("150.00"));

        // Test strategy ban đầu (Cash)
        assertTrue(processor.processPayment(billing1));
        System.out.println("✓ Processor ban đầu dùng Cash: Xử lý xong " + billing1.getTotalAmount());

        // Đổi sang strategy mới (Credit Card)
        processor.setStrategy(new CreditCardPaymentStrategy("5678", "Nguyễn Văn A"));
        Billing billing2 = new Billing(2, 102, 202, new BigDecimal("500.00"));

        assertTrue(processor.processPayment(billing2));
        System.out.println("✓ Processor đổi sang Credit Card: Xử lý xong " + billing2.getTotalAmount());
    }

    @Test
    void testStrategyFlexibility() {
        System.out.println("\n=== Test Tính Linh Hoạt (Duyệt danh sách Strategy) ===");
        BigDecimal amount = new BigDecimal("99.99");
        PaymentStrategy[] strategies = {
                new CashPaymentStrategy(),
                new CreditCardPaymentStrategy("9999", "Trần B"),
                new InsurancePaymentStrategy("XYZ", "P456")
        };

        for (PaymentStrategy s : strategies) {
            assertTrue(s.pay(amount));
            System.out.println("  → Đã test xong: " + s.getPaymentMethod());
        }
        System.out.println("✓ Tất cả các chiến lược đều hoạt động tốt với cùng một đầu vào.");
    }
}