package com.hospital.patterns.AbstractFactory;

public class StandardDAOFactory implements DAOFactory {
  @Override
  public Object getDAOInstance(String daoType) {
    switch (daoType.toLowerCase()) {
      case "patient":
        return new StandardPatientDAO();
      case "appointment":
        return new StandardAppointmentDAO();
      default:
        return null;
    }
  }
}

