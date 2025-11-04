package com.hospital.patterns.Observer;

import com.hospital.patterns.AbstractFactory.Appointment;

public class DoctorObserver implements Observer {
  private final int doctorId;

  public DoctorObserver(int doctorId) {
    this.doctorId = doctorId;
  }

  @Override
  public void update(Appointment appointment) {
    if (appointment.getDoctorId() == doctorId) {
      System.out.println("Doctor " + doctorId + " notified: Appointment updated");
    }
  }
}

