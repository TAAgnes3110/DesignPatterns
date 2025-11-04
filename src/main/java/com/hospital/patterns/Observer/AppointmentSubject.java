package com.hospital.patterns.Observer;

public interface AppointmentSubject {
  void attach(Observer observer);
  void detach(Observer observer);
  void notifyObservers();
}

