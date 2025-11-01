package com.hospital.patterns.AbstractFactory;

public class ConcreteFactory2 implements AbstractFactory {

    @Override
    public Object getDAOInstance(String daoType) {
        switch(daoType){
            case "PATIENT": return new PatientDAOImpl2();
            case "APPOINTMENT": return new AppointmentDAOImpl2();
        }
        return null;
    }
}

