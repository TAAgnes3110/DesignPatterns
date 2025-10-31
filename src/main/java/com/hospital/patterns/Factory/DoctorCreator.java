package com.hospital.patterns.Factory;

public class DoctorCreator extends StaffCreator {

    @Override
    public Staff createStaff(String firstName, String lastName, Object... params) {
        if (params.length < 1) {
            throw new IllegalArgumentException("Doctor cần: specialty");
        }
        return new DoctorStaff(firstName, lastName, (String) params[0]);
    }

    @Override
    public String getStaffType() {
        return "Doctor";
    }
}

