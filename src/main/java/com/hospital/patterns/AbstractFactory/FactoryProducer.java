package com.hospital.patterns.AbstractFactory;

public final class FactoryProducer {
    private FactoryProducer() {
        throw new AssertionError("Cannot instantiate FactoryProducer");
    }
    public static AbstractFactory getFactory(String factoryType) {
        switch (factoryType) {
            case "Factory1":
                return new ConcreteFactory1();
            case "Factory2":
                return new ConcreteFactory2();
            default:
                return null;
        }
    }
}

