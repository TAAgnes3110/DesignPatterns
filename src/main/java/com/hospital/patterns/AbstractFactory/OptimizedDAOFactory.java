package com.hospital.patterns.AbstractFactory;

public class OptimizedDAOFactory implements DAOFactory {
  @Override
  public Object getDAOInstance(String daoType) {
    switch (daoType.toLowerCase()) {
      case "patient":
        return new OptimizedPatientDAO();
      case "appointment":
        return new OptimizedAppointmentDAO();
      default:
        return null;
    }
  }
}

