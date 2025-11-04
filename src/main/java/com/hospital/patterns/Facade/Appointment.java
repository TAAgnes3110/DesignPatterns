package com.hospital.patterns.Facade;

public class Appointment {
  private final int appointmentId;
  private final int patientId;
  private final int doctorId;

  public Appointment(int appointmentId, int patientId, int doctorId) {
    this.appointmentId = appointmentId;
    this.patientId = patientId;
    this.doctorId = doctorId;
  }

  public int getAppointmentId() { return appointmentId; }
  public int getPatientId() { return patientId; }
  public int getDoctorId() { return doctorId; }
}

