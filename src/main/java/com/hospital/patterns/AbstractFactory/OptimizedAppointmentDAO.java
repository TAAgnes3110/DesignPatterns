package com.hospital.patterns.AbstractFactory;

import java.util.ArrayList;
import java.util.List;

public class OptimizedAppointmentDAO implements AppointmentDAO {

  @Override
  public boolean save(Appointment appointment) {
    System.out.println("Saving appointment: " + appointment.getAppointmentId());
    return true;
  }

  @Override
  public Appointment findById(int id) {
    System.out.println("Finding appointment by id: " + id);
    return null;
  }

  @Override
  public List<Appointment> findAll() {
    System.out.println("Finding all appointments");
    return new ArrayList<>();
  }

  @Override
  public boolean delete(int id) {
    System.out.println("Deleting appointment by id: " + id);
    return true;
  }
}
