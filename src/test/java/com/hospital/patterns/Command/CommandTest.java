package com.hospital.patterns.Command;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hospital.patterns.AbstractFactory.Appointment;
import com.hospital.patterns.AbstractFactory.AppointmentDAO;

class CommandTest {
  @Test
  void testCommandPattern() {
    AppointmentDAO appointmentDAO = mock(AppointmentDAO.class);
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(1);
    appointment.setPatientId(101);
    appointment.setDoctorId(201);

    Command command = new CreateAppointmentCommand(appointment, appointmentDAO);
    when(appointmentDAO.save(any(Appointment.class))).thenReturn(true);

    assertTrue(command.execute());
    System.out.println("✓ Command: execute");
  }

  @Test
  void testCommandInvoker() {
    CommandInvoker invoker = new CommandInvoker();
    AppointmentDAO appointmentDAO = mock(AppointmentDAO.class);
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(1);

    Command command = new CreateAppointmentCommand(appointment, appointmentDAO);
    when(appointmentDAO.save(any(Appointment.class))).thenReturn(true);

    invoker.setCommand(command);
    assertTrue(invoker.executeCommand());
    System.out.println("✓ Command: CommandInvoker");
  }
}
