package com.hospital.patterns.Builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class BuilderTest {
  @Test
  void testBuilderPattern() {
    PatientBuilder builder = new StandardPatientBuilder();
    PatientDirector director = new PatientDirector(builder);

    builder.setFirstName("Vân").setLastName("Anh").setAddress("Thái Bình");
    assertNotNull(director.buildPatient());
    System.out.println("✓ Builder: PatientDirector");
  }

  @Test
  void testStandardPatientBuilder() {
    StandardPatientBuilder builder = new StandardPatientBuilder();
    builder.setFirstName("Vân").setLastName("Anh").setAddress("Thái Bình");
    assertNotNull(builder.build());
    System.out.println("✓ Builder: StandardPatientBuilder");
  }
}
