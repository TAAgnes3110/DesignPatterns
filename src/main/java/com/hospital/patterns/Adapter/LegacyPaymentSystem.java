package com.hospital.patterns.Adapter;

public class LegacyPaymentSystem {
  public boolean pay(Double amount, String patientName) {
    System.out.println("Processing legacy payment: " + amount + " for patient: " + patientName);
    return true;
  }
}
