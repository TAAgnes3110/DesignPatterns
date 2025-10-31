package com.hospital.patterns.Factory;

/**
 * Concrete Creator - tạo NurseStaff
 */
public class NurseCreator extends StaffCreator {

    @Override
    public Staff createStaff(String firstName, String lastName, Object... params) {
        if (params.length < 2) {
            throw new IllegalArgumentException("Nurse cần: specialization, shiftHours");
        }
        return new NurseStaff(firstName, lastName,
            (String) params[0], (String) params[1]);
    }

    @Override
    public String getStaffType() {
        return "Nurse";
    }
}

