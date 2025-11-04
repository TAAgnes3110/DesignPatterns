package com.hospital.patterns.Observer;

import com.hospital.patterns.AbstractFactory.Appointment;

public interface Observer {
  void update(Appointment appointment);
}

