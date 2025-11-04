package com.hospital.patterns.Decorator;

public class BasicMedicalRecord implements MedicalRecord {
  private final int recordId;
  private final int patientId;
  private final int doctorId;
  private final String diagnosis;
  private final String treatment;
  private final String prescription;

  public BasicMedicalRecord(int recordId, int patientId, int doctorId) {
    this.recordId = recordId;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.diagnosis = "";
    this.treatment = "";
    this.prescription = "";
  }

  public BasicMedicalRecord(int recordId, int patientId, int doctorId, String diagnosis, String treatment, String prescription) {
    this.recordId = recordId;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.diagnosis = diagnosis;
    this.treatment = treatment;
    this.prescription = prescription;
  }

  @Override
  public String getRecord() {
    return "Basic Medical Record";
  }

  @Override
  public boolean save() {
    System.out.println("Saving basic medical record");
    return true;
  }
}

