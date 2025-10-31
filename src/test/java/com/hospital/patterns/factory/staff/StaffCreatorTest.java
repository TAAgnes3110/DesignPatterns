package com.hospital.patterns.factory.staff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hospital.patterns.Factory.AdminCreator;
import com.hospital.patterns.Factory.AdminStaff;
import com.hospital.patterns.Factory.DoctorCreator;
import com.hospital.patterns.Factory.DoctorStaff;
import com.hospital.patterns.Factory.NurseCreator;
import com.hospital.patterns.Factory.NurseStaff;
import com.hospital.patterns.Factory.Staff;
import com.hospital.patterns.Factory.StaffCreator;

@DisplayName("Factory Method Pattern Tests cho Staff")
class StaffCreatorTest {

    @Test
    @DisplayName("Tạo Doctor bằng DoctorCreator (Factory Method Pattern)")
    void testTaoDoctorTuCreator() {
        StaffCreator creator = new DoctorCreator();
        Staff doctor = creator.createStaff(
            "Nguyễn", "Văn A",
            "Tim mạch"
        );

        assertNotNull(doctor);
        assertTrue(doctor instanceof DoctorStaff);
        assertEquals("Doctor", doctor.getRole());
        assertEquals("Nguyễn Văn A", doctor.getFullName());
        assertEquals("Bác sĩ chuyên khoa Tim mạch - Chẩn đoán và điều trị bệnh nhân",
                     doctor.getJobDescription());
    }

    @Test
    @DisplayName("Tạo Nurse bằng NurseCreator (Factory Method Pattern)")
    void testTaoNurseTuCreator() {
        StaffCreator creator = new NurseCreator();
        Staff nurse = creator.createStaff(
            "Trần", "Thị B",
            "ICU",
            "Ca ngày (8h-16h)"
        );

        assertNotNull(nurse);
        assertTrue(nurse instanceof NurseStaff);
        assertEquals("Nurse", nurse.getRole());
        assertEquals("Trần Thị B", nurse.getFullName());
        assertTrue(nurse.getJobDescription().contains("ICU"));
    }

    @Test
    @DisplayName("Tạo Admin bằng AdminCreator (Factory Method Pattern)")
    void testTaoAdminTuCreator() {
        StaffCreator creator = new AdminCreator();
        Staff admin = creator.createStaff(
            "Lê", "Văn C",
            "123 Đường ABC"
        );

        assertNotNull(admin);
        assertTrue(admin instanceof AdminStaff);
        assertEquals("Admin", admin.getRole());
        assertEquals("Lê Văn C", admin.getFullName());
        assertTrue(admin.getJobDescription().contains("hành chính"));
    }

    @Test
    @DisplayName("Test performTask cho Doctor")
    void testPerformTaskDoctor() {
        StaffCreator creator = new DoctorCreator();
        Staff doctor = creator.createStaff(
            "Nguyễn", "Văn A",
            "Tim mạch"
        );

        String result = doctor.performTask("Khám bệnh cho bệnh nhân số 123");
        assertNotNull(result);
        assertTrue(result.contains("Bác sĩ"));
        assertTrue(result.contains("Khám bệnh cho bệnh nhân số 123"));
        assertTrue(result.contains("Tim mạch"));
    }

    @Test
    @DisplayName("Test performTask cho Nurse")
    void testPerformTaskNurse() {
        StaffCreator creator = new NurseCreator();
        Staff nurse = creator.createStaff(
            "Trần", "Thị B",
            "ICU",
            "Ca đêm (22h-6h)"
        );

        String result = nurse.performTask("Tiêm thuốc cho bệnh nhân");
        assertNotNull(result);
        assertTrue(result.contains("Điều dưỡng"));
        assertTrue(result.contains("Tiêm thuốc cho bệnh nhân"));
    }

    @Test
    @DisplayName("Test Creator với tham số không đủ - ném Exception")
    void testCreatorThieuThamSo() {
        StaffCreator creator = new DoctorCreator();
        assertThrows(IllegalArgumentException.class, () -> {
            creator.createStaff(
                "Nguyễn", "Văn A"
            );
        });
    }

    @Test
    @DisplayName("Test getStaffType cho các Creators")
    void testGetStaffType() {
        assertEquals("Doctor", new DoctorCreator().getStaffType());
        assertEquals("Nurse", new NurseCreator().getStaffType());
        assertEquals("Admin", new AdminCreator().getStaffType());
    }

    @Test
    @DisplayName("Test toString cho các loại Staff")
    void testToStringCacLoaiStaff() {
        StaffCreator creator = new DoctorCreator();
        Staff doctor = creator.createStaff(
            "Nguyễn", "Văn A",
            "Tim mạch"
        );

        String toString = doctor.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("DoctorStaff"));
        assertTrue(toString.contains("Nguyễn Văn A"));
    }

}

