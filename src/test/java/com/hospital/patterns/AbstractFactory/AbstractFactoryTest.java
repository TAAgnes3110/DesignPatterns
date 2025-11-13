package com.hospital.patterns.AbstractFactory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AbstractFactoryTest {
  @Test
  void testStandardDAOFactory() {
    DAOFactory factory = DAOFactoryProducer.getFactory("standard");
    assertNotNull(factory);
    assertTrue(factory instanceof StandardDAOFactory);

    Object patientDAO = factory.getDAOInstance("patient");
    Object appointmentDAO = factory.getDAOInstance("appointment");

    assertNotNull(patientDAO);
    assertNotNull(appointmentDAO);
    assertTrue(patientDAO instanceof StandardPatientDAO);
    assertTrue(appointmentDAO instanceof StandardAppointmentDAO);

    System.out.println("✓ AbstractFactory: StandardDAOFactory");
    System.out.println("  → Tạo StandardPatientDAO: " + (patientDAO instanceof StandardPatientDAO));
    System.out.println("  → Tạo StandardAppointmentDAO: " + (appointmentDAO instanceof StandardAppointmentDAO));
  }

  @Test
  void testOptimizedDAOFactory() {
    DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
    assertNotNull(factory);
    assertTrue(factory instanceof OptimizedDAOFactory);

    Object patientDAO = factory.getDAOInstance("patient");
    Object appointmentDAO = factory.getDAOInstance("appointment");

    assertNotNull(patientDAO);
    assertNotNull(appointmentDAO);
    assertTrue(patientDAO instanceof OptimizedPatientDAO);
    assertTrue(appointmentDAO instanceof OptimizedAppointmentDAO);

    System.out.println("✓ AbstractFactory: OptimizedDAOFactory");
    System.out.println("  → Tạo OptimizedPatientDAO: " + (patientDAO instanceof OptimizedPatientDAO));
    System.out.println("  → Tạo OptimizedAppointmentDAO: " + (appointmentDAO instanceof OptimizedAppointmentDAO));
  }

  @Test
  void testDAOFactoryProducer() {
    DAOFactory standardFactory = DAOFactoryProducer.getFactory("standard");
    DAOFactory optimizedFactory = DAOFactoryProducer.getFactory("optimized");
    DAOFactory invalidFactory = DAOFactoryProducer.getFactory("invalid");

    assertNotNull(standardFactory);
    assertNotNull(optimizedFactory);
    assertNull(invalidFactory);
    assertTrue(standardFactory instanceof StandardDAOFactory);
    assertTrue(optimizedFactory instanceof OptimizedDAOFactory);

    System.out.println("✓ AbstractFactory: DAOFactoryProducer");
    System.out.println("  → Factory type 'standard': " + standardFactory.getClass().getSimpleName());
    System.out.println("  → Factory type 'optimized': " + optimizedFactory.getClass().getSimpleName());
    System.out.println("  → Factory type 'invalid': " + (invalidFactory == null ? "null (đúng)" : "không null (sai)"));
  }

  @Test
  void testFactoryCompatibility() {
    // Kiểm tra các DAO từ cùng factory tương thích với nhau
    DAOFactory standardFactory = DAOFactoryProducer.getFactory("standard");
    Object standardPatientDAO = standardFactory.getDAOInstance("patient");
    Object standardAppointmentDAO = standardFactory.getDAOInstance("appointment");

    assertTrue(standardPatientDAO instanceof StandardPatientDAO);
    assertTrue(standardAppointmentDAO instanceof StandardAppointmentDAO);
    assertFalse(standardPatientDAO instanceof OptimizedPatientDAO);
    assertFalse(standardAppointmentDAO instanceof OptimizedAppointmentDAO);

    System.out.println("✓ AbstractFactory: Các DAO từ cùng factory tương thích với nhau");
  }
}
