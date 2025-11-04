package com.hospital.patterns.Observer;

import com.hospital.patterns.AbstractFactory.Appointment;

public class SMSNotificationObserver implements Observer {
  private final String phoneNumber;

  public SMSNotificationObserver(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public void update(Appointment appointment) {
    System.out.println("SMS sent to " + phoneNumber + ": Appointment " + appointment.getAppointmentId() + " updated");
  }
}

