package com.hospital.patterns.Facade;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HospitalFacade {
  private final PatientDAO patientDAO;
  private final AppointmentDAO appointmentDAO;
  private final BillingDAO billingDAO;

  public HospitalFacade(PatientDAO patientDAO, AppointmentDAO appointmentDAO, BillingDAO billingDAO) {
    this.patientDAO = patientDAO;
    this.appointmentDAO = appointmentDAO;
    this.billingDAO = billingDAO;
  }

  public Patient registerPatient(Map<String, Object> patientData) {
    Patient patient = new Patient(0, (String) patientData.get("firstName"), (String) patientData.get("lastName"));
    patientDAO.save(patient);
    return patient;
  }

  public Appointment bookAppointment(int patientId, int doctorId, Date date, Time time) {
    Appointment appointment = new Appointment(0, patientId, doctorId);
    appointmentDAO.save(appointment);
    return appointment;
  }

  public Billing processBilling(int appointmentId) {
    Appointment appointment = appointmentDAO.findById(appointmentId);
    Billing billing = new Billing(0, appointment.getPatientId(), new BigDecimal("100.00"));
    billingDAO.save(billing);
    return billing;
  }

  public Map<String, Object> getPatientRecords(int patientId) {
    Map<String, Object> records = new HashMap<>();
    records.put("patient", patientDAO.findById(patientId));
    return records;
  }
}

