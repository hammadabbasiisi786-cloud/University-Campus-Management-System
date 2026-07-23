package com.campus.backend.academicunit;

public class Computer extends Equipment {

    // CONSTRUCTORS
    public Computer() {
        super();
    }

    public Computer(String name, int operationalCost) {
        super("Computer", name, operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return super.toString().replace("=== Equipment ===", "=== Computer ===");
    }
}

