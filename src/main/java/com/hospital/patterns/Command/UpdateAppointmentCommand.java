package com.hospital.patterns.Command;

import java.sql.Time;
import java.util.Date;

import com.hospital.patterns.AbstractFactory.Appointment;
import com.hospital.patterns.AbstractFactory.AppointmentDAO;

public class UpdateAppointmentCommand implements Command {
  private final Appointment appointment;
  private final AppointmentDAO appointmentDAO;

  private Date previousDate;
  private Time previousTime;
  private String previousPurpose;
  private String previousStatus;

  public UpdateAppointmentCommand(Appointment appointment, AppointmentDAO appointmentDAO) {
    this.appointment = appointment;
    this.appointmentDAO = appointmentDAO;
  }

  public UpdateAppointmentCommand setDate(Date date) {
    if (previousDate == null) previousDate = appointment.getAppointmentDate();
    appointment.setAppointmentDate(date);
    return this;
  }

  public UpdateAppointmentCommand setTime(Time time) {
    if (previousTime == null) previousTime = appointment.getAppointmentTime();
    appointment.setAppointmentTime(time);
    return this;
  }

  public UpdateAppointmentCommand setPurpose(String purpose) {
    if (previousPurpose == null) previousPurpose = appointment.getPurpose();
    appointment.setPurpose(purpose);
    return this;
  }

  public UpdateAppointmentCommand setStatus(String status) {
    if (previousStatus == null) previousStatus = appointment.getStatus();
    appointment.setStatus(status);
    return this;
  }

  @Override
  public boolean execute() {
    return appointmentDAO.save(appointment);
  }

  @Override
  public boolean undo() {
    if (previousDate != null) appointment.setAppointmentDate(previousDate);
    if (previousTime != null) appointment.setAppointmentTime(previousTime);
    if (previousPurpose != null) appointment.setPurpose(previousPurpose);
    if (previousStatus != null) appointment.setStatus(previousStatus);
    return appointmentDAO.save(appointment);
  }
}

