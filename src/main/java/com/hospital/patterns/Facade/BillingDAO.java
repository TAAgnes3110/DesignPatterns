package com.hospital.patterns.Facade;

public interface BillingDAO {
  boolean save(Billing billing);
  Billing findById(int id);
}

