package com.hospital.patterns.AbstractFactory;

public class OptimizedDAOFactory implements DAOFactory {
  @Override
  public Object getDAOInstance(String daoType) {
    return switch (daoType.toLowerCase()) {
      case "patient" -> new OptimizedPatientDAO();
      case "appointment" -> new OptimizedAppointmentDAO();
      default -> null;
    };
  }
}

