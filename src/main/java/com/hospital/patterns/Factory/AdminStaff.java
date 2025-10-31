package com.hospital.patterns.Factory;

public class AdminStaff implements Staff {
    private String firstName;
    private String lastName;
    private String address;

    public AdminStaff(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getJobDescription() {
        return "Nhân viên hành chính - Quản lý hồ sơ, lịch hẹn, thanh toán";
    }

    @Override
    public String performTask(String task) {
        return "Nhân viên hành chính " + getFullName() +
               " đang thực hiện: " + task;
    }

    @Override
    public String toString() {
        return "AdminStaff{" +
                "fullName='" + getFullName() + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

