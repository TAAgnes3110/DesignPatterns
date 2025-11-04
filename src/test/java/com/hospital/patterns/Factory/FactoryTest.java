package com.hospital.patterns.Factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class FactoryTest {
  @Test
  void testDoctorCreator() {
    StaffCreator creator = new DoctorCreator();
    Staff doctor = creator.createStaff("Vân", "Anh", "Cardiology");
    assertEquals("Doctor", creator.getStaffType());
    assertTrue(doctor instanceof DoctorStaff);
    System.out.println("✓ Factory: DoctorCreator");
  }

  @Test
  void testNurseCreator() {
    StaffCreator creator = new NurseCreator();
    Staff nurse = creator.createStaff("Minh", "Hương", "ICU", "Day");
    assertEquals("Nurse", creator.getStaffType());
    assertTrue(nurse instanceof NurseStaff);
    System.out.println("✓ Factory: NurseCreator");
  }

  @Test
  void testAdminCreator() {
    StaffCreator creator = new AdminCreator();
    Staff admin = creator.createStaff("Thanh", "Tùng", "Manager");
    assertEquals("Admin", creator.getStaffType());
    assertTrue(admin instanceof AdminStaff);
    System.out.println("✓ Factory: AdminCreator");
  }
}
