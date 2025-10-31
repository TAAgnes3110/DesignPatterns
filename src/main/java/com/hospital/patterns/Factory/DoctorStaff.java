package com.hospital.patterns.Factory;

public class DoctorStaff implements Staff {
    private String firstName;
    private String lastName;
    private String specialty;

    public DoctorStaff(String firstName, String lastName, String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    public String getSpecialty() {
        return specialty;
    }

    @Override
    public String getJobDescription() {
        return "Bác sĩ chuyên khoa " + specialty +
               " - Chẩn đoán và điều trị bệnh nhân";
    }

    @Override
    public String performTask(String task) {
        return "Bác sĩ " + getFullName() + " đang thực hiện: " + task +
               " (Chuyên khoa: " + specialty + ")";
    }

    @Override
    public String toString() {
        return "DoctorStaff{" +
                "fullName='" + getFullName() + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}

