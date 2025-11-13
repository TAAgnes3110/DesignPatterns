package com.hospital.patterns.Observer;

import java.util.ArrayList;
import java.util.List;

import com.hospital.patterns.AbstractFactory.Appointment;

public class AppointmentObservable implements AppointmentSubject {
  private final List<Observer> observers = new ArrayList<>();
  private final Appointment appointment;

  public AppointmentObservable(Appointment appointment) {
    this.appointment = appointment;
  }

  @Override
  public void attach(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void detach(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(appointment);
    }
  }

  public void setStatus(String status) {
    if (appointment != null) {
      appointment.setStatus(status);
    }
    notifyObservers();
  }
}

