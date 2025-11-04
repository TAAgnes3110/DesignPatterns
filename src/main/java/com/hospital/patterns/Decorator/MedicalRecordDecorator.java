package com.hospital.patterns.Decorator;

public abstract class MedicalRecordDecorator implements MedicalRecord {
  protected MedicalRecord medicalRecord;

  public MedicalRecordDecorator(MedicalRecord medicalRecord) {
    this.medicalRecord = medicalRecord;
  }

  @Override
  public String getRecord() {
    return medicalRecord.getRecord();
  }

  @Override
  public boolean save() {
    return medicalRecord.save();
  }
}

