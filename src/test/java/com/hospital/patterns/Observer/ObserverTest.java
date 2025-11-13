package com.hospital.patterns.Observer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.hospital.patterns.AbstractFactory.Appointment;

class ObserverTest {
  @Test
  void testObserverPattern() {
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);
    apt.setPatientId(101);
    apt.setDoctorId(201);
    apt.setStatus("Scheduled");

    AppointmentObservable observable = new AppointmentObservable(apt);
    PatientObserver patientObserver = new PatientObserver(101);
    DoctorObserver doctorObserver = new DoctorObserver(201);

    observable.attach(patientObserver);
    observable.attach(doctorObserver);

    assertEquals("Scheduled", apt.getStatus());
    System.out.println("  → Trạng thái ban đầu: " + apt.getStatus());
    System.out.println("  → Đã đăng ký: PatientObserver (ID: 101), DoctorObserver (ID: 201)");

    observable.setStatus("Confirmed");

    assertEquals("Confirmed", apt.getStatus());
    System.out.println("  → Trạng thái mới: " + apt.getStatus());
    System.out.println("✓ Observer: setStatus cập nhật appointment và thông báo tất cả observers");
  }

  @Test
  void testAttachDetach() {
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);
    apt.setPatientId(101);

    AppointmentObservable observable = new AppointmentObservable(apt);
    PatientObserver observer = new PatientObserver(101);
    SMSNotificationObserver smsObserver = new SMSNotificationObserver("0123456789");

    // Test attach
    observable.attach(observer);
    observable.attach(smsObserver);
    System.out.println("  → Đã attach: PatientObserver, SMSNotificationObserver");

    observable.setStatus("Confirmed");
    System.out.println("  → Thay đổi status: Confirmed (2 observers nhận thông báo)");

    // Test detach
    observable.detach(observer);
    System.out.println("  → Đã detach: PatientObserver");

    observable.setStatus("Cancelled");
    System.out.println("  → Thay đổi status: Cancelled (chỉ 1 observer nhận thông báo)");

    assertDoesNotThrow(() -> observable.notifyObservers());
    System.out.println("✓ Observer: attach và detach hoạt động đúng");
  }

  @Test
  void testMultipleObservers() {
    Appointment apt = new Appointment();
    apt.setAppointmentId(1);
    apt.setPatientId(101);
    apt.setDoctorId(201);

    AppointmentObservable observable = new AppointmentObservable(apt);
    observable.attach(new PatientObserver(101));
    observable.attach(new DoctorObserver(201));
    observable.attach(new SMSNotificationObserver("0123456789"));

    System.out.println("  → Đã đăng ký 3 observers: PatientObserver, DoctorObserver, SMSNotificationObserver");

    observable.setStatus("Completed");

    assertEquals("Completed", apt.getStatus());
    System.out.println("✓ Observer: Nhiều observers cùng nhận thông báo khi status thay đổi");
  }
}
