package com.hospital.patterns.Decorator;

public class EncryptedMedicalRecordDecorator extends MedicalRecordDecorator {
  private final String encryptionKey;

  public EncryptedMedicalRecordDecorator(MedicalRecord medicalRecord, String encryptionKey) {
    super(medicalRecord);
    this.encryptionKey = encryptionKey;
  }

  @Override
  public String getRecord() {
    return decrypt(medicalRecord.getRecord());
  }

  @Override
  public boolean save() {
    encrypt(medicalRecord.getRecord());
    System.out.println("Saving encrypted medical record with key: " + encryptionKey);
    return true;
  }

  private String encrypt(String data) {
    return "[ENCRYPTED:" + data + "]";
  }

  private String decrypt(String data) {
    return data.replace("[ENCRYPTED:", "").replace("]", "");
  }
}

