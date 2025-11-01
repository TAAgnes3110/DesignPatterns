package com.hospital.patterns.AbstractFactory;

import java.util.List;

public interface PatientDAO {
  boolean save (Patient patient);
  Patient findById (int id);
  List<Patient> findAll();
  boolean delete (int id);
}
