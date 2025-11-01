package com.hospital.patterns.AbstractFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hospital.patterns.Singleton.DatabaseConnection;

public class PatientDAOImpl2 implements PatientDAO {
    private final DatabaseConnection dbConnection;

    public PatientDAOImpl2() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean save(Patient patient) {
        // Implementation 2: Alternative approach - sorted by name in findAll
        String sql = "INSERT INTO Patients (first_name, last_name, date_of_birth, gender, " +
                    "contact_number, address, email, medical_history, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setDate(3, new java.sql.Date(patient.getDateOfBirth().getTime()));
            pstmt.setString(4, patient.getGender());
            pstmt.setString(5, patient.getContactNumber());
            pstmt.setString(6, patient.getAddress());
            pstmt.setString(7, patient.getEmail());
            pstmt.setString(8, patient.getMedicalHistory());
            pstmt.setTimestamp(9, patient.getCreatedAt());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving patient (Impl2): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Patient findById(int id) {
        String sql = "SELECT * FROM Patients WHERE patient_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPatient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding patient by ID (Impl2): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Patient> findAll() {
        // Implementation 2: Different sorting - by last name, first name
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients ORDER BY last_name, first_name";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all patients (Impl2): " + e.getMessage());
        }
        return patients;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Patients WHERE patient_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting patient (Impl2): " + e.getMessage());
            return false;
        }
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setDateOfBirth(new java.util.Date(rs.getDate("date_of_birth").getTime()));
        patient.setGender(rs.getString("gender"));
        patient.setContactNumber(rs.getString("contact_number"));
        patient.setAddress(rs.getString("address"));
        patient.setEmail(rs.getString("email"));
        patient.setMedicalHistory(rs.getString("medical_history"));
        patient.setCreatedAt(rs.getTimestamp("created_at"));
        return patient;
    }
}

