package com.hospital.patterns.Facade;

import java.math.BigDecimal;

public class Billing {
  private final int billId;
  private final int patientId;
  private final BigDecimal totalAmount;

  public Billing(int billId, int patientId, BigDecimal totalAmount) {
    this.billId = billId;
    this.patientId = patientId;
    this.totalAmount = totalAmount;
  }

  public int getBillId() { return billId; }
  public int getPatientId() { return patientId; }
  public BigDecimal getTotalAmount() { return totalAmount; }
}

