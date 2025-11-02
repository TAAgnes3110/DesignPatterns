package com.hospital.patterns.AbstractFactory;

public interface DAOFactory {
  Object getDAOInstance(String daoType);
}
