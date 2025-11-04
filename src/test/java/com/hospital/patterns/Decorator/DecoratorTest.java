package com.hospital.patterns.Decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DecoratorTest {
  @Test
  void testBasicRecord() {
    MedicalRecord record = new BasicMedicalRecord(1, 101, 201);
    assertEquals("Basic Medical Record", record.getRecord());
    assertTrue(record.save());
    System.out.println("✓ BasicMedicalRecord");
  }

  @Test
  void testEncryptedDecorator() {
    MedicalRecord record = new EncryptedMedicalRecordDecorator(
        new BasicMedicalRecord(1, 101, 201), "key123");
    assertTrue(record.getRecord().contains("Basic Medical Record"));
    assertTrue(record.save());
    System.out.println("✓ EncryptedMedicalRecordDecorator");
  }

  @Test
  void testSignedDecorator() {
    MedicalRecord record = new SignedMedicalRecordDecorator(
        new BasicMedicalRecord(1, 101, 201), "Vân Anh");
    assertTrue(record.getRecord().contains("Signed"));
    assertTrue(record.save());
    System.out.println("✓ SignedMedicalRecordDecorator");
  }

  @Test
  void testAuditLogDecorator() {
    MedicalRecord record = new AuditLogMedicalRecordDecorator(
        new BasicMedicalRecord(1, 101, 201), new AuditLogService());
    assertNotNull(record.getRecord());
    assertTrue(record.save());
    System.out.println("✓ AuditLogMedicalRecordDecorator");
  }
}
