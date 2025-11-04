package com.hospital.patterns.Builder;

import java.util.Date;

import com.hospital.patterns.AbstractFactory.Patient;

public class StandardPatientBuilder implements PatientBuilder {

  private final Patient patient;

  public StandardPatientBuilder() {
    this.patient = new Patient();
  }

  @Override
  public PatientBuilder setFirstName(String firstName) {
    this.patient.setFirstName(firstName);
    return this;
  }

  @Override
  public PatientBuilder setLastName(String lastName) {
    this.patient.setLastName(lastName);
    return this;
  }

  @Override
  public PatientBuilder setDateOfBirth(Date dateOfBirth) {
    this.patient.setDateOfBirth(dateOfBirth);
    return this;
  }

  @Override
  public PatientBuilder setGender(String gender) {
    this.patient.setGender(gender);
    return this;
  }

  @Override
  public PatientBuilder setContactNumber(String contactNumber) {
    this.patient.setContactNumber(contactNumber);
    return this;
  }

  @Override
  public PatientBuilder setAddress(String address) {
    this.patient.setAddress(address);
    return this;
  }

  @Override
  public PatientBuilder setEmail(String email) {
    this.patient.setEmail(email);
    return this;
  }

  @Override
  public PatientBuilder setMedicalHistory(String medicalHistory) {
    this.patient.setMedicalHistory(medicalHistory);
    return this;
  }

  @Override
  public Patient build() {
    Patient builtPatient = new Patient();
    builtPatient.setFirstName(this.patient.getFirstName());
    builtPatient.setLastName(this.patient.getLastName());
    builtPatient.setDateOfBirth(this.patient.getDateOfBirth());
    builtPatient.setGender(this.patient.getGender());
    builtPatient.setContactNumber(this.patient.getContactNumber());
    builtPatient.setAddress(this.patient.getAddress());
    builtPatient.setEmail(this.patient.getEmail());
    builtPatient.setMedicalHistory(this.patient.getMedicalHistory());
    return builtPatient;
  }
}
