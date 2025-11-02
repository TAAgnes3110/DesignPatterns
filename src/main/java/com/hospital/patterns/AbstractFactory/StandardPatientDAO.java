package com.hospital.patterns.AbstractFactory;

import java.util.ArrayList;
import java.util.List;

public class StandardPatientDAO implements PatientDAO {
  @Override
  public boolean save(Patient patient) {
    // TODO: Implement the logic to save the patient
    // Save the patient to the database
    // Return true if the patient is saved successfully, false otherwise
    System.out.println("Saving patient: " + patient.getFirstName() + " " + patient.getLastName());
    return true;
  }

  @Override
  public Patient findById(int id) {
    // TODO: Implement the logic to find the patient by id
    // Find the patient in the database
    // Return the patient if found, null otherwise
    System.out.println("Finding patient by id: " + id);
    return null;
  }

  @Override
  public List<Patient> findAll() {
    // TODO: Implement the logic to find all patients
    // Find all patients in the database
    // Return the list of patients
    System.out.println("Finding all patients");
    return new ArrayList<>();
  }

  @Override
  public boolean delete(int id) {
    // TODO: Implement the logic to delete the patient by id
    // Delete the patient from the database
    // Return true if the patient is deleted successfully, false otherwise
    System.out.println("Deleting patient by id: " + id);
    return true;
  }
}
