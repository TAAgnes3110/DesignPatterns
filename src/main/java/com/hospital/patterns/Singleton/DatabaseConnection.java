package com.hospital.patterns.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
  private static volatile DatabaseConnection instance;
  private Connection connection;
  private String url;
  private String username;
  private String password;
  private String driver;

  private DatabaseConnection() {
    loadDatabaseProperties();
    initializeDataSource();
  }

  private DatabaseConnection(String url, String username, String password, String driver) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.driver = driver;
    initializeDataSource();
  }

  /**
   * Lấy instance của DatabaseConnection
   * @return instance của DatabaseConnection
   */
  public static DatabaseConnection getInstance() {
    DatabaseConnection result = instance;
    if (result == null) {
      synchronized (DatabaseConnection.class) {
        result = instance;
        if (result == null) {
          result = new DatabaseConnection();
          instance = result;
        }
      }
    }
    return result;
  }

  /**
   * Ngăn việc tạo nhiều instance của DatabaseConnection
   * @param url url của database
   * @param username username của database
   * @param password password của database
   * @param driver driver của database
   * @return instance của DatabaseConnection
   */
  public static DatabaseConnection getInstance(String url, String username, String password, String driver) {
    DatabaseConnection result = instance;
    if (result == null) {
      synchronized (DatabaseConnection.class) {
        result = instance;
        if (result == null) {
          result = new DatabaseConnection(url, username, password, driver);
          instance = result;
        }
      }
    }
    return result;
  }

  /**
   * Lấy connection từ database
   * @return connection từ database
   * @throws SQLException nếu không thể lấy connection
   */
  public Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      connection = DriverManager.getConnection(url, username, password);
    }
    return connection;
  }

  /**
   * Đóng connection từ database
   * @throws SQLException nếu không thể đóng connection
   */
  public void closeConnection() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
      connection = null;
    }
  }

  /**
   * Kiểm tra kết nối tới database
   * @return true nếu kết nối tới database, false nếu không
   * @throws SQLException nếu không thể kiểm tra kết nối
   */
  public boolean testConnection() {
    try {
      Connection conn = getConnection();
      return conn != null && !conn.isClosed() && conn.isValid(5);
    } catch (SQLException e) {
      System.err.println("Error testing connection: " + e.getMessage());
      return false;
    }
  }

  /**
   * Load các thông tin từ file database.properties
   * @return Properties chứa các thông tin từ file database.properties
   * @throws IOException nếu không thể load file database.properties
   */
  private void loadDatabaseProperties() {
    Properties props = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
      if (input == null) {
        throw new IOException("database.properties file not found");
      }
      props.load(input);

      url = props.getProperty("db.url");
      username = props.getProperty("db.username");
      password = props.getProperty("db.password");
      driver = props.getProperty("db.driver");

      if (url == null || username == null || password == null || driver == null) {
        throw new RuntimeException("Missing required database properties");
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load database properties", e);
    }
  }

  /**
   * Tạo kết nối tới database
   * @throws ClassNotFoundException nếu không thể load driver
   * @throws SQLException nếu không thể tạo kết nối tới database
   */
  private void initializeDataSource() {
    try {
      Class.forName(driver);
      Connection testConnection = DriverManager.getConnection(url, username, password);
      testConnection.close();

      System.out.println("Database connection established successfully");
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException("Failed to initialize database connection", e);
    }
  }

  /**
   * Trả về thông tin cấu hình của database
   * @return thông tin cấu hình của database
   */
  public String getDatabaseInfo() {
    return "Database Connection{" +
    "url='" + url + '\'' +
    ", username='" + username + '\'' +
    ", driver='" + driver + '\'' +
    '}';
  }

  /**
   * Trả về thông tin về DatabaseConnection
   * @return thông tin về DatabaseConnection
   */
  @Override
  public String toString() {
    return "DatabaseConnection{" +
    "url='" + url + '\'' +
    ", username='" + username + '\'' +
    ", driver='" + driver + '\'' +
    '}';
  }
}
