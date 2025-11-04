package com.hospital.patterns.Facade;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    System.out.println("✓ Facade: registerPatient");
  }

  @Test
  void testGetPatientRecords() {
    PatientDAO patientDAO = mock(PatientDAO.class);
    HospitalFacade facade = new HospitalFacade(patientDAO, mock(AppointmentDAO.class), mock(BillingDAO.class));
    Patient mockPatient = new Patient(1, "Vân", "Anh");

    when(patientDAO.findById(1)).thenReturn(mockPatient);
    Map<String, Object> records = facade.getPatientRecords(1);

    assertEquals(mockPatient, records.get("patient"));
    System.out.println("✓ Facade: getPatientRecords");
  }
}
