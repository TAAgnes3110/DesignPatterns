package com.hospital.patterns.singleton;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import com.hospital.patterns.database.DatabaseConnection;

class DatabaseConnectionTest {

  @Test
  void testSingleton_ChiCoMotInstance() {
    DatabaseConnection db1 = DatabaseConnection.getInstance();
    DatabaseConnection db2 = DatabaseConnection.getInstance();
    assertSame(db1, db2, "Phải là cùng 1 instance");
  }

  @Test
  void testKetNoiDatabase() throws Exception {
    DatabaseConnection db = DatabaseConnection.getInstance();
    Connection conn = db.getConnection();
    assertNotNull(conn, "Connection không được null");
    assertFalse(conn.isClosed(), "Connection phải đang mở");
    db.closeConnection();
  }

  @Test
  void testDongVaMoLaiConnection() throws Exception {
    DatabaseConnection db = DatabaseConnection.getInstance();

    Connection conn1 = db.getConnection();
    db.closeConnection();

    Connection conn2 = db.getConnection();
    assertNotSame(conn1, conn2, "Phải là connection mới");
    assertFalse(conn2.isClosed(), "Connection mới phải đang mở");

    db.closeConnection();
  }
}
