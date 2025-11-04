package com.hospital.patterns.TemplateMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PatientReportGenerator extends MedicalReport {
  public PatientReportGenerator(int patientId, Date reportDate) {
    super(patientId, reportDate);
  }

  @Override
  protected Map<String, Object> collectData() {
    Map<String, Object> data = new HashMap<>();
    data.put("patientId", patientId);
    return data;
  }

  @Override
  protected String formatReport(Map<String, Object> data) {
    return "Patient Report: " + data.get("patientId");
  }

  @Override
  protected boolean validateReport(String report) {
    return report != null && !report.isEmpty();
  }
}

