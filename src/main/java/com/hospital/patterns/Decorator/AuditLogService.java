package com.hospital.patterns.Decorator;

public class AuditLogService {
  public void log(String action) {
    System.out.println("[AUDIT LOG] " + action);
  }
}

