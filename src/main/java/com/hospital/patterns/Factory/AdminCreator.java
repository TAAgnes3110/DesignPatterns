package com.hospital.patterns.Factory;

public class AdminCreator extends StaffCreator {

    @Override
    public Staff createStaff(String firstName, String lastName, Object... params) {
        if (params.length < 1) {
            throw new IllegalArgumentException("Admin cần: address");
        }
        return new AdminStaff(firstName, lastName, (String) params[0]);
    }

    @Override
    public String getStaffType() {
        return "Admin";
    }
}

