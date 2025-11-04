package com.hospital.patterns.Decorator;

public class SignedMedicalRecordDecorator extends MedicalRecordDecorator {
  private final String signature;

  public SignedMedicalRecordDecorator(MedicalRecord medicalRecord, String signature) {
    super(medicalRecord);
    this.signature = signature;
  }

  @Override
  public String getRecord() {
    return medicalRecord.getRecord() + " [Signed: " + signature + "]";
  }

  @Override
  public boolean save() {
    sign(medicalRecord.getRecord());
    return medicalRecord.save();
  }

  private String sign(String data) {
    return data + " [Signature: " + signature + "]";
  }
}

