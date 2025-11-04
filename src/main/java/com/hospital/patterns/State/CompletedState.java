package com.hospital.patterns.State;

public class CompletedState implements AppointmentState {
  @Override
  public void handle(AppointmentContext context) {
    System.out.println("Appointment is completed");
  }

  @Override
  public String getStatus() {
    return "Completed";
  }
}

