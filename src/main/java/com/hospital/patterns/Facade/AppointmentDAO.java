package com.hospital.patterns.Facade;

public interface AppointmentDAO {
  boolean save(Appointment appointment);
  Appointment findById(int id);
}

