package com.hospital.patterns.AbstractFactory;

public class DAOFactoryProducer {
  private DAOFactoryProducer() {

  }

  public static DAOFactory getFactory(String factoryType) {
    if (factoryType == null) {
      return null;
    }
    switch (factoryType.toLowerCase()) {
      case "standard":
        return new StandardDAOFactory();
      case "optimized":
        return new OptimizedDAOFactory();
      default:
        return null;
    }
  }
}

