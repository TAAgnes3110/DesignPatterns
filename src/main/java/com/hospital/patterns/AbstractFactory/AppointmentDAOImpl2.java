package com.hospital.patterns.AbstractFactory;

import com.hospital.patterns.Singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl2 implements AppointmentDAO {
    private final DatabaseConnection dbConnection;

    public AppointmentDAOImpl2() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean save(Appointment appointment) {
        // Implementation 2: With validation
        if (appointment.getPatientId() <= 0) {
            System.err.println("Invalid patient ID");
            return false;
        }

        String sql = "INSERT INTO Appointments (patient_id, doctor_id, appointment_date, " +
                    "appointment_time, purpose, status, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setDate(3, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            pstmt.setTime(4, appointment.getAppointmentTime());
            pstmt.setString(5, appointment.getPurpose());
            pstmt.setString(6, appointment.getStatus());
            pstmt.setTimestamp(7, appointment.getCreatedAt());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving appointment (Impl2): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM Appointments WHERE appointment_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointment by ID (Impl2): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Appointment> findAll() {
        // Implementation 2: Different sorting - by status and date DESC
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments ORDER BY status, appointment_date DESC";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all appointments (Impl2): " + e.getMessage());
        }
        return appointments;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Appointments WHERE appointment_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting appointment (Impl2): " + e.getMessage());
            return false;
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setAppointmentDate(new java.util.Date(rs.getDate("appointment_date").getTime()));
        appointment.setAppointmentTime(rs.getTime("appointment_time"));
        appointment.setPurpose(rs.getString("purpose"));
        appointment.setStatus(rs.getString("status"));
        appointment.setCreatedAt(rs.getTimestamp("created_at"));
        return appointment;
    }
}

