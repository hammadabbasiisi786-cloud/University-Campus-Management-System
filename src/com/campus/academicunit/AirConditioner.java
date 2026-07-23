package com.campus.academicunit;

public class AirConditioner extends Equipment {

    // CONSTRUCTORS
    public AirConditioner() {
        super();
    }

    public AirConditioner(String name, int operationalCost) {
        super("Air Conditioner", name, operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return super.toString().replace("=== Equipment ===", "=== Air Conditioner ===");
    }
}
