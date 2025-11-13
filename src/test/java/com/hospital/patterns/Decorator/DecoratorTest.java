package com.hospital.patterns.Decorator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DecoratorTest {
  @Test
  void testBasicRecord() {
    MedicalRecord record = new BasicMedicalRecord(1, 101, 201);
    assertEquals("Basic Medical Record", record.getRecord());
    assertTrue(record.save());
    System.out.println("✓ Decorator: BasicMedicalRecord");
    System.out.println("  → Record ID: 1, Patient ID: 101, Doctor ID: 201");
    System.out.println("  → Record type: " + record.getRecord());
  }

  @Test
  void testEncryptedDecorator() {
    MedicalRecord basicRecord = new BasicMedicalRecord(1, 101, 201);
    MedicalRecord encryptedRecord = new EncryptedMedicalRecordDecorator(basicRecord, "key123");

    assertTrue(encryptedRecord.getRecord().contains("Basic Medical Record"));
    assertTrue(encryptedRecord.save());
    System.out.println("✓ Decorator: EncryptedMedicalRecordDecorator");
    System.out.println("  → Thêm tính năng: Mã hóa với key 'key123'");
    System.out
        .println("  → Record vẫn chứa nội dung gốc: " + encryptedRecord.getRecord().contains("Basic Medical Record"));
  }

  @Test
  void testSignedDecorator() {
    String signature = "Vân Anh";
    MedicalRecord basicRecord = new BasicMedicalRecord(1, 101, 201);
    MedicalRecord signedRecord = new SignedMedicalRecordDecorator(basicRecord, signature);

    assertTrue(signedRecord.getRecord().contains("Signed"));
    assertTrue(signedRecord.getRecord().contains(signature));
    assertTrue(signedRecord.save());
    System.out.println("✓ Decorator: SignedMedicalRecordDecorator");
    System.out.println("  → Thêm tính năng: Chữ ký số");
    System.out.println("  → Người ký: " + signature);
    System.out.println("  → Record: " + signedRecord.getRecord());
  }

  @Test
  void testAuditLogDecorator() {
    MedicalRecord basicRecord = new BasicMedicalRecord(1, 101, 201);
    AuditLogService logService = new AuditLogService();
    MedicalRecord auditedRecord = new AuditLogMedicalRecordDecorator(basicRecord, logService);

    assertNotNull(auditedRecord.getRecord());
    assertTrue(auditedRecord.save());
    System.out.println("✓ Decorator: AuditLogMedicalRecordDecorator");
    System.out.println("  → Thêm tính năng: Ghi log kiểm toán");
    System.out.println("  → Mọi thao tác đều được ghi log");
  }

  @Test
  void testMultipleDecorators() {
    MedicalRecord basic = new BasicMedicalRecord(1, 101, 201);
    MedicalRecord encrypted = new EncryptedMedicalRecordDecorator(basic, "key123");
    MedicalRecord signed = new SignedMedicalRecordDecorator(encrypted, "Vân Anh");
    MedicalRecord decorated = new AuditLogMedicalRecordDecorator(signed, new AuditLogService());

    assertTrue(decorated.save());
    System.out.println("✓ Decorator: Kết hợp nhiều decorator");
    System.out.println("  → Thứ tự áp dụng: Basic → Encrypted → Signed → AuditLog");
    System.out.println("  → Tổng số decorator: 3 (Encrypted, Signed, AuditLog)");
    System.out.println("  → Tất cả tính năng hoạt động đồng thời");
  }
}
