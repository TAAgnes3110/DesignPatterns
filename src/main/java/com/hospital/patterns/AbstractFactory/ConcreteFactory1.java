package com.hospital.patterns.AbstractFactory;

public class ConcreteFactory1 implements AbstractFactory {

    @Override
    public Object getDAOInstance(String daoType) {
        switch(daoType){
            case "PATIENT": return new PatientDAOImpl1();
            case "APPOINTMENT": return new AppointmentDAOImpl1();
        }
        return null;
    }
}

