package com.hospital.patterns.State;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.hospital.patterns.AbstractFactory.Appointment;

class StateTest {

    // Helper method để giảm lặp code kiểm tra trạng thái
    private void assertState(AppointmentContext context, AppointmentState expectedState, String expectedStatusName) {
        context.setState(expectedState);
        assertEquals(expectedStatusName, context.getState().getStatus(), "Tên trạng thái không khớp");
        assertInstanceOf(expectedState.getClass(), context.getState(), "Loại class trạng thái không khớp");
        assertDoesNotThrow(context::request, "Gọi request() gây lỗi ngoại lệ");
        System.out.println("  → Đã chuyển sang: " + expectedStatusName);
    }

    @Test
    void testStateTransitionsFlow() {
        System.out.println("=== Test Luồng Chuyển Đổi Trạng Thái (State Pattern) ===");
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        AppointmentContext context = new AppointmentContext(appointment);

        // Kiểm tra luồng chuyển đổi trạng thái điển hình
        assertState(context, new ScheduledState(), "Scheduled");
        assertState(context, new ConfirmedState(), "Confirmed");
        assertState(context, new CompletedState(), "Completed");

        // Test trạng thái hủy từ Completed (ví dụ minh họa, tùy logic thực tế có cho phép hay không)
        assertState(context, new CancelledState(), "Cancelled");

        System.out.println("✓ Hoàn thành luồng chuyển đổi trạng thái cơ bản.");
    }

    @Test
    void testIndividualStatesIntegrity() {
        System.out.println("\n=== Test Tính Toàn Vẹn Từng Trạng Thái ===");
        // Kiểm tra nhanh tên status của các object state độc lập
        assertEquals("Scheduled", new ScheduledState().getStatus());
        assertEquals("Confirmed", new ConfirmedState().getStatus());
        assertEquals("Completed", new CompletedState().getStatus());
        assertEquals("Cancelled", new CancelledState().getStatus());
        System.out.println("✓ Tất cả các class State trả về đúng tên status.");
    }
}