package com.hospital.patterns.AbstractFactory;

import java.sql.Timestamp;
import java.util.Date;

public class Patient {
  private int patientId;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private String gender;
  private String contactNumber;
  private String address;
  private String email;
  private String medicalHistory;
  private Timestamp createdAt;

  public Patient() {
  }

  public Patient(int patientId, String firstName, String lastName, Date dateOfBirth,
                 String gender, String contactNumber, String address, String email,
                 String medicalHistory, Timestamp createdAt) {
    this.patientId = patientId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.contactNumber = contactNumber;
    this.address = address;
    this.email = email;
    this.medicalHistory = medicalHistory;
    this.createdAt = createdAt;
  }

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMedicalHistory() {
    return medicalHistory;
  }

  public void setMedicalHistory(String medicalHistory) {
    this.medicalHistory = medicalHistory;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "Patient{" +
        "patientId=" + patientId +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        ", gender='" + gender + '\'' +
        ", contactNumber='" + contactNumber + '\'' +
        ", address='" + address + '\'' +
        ", email='" + email + '\'' +
        ", medicalHistory='" + medicalHistory + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
