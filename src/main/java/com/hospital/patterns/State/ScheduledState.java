package com.hospital.patterns.State;

public class ScheduledState implements AppointmentState {
  @Override
  public void handle(AppointmentContext context) {
    System.out.println("Appointment is scheduled");
  }

  @Override
  public String getStatus() {
    return "Scheduled";
  }
}

