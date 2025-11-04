package com.hospital.patterns.Builder;

import com.hospital.patterns.AbstractFactory.Patient;

public class PatientDirector {
  private final PatientBuilder builder;

  public PatientDirector(PatientBuilder builder) {
    this.builder = builder;
  }

  public Patient buildPatient() {
    return builder.build();
  }
}
