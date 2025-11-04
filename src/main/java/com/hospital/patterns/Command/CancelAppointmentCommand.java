package com.hospital.patterns.Command;

import com.hospital.patterns.AbstractFactory.Appointment;
import com.hospital.patterns.AbstractFactory.AppointmentDAO;

public class CancelAppointmentCommand implements Command {
  private final Appointment appointment;
  private final AppointmentDAO appointmentDAO;
  private String previousStatus;

  public CancelAppointmentCommand(Appointment appointment, AppointmentDAO appointmentDAO) {
    this.appointment = appointment;
    this.appointmentDAO = appointmentDAO;
  }

  @Override
  public boolean execute() {
    previousStatus = appointment.getStatus();
    appointment.setStatus("Cancelled");
    return appointmentDAO.save(appointment);
  }

  @Override
  public boolean undo() {
    if (previousStatus != null) {
      appointment.setStatus(previousStatus);
      return appointmentDAO.save(appointment);
    }
    return false;
  }
}

