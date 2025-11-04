package com.hospital.patterns.Builder;

import java.util.Date;

import com.hospital.patterns.AbstractFactory.Patient;

public interface PatientBuilder {
  PatientBuilder setFirstName(String firstName);
  PatientBuilder setLastName(String lastName);
  PatientBuilder setDateOfBirth(Date dateOfBirth);
  PatientBuilder setGender(String gender);
  PatientBuilder setContactNumber(String contactNumber);
  PatientBuilder setAddress(String address);
  PatientBuilder setEmail(String email);
  PatientBuilder setMedicalHistory(String medicalHistory);
  Patient build();
}
