package com.hospital.patterns.Facade;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FacadeTest {
  @Test
  void testRegisterPatient() {
    PatientDAO patientDAO = mock(PatientDAO.class);
    HospitalFacade facade = new HospitalFacade(patientDAO, mock(AppointmentDAO.class), mock(BillingDAO.class));

    Map<String, Object> data = new HashMap<>();
    data.put("firstName", "Vân");
    data.put("lastName", "Anh");

    when(patientDAO.save(any(Patient.class))).thenReturn(true);
    Patient patient = facade.registerPatient(data);

    assertEquals("Vân", patient.getFirstName());
    assertEquals("Anh", patient.getLastName());
    verify(patientDAO, times(1)).save(any(Patient.class));

    System.out.println("✓ Facade: registerPatient");
    System.out.println("  → Patient đã đăng ký: " + patient.getFirstName() + " " + patient.getLastName());
  }

  @Test
  void testBookAppointment() {
    AppointmentDAO appointmentDAO = mock(AppointmentDAO.class);
    HospitalFacade facade = new HospitalFacade(mock(PatientDAO.class), appointmentDAO, mock(BillingDAO.class));

    int patientId = 101;
    int doctorId = 201;
    Date date = new Date();
    Time time = new Time(System.currentTimeMillis());

    when(appointmentDAO.save(any(Appointment.class))).thenReturn(true);
    Appointment apt = facade.bookAppointment(patientId, doctorId, date, time);

    assertEquals(patientId, apt.getPatientId());
    assertEquals(doctorId, apt.getDoctorId());
    verify(appointmentDAO, times(1)).save(any(Appointment.class));

    System.out.println("✓ Facade: bookAppointment");
    System.out.println("  → Appointment: Patient ID " + apt.getPatientId() + ", Doctor ID " + apt.getDoctorId());
  }

  @Test
  void testProcessBilling() {
    AppointmentDAO appointmentDAO = mock(AppointmentDAO.class);
    BillingDAO billingDAO = mock(BillingDAO.class);
    HospitalFacade facade = new HospitalFacade(mock(PatientDAO.class), appointmentDAO, billingDAO);

    int appointmentId = 1;
    Appointment mockAppointment = new Appointment(appointmentId, 101, 201);

    when(appointmentDAO.findById(appointmentId)).thenReturn(mockAppointment);
    when(billingDAO.save(any(Billing.class))).thenReturn(true);

    Billing billing = facade.processBilling(appointmentId);

    assertEquals(new BigDecimal("100.00"), billing.getTotalAmount());
    assertEquals(101, billing.getPatientId());
    verify(appointmentDAO, times(1)).findById(appointmentId);
    verify(billingDAO, times(1)).save(any(Billing.class));

    System.out.println("✓ Facade: processBilling");
    System.out.println("  → Billing: Patient ID " + billing.getPatientId() + ", Amount: " + billing.getTotalAmount());
  }

  @Test
  void testGetPatientRecords() {
    PatientDAO patientDAO = mock(PatientDAO.class);
    HospitalFacade facade = new HospitalFacade(patientDAO, mock(AppointmentDAO.class), mock(BillingDAO.class));

    Patient mockPatient = new Patient(1, "Vân", "Anh");
    when(patientDAO.findById(1)).thenReturn(mockPatient);

    Map<String, Object> records = facade.getPatientRecords(1);

    assertNotNull(records);
    assertEquals(mockPatient, records.get("patient"));
    verify(patientDAO, times(1)).findById(1);

    System.out.println("✓ Facade: getPatientRecords");
    System.out.println("  → Records chứa: patient");
  }
}
