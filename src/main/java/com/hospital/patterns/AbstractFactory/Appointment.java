package com.hospital.patterns.AbstractFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Appointment {
  private int appointmentId;
  private int patientId;
  private int doctorId;
  private Date appointmentDate;
  private Time appointmentTime;
  private String purpose;
  private String status;
  private Timestamp createdAt;

  public Appointment() {
  }

  public Appointment(int appointmentId, int patientId, int doctorId, Date appointmentDate,
                     Time appointmentTime, String purpose, String status, Timestamp createdAt) {
    this.appointmentId = appointmentId;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.appointmentDate = appointmentDate;
    this.appointmentTime = appointmentTime;
    this.purpose = purpose;
    this.status = status;
    this.createdAt = createdAt;
  }

  public int getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public int getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(int doctorId) {
    this.doctorId = doctorId;
  }

  public Date getAppointmentDate() {
    return appointmentDate;
  }

  public void setAppointmentDate(Date appointmentDate) {
    this.appointmentDate = appointmentDate;
  }

  public Time getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(Time appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "Appointment{" +
        "appointmentId=" + appointmentId +
        ", patientId=" + patientId +
        ", doctorId=" + doctorId +
        ", appointmentDate=" + appointmentDate +
        ", appointmentTime=" + appointmentTime +
        ", purpose='" + purpose + '\'' +
        ", status='" + status + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
