package com.hospital.patterns.Facade;

public interface PatientDAO {
  boolean save(Patient patient);
  Patient findById(int id);
}
