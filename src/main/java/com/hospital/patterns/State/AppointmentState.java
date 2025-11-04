package com.hospital.patterns.State;

public interface AppointmentState {
  void handle(AppointmentContext context);
  String getStatus();
}

