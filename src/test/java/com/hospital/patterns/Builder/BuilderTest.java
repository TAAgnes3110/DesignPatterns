package com.hospital.patterns.Builder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;
import com.hospital.patterns.AbstractFactory.Patient;

class BuilderTest {
  @Test
  void testBuilderPattern() {
    PatientBuilder builder = new StandardPatientBuilder();
    builder.setFirstName("Vân")
           .setLastName("Anh")
           .setAddress("Thái Bình")
           .setEmail("vananh@example.com")
           .setContactNumber("0123456789");

    Patient patient = new PatientDirector(builder).buildPatient();

    assertEquals("Vân", patient.getFirstName());
    assertEquals("Anh", patient.getLastName());
    assertEquals("Thái Bình", patient.getAddress());
    assertEquals("vananh@example.com", patient.getEmail());
    assertEquals("0123456789", patient.getContactNumber());

    System.out.println("✓ Builder: PatientDirector tạo patient với đầy đủ thông tin");
    System.out.println("  → Tên: " + patient.getFirstName() + " " + patient.getLastName());
    System.out.println("  → Địa chỉ: " + patient.getAddress());
    System.out.println("  → Email: " + patient.getEmail());
  }

  @Test
  void testStandardPatientBuilder() {
    Patient patient = new StandardPatientBuilder()
        .setFirstName("Vân")
        .setLastName("Anh")
        .setEmail("vananh@example.com")
        .setGender("Nữ")
        .setDateOfBirth(new Date())
        .setMedicalHistory("Không có tiền sử bệnh")
        .build();

    assertEquals("Vân", patient.getFirstName());
    assertEquals("Anh", patient.getLastName());
    assertEquals("vananh@example.com", patient.getEmail());
    assertEquals("Nữ", patient.getGender());
    assertNotNull(patient.getDateOfBirth());
    assertEquals("Không có tiền sử bệnh", patient.getMedicalHistory());

    System.out.println("✓ Builder: StandardPatientBuilder hỗ trợ fluent interface");
    System.out.println("  → Đã set " + 6 + " thuộc tính: firstName, lastName, email, gender, dateOfBirth, medicalHistory");
  }

  @Test
  void testBuilderPartialData() {
    // Test với dữ liệu một phần
    Patient patient = new StandardPatientBuilder()
        .setFirstName("Vân")
        .setLastName("Anh")
        .build();

    assertEquals("Vân", patient.getFirstName());
    assertEquals("Anh", patient.getLastName());
    assertNull(patient.getAddress());
    assertNull(patient.getEmail());

    System.out.println("✓ Builder: Có thể tạo patient với dữ liệu một phần");
    System.out.println("  → Chỉ set firstName và lastName, các trường khác null (hợp lệ)");
  }
}
