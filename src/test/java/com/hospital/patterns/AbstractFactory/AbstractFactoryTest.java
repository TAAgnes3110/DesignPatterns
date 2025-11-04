package com.hospital.patterns.AbstractFactory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AbstractFactoryTest {
  @Test
  void testStandardDAOFactory() {
    DAOFactory factory = DAOFactoryProducer.getFactory("standard");
    assertNotNull(factory.getDAOInstance("patient"));
    assertNotNull(factory.getDAOInstance("appointment"));
    System.out.println("✓ AbstractFactory: StandardDAOFactory");
  }

  @Test
  void testOptimizedDAOFactory() {
    DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
    assertNotNull(factory.getDAOInstance("patient"));
    assertNotNull(factory.getDAOInstance("appointment"));
    System.out.println("✓ AbstractFactory: OptimizedDAOFactory");
  }
}
