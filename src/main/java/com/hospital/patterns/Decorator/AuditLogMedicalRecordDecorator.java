package com.hospital.patterns.Decorator;

public class AuditLogMedicalRecordDecorator extends MedicalRecordDecorator {
  private final AuditLogService logService;

  public AuditLogMedicalRecordDecorator(MedicalRecord medicalRecord, AuditLogService logService) {
    super(medicalRecord);
    this.logService = logService;
  }

  @Override
  public String getRecord() {
    logAction("Record accessed");
    return medicalRecord.getRecord();
  }

  @Override
  public boolean save() {
    logAction("Record saved");
    return medicalRecord.save();
  }

  private void logAction(String action) {
    logService.log(action);
  }
}

