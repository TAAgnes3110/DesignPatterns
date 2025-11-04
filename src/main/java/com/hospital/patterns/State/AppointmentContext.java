package com.hospital.patterns.State;

import com.hospital.patterns.AbstractFactory.Appointment;

public class AppointmentContext {
  private AppointmentState state;
  private Appointment appointment;

  public AppointmentContext(Appointment appointment) {
    this.appointment = appointment;
  }

  public void setState(AppointmentState state) {
    this.state = state;
  }

  public AppointmentState getState() {
    return state;
  }

  public void request() {
    if (state != null) {
      state.handle(this);
    }
  }

  public Appointment getAppointment() {
    return appointment;
  }
}

