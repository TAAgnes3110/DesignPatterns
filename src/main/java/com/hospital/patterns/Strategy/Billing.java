package com.hospital.patterns.Strategy;

import java.math.BigDecimal;
import java.util.Date;

public class Billing {
  private final int billId;
  private final int patientId;
  private final int appointmentId;
  private final BigDecimal totalAmount;
  private String paymentStatus;
  private Date paymentDate;
  private String insuranceProvider;

  public Billing(int billId, int patientId, int appointmentId, BigDecimal totalAmount) {
    this.billId = billId;
    this.patientId = patientId;
    this.appointmentId = appointmentId;
    this.totalAmount = totalAmount;
  }

  public BigDecimal getTotalAmount() { return totalAmount; }
  public int getBillId() { return billId; }
  public int getPatientId() { return patientId; }
  public int getAppointmentId() { return appointmentId; }
  public String getPaymentStatus() { return paymentStatus; }
  public Date getPaymentDate() { return paymentDate; }
  public String getInsuranceProvider() { return insuranceProvider; }
}

