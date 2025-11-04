package com.hospital.patterns.TemplateMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BillingReportGenerator extends MedicalReport {
  public BillingReportGenerator(int patientId, Date reportDate) {
    super(patientId, reportDate);
  }

  @Override
  protected Map<String, Object> collectData() {
    Map<String, Object> data = new HashMap<>();
    data.put("patientId", patientId);
    data.put("reportDate", reportDate);
    return data;
  }

  @Override
  protected String formatReport(Map<String, Object> data) {
    return "Billing Report: Patient " + data.get("patientId") + " on " + data.get("reportDate");
  }

  @Override
  protected boolean validateReport(String report) {
    return report != null && report.contains("Billing");
  }
}

