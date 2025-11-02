package com.hospital.patterns.AbstractFactory;

import java.util.List;

public interface AppointmentDAO {
  boolean save(Appointment appointment);
  Appointment findById(int id);
  List<Appointment> findAll();
  boolean delete(int id);
}
