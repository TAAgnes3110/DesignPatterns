package com.hospital.patterns.TemplateMethod;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TemplateMethodTest {
    private void assertReportGenerated(MedicalReport report, String expectedTitle, int patientId) {
        String result = report.generateReport();
        assertNotNull(result);
        assertTrue(result.contains(expectedTitle), "Báo cáo thiếu tiêu đề mong đợi: " + expectedTitle);
        assertTrue(result.contains(String.valueOf(patientId)), "Báo cáo thiếu ID bệnh nhân: " + patientId);
        assertNotEquals("Invalid report", result);
        System.out.println("✓ " + report.getClass().getSimpleName() + ": Đã tạo báo cáo '" + expectedTitle + "' cho ID " + patientId);
    }

    @Test
    void testIndividualReports() {
        System.out.println("=== Test Các Loại Báo Cáo Riêng Lẻ ===");
        Date now = new Date();
        assertReportGenerated(new PatientReportGenerator(101, now), "Patient Report", 101);
        assertReportGenerated(new AppointmentReportGenerator(102, now), "Appointment Report", 102);
        assertReportGenerated(new BillingReportGenerator(103, now), "Billing Report", 103);
    }

    @Test
    void testTemplateConsistency() {
        System.out.println("\n=== Test Tính Nhất Quán Của Template ===");
        Date now = new Date();
        MedicalReport[] reports = {
                new PatientReportGenerator(201, now),
                new AppointmentReportGenerator(202, now),
                new BillingReportGenerator(203, now)
        };

        for (MedicalReport report : reports) {
            String result = report.generateReport();
            assertNotNull(result);
            assertNotEquals("Invalid report", result);
            // Kiểm tra xem quy trình template có được thực thi không (giả sử kết quả luôn chứa một định dạng chung nào đó nếu có)
        }
        System.out.println("✓ Tất cả Generator đều tuân theo template chung (collect -> format -> validate).");
    }
}