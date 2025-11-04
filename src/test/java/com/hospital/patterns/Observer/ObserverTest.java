package com.hospital.patterns.Observer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import com.hospital.patterns.AbstractFactory.Appointment;

class ObserverTest {
  @Test
  void testObserverPattern() {
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(1);
    appointment.setPatientId(101);
    appointment.setDoctorId(201);

    AppointmentObservable observable = new AppointmentObservable(appointment);
    observable.attach(new PatientObserver(101));
    observable.attach(new DoctorObserver(201));

    assertDoesNotThrow(() -> observable.setStatus("Confirmed"));
    System.out.println("✓ Observer: attach & notify");
  }

  @Test
  void testAttachDetach() {
    AppointmentObservable observable = new AppointmentObservable(new Appointment());
    PatientObserver observer = new PatientObserver(101);

    observable.attach(observer);
    observable.detach(observer);
    assertDoesNotThrow(() -> observable.notifyObservers());
    System.out.println("✓ Observer: attach & detach");
  }
}
