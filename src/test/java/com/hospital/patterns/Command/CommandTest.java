package com.hospital.patterns.Command;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.hospital.patterns.AbstractFactory.Appointment;
import com.hospital.patterns.AbstractFactory.AppointmentDAO;

class CommandTest {
  @Test
  void testCreateAppointmentCommand() {
    AppointmentDAO dao = mock(AppointmentDAO.class);
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);
    apt.setPatientId(101);
    apt.setDoctorId(201);

    Command cmd = new CreateAppointmentCommand(apt, dao);
    when(dao.save(any(Appointment.class))).thenReturn(true);
    when(dao.delete(anyInt())).thenReturn(true);

    // Test execute
    assertTrue(cmd.execute());
    verify(dao, times(1)).save(any(Appointment.class));
    System.out.println("  → Execute: Tạo appointment ID " + apt.getAppointmentId());

    // Test undo
    assertTrue(cmd.undo());
    verify(dao, times(1)).delete(1);
    System.out.println("  → Undo: Xóa appointment ID " + apt.getAppointmentId());

    System.out.println("✓ Command: CreateAppointmentCommand - execute và undo hoạt động đúng");
  }

  @Test
  void testUpdateAppointmentCommand() {
    AppointmentDAO dao = mock(AppointmentDAO.class);
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);
    apt.setStatus("Scheduled");

    UpdateAppointmentCommand cmd = new UpdateAppointmentCommand(apt, dao);
    when(dao.save(any(Appointment.class))).thenReturn(true);

    System.out.println("  → Trạng thái ban đầu: " + apt.getStatus());

    cmd.setStatus("Confirmed");
    assertTrue(cmd.execute());
    assertEquals("Confirmed", apt.getStatus());
    verify(dao, times(1)).save(any(Appointment.class));
    System.out.println("  → Execute: Cập nhật status thành " + apt.getStatus());

    assertTrue(cmd.undo());
    assertEquals("Scheduled", apt.getStatus());
    verify(dao, times(2)).save(any(Appointment.class));
    System.out.println("  → Undo: Khôi phục status về " + apt.getStatus());

    System.out.println("✓ Command: UpdateAppointmentCommand - cập nhật và hoàn tác đúng");
  }

  @Test
  void testCancelAppointmentCommand() {
    AppointmentDAO dao = mock(AppointmentDAO.class);
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);
    apt.setStatus("Confirmed");

    CancelAppointmentCommand cmd = new CancelAppointmentCommand(apt, dao);
    when(dao.save(any(Appointment.class))).thenReturn(true);

    System.out.println("  → Trạng thái ban đầu: " + apt.getStatus());

    assertTrue(cmd.execute());
    assertEquals("Cancelled", apt.getStatus());
    System.out.println("  → Execute: Hủy appointment, status = " + apt.getStatus());

    assertTrue(cmd.undo());
    assertEquals("Confirmed", apt.getStatus());
    System.out.println("  → Undo: Khôi phục status về " + apt.getStatus());

    System.out.println("✓ Command: CancelAppointmentCommand - hủy và hoàn tác đúng");
  }

  @Test
  void testCommandInvoker() {
    AppointmentDAO dao = mock(AppointmentDAO.class);
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);

    Command cmd = new CreateAppointmentCommand(apt, dao);
    when(dao.save(any(Appointment.class))).thenReturn(true);
    when(dao.delete(anyInt())).thenReturn(true);

    CommandInvoker invoker = new CommandInvoker();
    invoker.setCommand(cmd);

    assertFalse(invoker.hasHistory());
    assertTrue(invoker.executeCommand());
    assertTrue(invoker.hasHistory());
    System.out.println("  → Execute command: History có " + (invoker.hasHistory() ? "1" : "0") + " command");

    assertTrue(invoker.undo());
    assertFalse(invoker.hasHistory());
    System.out.println("  → Undo: History còn " + (invoker.hasHistory() ? "1" : "0") + " command");

    System.out.println("✓ Command: CommandInvoker quản lý command history và undo đúng");
  }
}
