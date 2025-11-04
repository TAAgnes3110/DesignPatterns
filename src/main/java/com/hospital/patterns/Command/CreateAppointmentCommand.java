package com.hospital.patterns.Command;

import com.hospital.patterns.AbstractFactory.Appointment;
import com.hospital.patterns.AbstractFactory.AppointmentDAO;

public class CreateAppointmentCommand implements Command {
  private final Appointment appointment;
  private final AppointmentDAO appointmentDAO;

  public CreateAppointmentCommand(Appointment appointment, AppointmentDAO appointmentDAO) {
    this.appointment = appointment;
    this.appointmentDAO = appointmentDAO;
  }

  @Override
  public boolean execute() {
    return appointmentDAO.save(appointment);
  }

  @Override
  public boolean undo() {
    return appointmentDAO.delete(appointment.getAppointmentId());
  }
}
