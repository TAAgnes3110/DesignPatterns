package com.hospital.patterns.AbstractFactory;

import java.util.ArrayList;
import java.util.List;

public class OptimizedPatientDAO implements PatientDAO {
  @Override
  public boolean save(Patient patient) {
    System.out.println("Saving patient: " + patient.getFirstName() + " " + patient.getLastName());
    return true;
  }

  @Override
  public Patient findById(int id) {
    System.out.println("Finding patient by id: " + id);
    return null;
  }

  @Override
  public List<Patient> findAll() {
    System.out.println("Finding all patients");
    return new ArrayList<>();
  }

  @Override
  public boolean delete(int id) {
    System.out.println("Deleting patient by id: " + id);
    return true;
  }
}
