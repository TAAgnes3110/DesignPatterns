package com.hospital.patterns.Factory;

public abstract class StaffCreator {

    public abstract Staff createStaff(String firstName, String lastName, Object... params);

    public abstract String getStaffType();
}

