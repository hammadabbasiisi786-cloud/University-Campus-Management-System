package com.campus.academicunit;

public class Fan extends Equipment {

    // CONSTRUCTORS
    public Fan() {
        super();
    }

    public Fan(String name, int operationalCost) {
        super("Fan", name, operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return super.toString().replace("=== Equipment ===", "=== Fan ===");
    }
}
