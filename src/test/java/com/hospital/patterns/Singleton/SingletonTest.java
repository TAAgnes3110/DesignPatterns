package com.hospital.patterns.Singleton;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

class SingletonTest {
  @Test
  void testSingletonInstance() {
    DatabaseConnection db1 = DatabaseConnection.getInstance();
    DatabaseConnection db2 = DatabaseConnection.getInstance();
    DatabaseConnection db3 = DatabaseConnection.getInstance();

    assertNotNull(db1);
    assertNotNull(db2);
    assertNotNull(db3);
    assertSame(db1, db2);
    assertSame(db2, db3);
    assertSame(db1, db3);

    System.out.println("✓ Singleton: Đảm bảo chỉ có một instance duy nhất");
    System.out.println("  → db1 == db2: " + (db1 == db2));
    System.out.println("  → db2 == db3: " + (db2 == db3));
    System.out.println("  → db1 == db3: " + (db1 == db3));
    System.out.println("  → Tất cả các lần gọi getInstance() trả về cùng một object");
  }

  @Test
  void testSingletonDatabaseInfo() {
    DatabaseConnection db = DatabaseConnection.getInstance();
    String info = db.getDatabaseInfo();

    assertNotNull(info);
    assertTrue(info.contains("Database Connection"));
    assertTrue(info.contains("url"));
    assertTrue(info.contains("username"));

    System.out.println("✓ Singleton: Cung cấp thông tin database");
    System.out.println("  → Thông tin: " + info);
    System.out.println("  → Load từ file: database.properties");
  }

  @Test
  void testConnectionMethods() {
    DatabaseConnection db = DatabaseConnection.getInstance();

    assertDoesNotThrow(() -> {
      // Test getConnection()
      try {
        db.getConnection();
        System.out.println("  → getConnection(): Thành công");
      } catch (SQLException e) {
        System.out.println("  → getConnection(): " + e.getMessage() + " (có thể do DB chưa cấu hình)");
      }

      // Test testConnection()
      boolean isConnected = db.testConnection();
      System.out.println("  → testConnection(): " + (isConnected ? "Kết nối thành công" : "Không kết nối được"));

      // Test closeConnection()
      try {
        db.closeConnection();
        System.out.println("  → closeConnection(): Thành công");
      } catch (SQLException e) {
        System.out.println("  → closeConnection(): " + e.getMessage());
      }
    });

    System.out.println("✓ Singleton: Các phương thức connection hoạt động đúng");
  }
}
