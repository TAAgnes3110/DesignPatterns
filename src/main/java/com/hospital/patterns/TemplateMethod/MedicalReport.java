package com.hospital.patterns.TemplateMethod;

import java.util.Date;
import java.util.Map;

public abstract class MedicalReport {
  protected int patientId;
  protected Date reportDate;

  public MedicalReport(int patientId, Date reportDate) {
    this.patientId = patientId;
    this.reportDate = reportDate;
  }

  public final String generateReport() {
    Map<String, Object> data = collectData();
    String report = formatReport(data);
    if (validateReport(report)) {
      return report;
    }
    return "Invalid report";
  }

  protected abstract Map<String, Object> collectData();
  protected abstract String formatReport(Map<String, Object> data);
  protected abstract boolean validateReport(String report);
}

