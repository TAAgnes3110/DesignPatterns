package com.hospital.patterns.Observer;

import com.hospital.patterns.AbstractFactory.Appointment;

public class PatientObserver implements Observer {
  private final int patientId;

  public PatientObserver(int patientId) {
    this.patientId = patientId;
  }

  @Override
  public void update(Appointment appointment) {
    if (appointment.getPatientId() == patientId) {
      System.out.println("Patient " + patientId + " notified: Appointment updated");
    }
  }
}

