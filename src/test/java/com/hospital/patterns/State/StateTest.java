package com.hospital.patterns.State;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.hospital.patterns.AbstractFactory.Appointment;

class StateTest {
  @Test
  void testStatePattern() {
    AppointmentContext context = new AppointmentContext(new Appointment());
    context.setState(new ScheduledState());
    assertEquals("Scheduled", context.getState().getStatus());
    assertDoesNotThrow(() -> context.request());
    System.out.println("✓ State: ScheduledState");
  }

  @Test
  void testStateTransitions() {
    AppointmentContext context = new AppointmentContext(new Appointment());
    context.setState(new ScheduledState());
    assertEquals("Scheduled", context.getState().getStatus());

    context.setState(new ConfirmedState());
    assertEquals("Confirmed", context.getState().getStatus());

    context.setState(new CompletedState());
    assertEquals("Completed", context.getState().getStatus());
    System.out.println("✓ State: State transitions");
  }
}
