package com.hospital.patterns.State;

public class ConfirmedState implements AppointmentState {
  @Override
  public void handle(AppointmentContext context) {
    System.out.println("Appointment is confirmed");
  }

  @Override
  public String getStatus() {
    return "Confirmed";
  }
}

