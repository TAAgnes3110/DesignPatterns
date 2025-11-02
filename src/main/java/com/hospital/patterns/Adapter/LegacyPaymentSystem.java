package com.hospital.patterns.Adapter;

public class LegacyPaymentSystem {
  public boolean pay(Double amount, String patientName) {
    // Legacy payment system implementation
    // In a real system, this would connect to the old payment gateway
    System.out.println("Processing legacy payment: " + amount + " for patient: " + patientName);
    return true;
  }
}
