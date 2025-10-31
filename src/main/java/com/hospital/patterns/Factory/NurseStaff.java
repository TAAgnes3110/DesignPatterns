package com.hospital.patterns.Factory;

public class NurseStaff implements Staff {
    private String firstName;
    private String lastName;
    private String specialization;
    private String shiftHours;

    public NurseStaff(String firstName, String lastName, String specialization, String shiftHours) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.shiftHours = shiftHours;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getRole() {
        return "Nurse";
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getShiftHours() {
        return shiftHours;
    }

    @Override
    public String getJobDescription() {
        return "Điều dưỡng chuyên về " + specialization +
               " - Chăm sóc bệnh nhân, ca trực: " + shiftHours;
    }

    @Override
    public String performTask(String task) {
        return "Điều dưỡng " + getFullName() + " đang thực hiện: " + task +
               " (Chuyên môn: " + specialization + ", Ca: " + shiftHours + ")";
    }

    @Override
    public String toString() {
        return "NurseStaff{" +
                "fullName='" + getFullName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", shiftHours='" + shiftHours + '\'' +
                '}';
    }
}

