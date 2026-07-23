package com.campus.backend.academicunit;

public class Projector extends Equipment {

    // CONSTRUCTORS
    public Projector() {
        super();
    }

    public Projector(String name, int operationalCost) {
        super("Projector", name, operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return super.toString().replace("=== Equipment ===", "=== Projector ===");
    }
}

