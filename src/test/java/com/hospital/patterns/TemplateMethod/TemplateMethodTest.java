package com.hospital.patterns.TemplateMethod;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TemplateMethodTest {
  @Test
  void testPatientReportGenerator() {
    MedicalReport report = new PatientReportGenerator(101, new Date());
    String result = report.generateReport();
    assertTrue(result.contains("Patient Report"));
    System.out.println("✓ TemplateMethod: PatientReportGenerator");
  }

  @Test
  void testAppointmentReportGenerator() {
    MedicalReport report = new AppointmentReportGenerator(101, new Date());
    String result = report.generateReport();
    assertTrue(result.contains("Appointment Report"));
    System.out.println("✓ TemplateMethod: AppointmentReportGenerator");
  }

  @Test
  void testBillingReportGenerator() {
    MedicalReport report = new BillingReportGenerator(101, new Date());
    String result = report.generateReport();
    assertTrue(result.contains("Billing Report"));
    System.out.println("✓ TemplateMethod: BillingReportGenerator");
  }
}
