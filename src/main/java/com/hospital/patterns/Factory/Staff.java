package com.hospital.patterns.Factory;

public interface Staff {
    String getFullName();
    String getRole();
    String getJobDescription();
    String performTask(String task);
}

