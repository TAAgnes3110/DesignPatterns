package com.hospital.patterns.Singleton;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SingletonTest {
  @Test
  void testSingletonInstance() {
    DatabaseConnection db1 = DatabaseConnection.getInstance();
    DatabaseConnection db2 = DatabaseConnection.getInstance();
    assertSame(db1, db2);
    System.out.println("✓ Singleton: same instance");
  }
}
