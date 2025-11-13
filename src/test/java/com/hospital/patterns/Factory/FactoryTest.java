package com.hospital.patterns.Factory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FactoryTest {
  @Test
  void testDoctorCreator() {
    StaffCreator creator = new DoctorCreator();
    assertEquals("Doctor", creator.getStaffType());

    String specialty = "Cardiology";
    Staff doctor = creator.createStaff("Vân", "Anh", specialty);

    assertNotNull(doctor);
    assertTrue(doctor instanceof DoctorStaff);
    assertEquals("Vân Anh", doctor.getFullName());
    assertEquals("Doctor", doctor.getRole());
    assertEquals(specialty, ((DoctorStaff) doctor).getSpecialty());

    System.out.println("✓ Factory: DoctorCreator");
    System.out.println("  → Staff type: " + creator.getStaffType());
    System.out.println("  → Tạo DoctorStaff: " + doctor.getFullName());
    System.out.println("  → Chuyên khoa: " + specialty);
    System.out.println("  → Vai trò: " + doctor.getRole());
  }

  @Test
  void testNurseCreator() {
    StaffCreator creator = new NurseCreator();
    assertEquals("Nurse", creator.getStaffType());

    String specialization = "ICU";
    String shiftHours = "Day";
    Staff nurse = creator.createStaff("Minh", "Hương", specialization, shiftHours);

    assertNotNull(nurse);
    assertTrue(nurse instanceof NurseStaff);
    assertEquals("Minh Hương", nurse.getFullName());
    assertEquals("Nurse", nurse.getRole());
    assertEquals(specialization, ((NurseStaff) nurse).getSpecialization());
    assertEquals(shiftHours, ((NurseStaff) nurse).getShiftHours());

    System.out.println("✓ Factory: NurseCreator");
    System.out.println("  → Staff type: " + creator.getStaffType());
    System.out.println("  → Tạo NurseStaff: " + nurse.getFullName());
    System.out.println("  → Chuyên môn: " + specialization);
    System.out.println("  → Ca trực: " + shiftHours);
  }

  @Test
  void testAdminCreator() {
    StaffCreator creator = new AdminCreator();
    assertEquals("Admin", creator.getStaffType());

    String department = "Manager";
    Staff admin = creator.createStaff("Thanh", "Tùng", department);

    assertNotNull(admin);
    assertTrue(admin instanceof AdminStaff);
    assertEquals("Thanh Tùng", admin.getFullName());
    assertEquals("Admin", admin.getRole());
    assertEquals(department, ((AdminStaff) admin).getAddress());

    System.out.println("✓ Factory: AdminCreator");
    System.out.println("  → Staff type: " + creator.getStaffType());
    System.out.println("  → Tạo AdminStaff: " + admin.getFullName());
    System.out.println("  → Phòng ban: " + department);
    System.out.println("  → Vai trò: " + admin.getRole());
  }

  @Test
  void testFactoryMethodPattern() {
    // Test tính đa hình của Factory Method
    StaffCreator[] creators = {
        new DoctorCreator(),
        new NurseCreator(),
        new AdminCreator()
    };

    System.out.println("✓ Factory: Factory Method Pattern");
    for (StaffCreator creator : creators) {
      Staff staff = creator.getStaffType().equals("Nurse")
          ? creator.createStaff("Test", "User", "ICU", "Day")
          : creator.createStaff("Test", "User", "Param");

      assertEquals(creator.getStaffType(), staff.getRole());
      System.out.println("  → " + creator.getStaffType() + "Creator tạo " + staff.getRole() + " thành công");
    }
  }
}
