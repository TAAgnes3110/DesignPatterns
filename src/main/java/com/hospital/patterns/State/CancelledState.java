package com.hospital.patterns.State;

public class CancelledState implements AppointmentState {
  @Override
  public void handle(AppointmentContext context) {
    System.out.println("Appointment is cancelled");
  }

  @Override
  public String getStatus() {
    return "Cancelled";
  }
}

