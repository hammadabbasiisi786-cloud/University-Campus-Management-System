package com.campus.backend.academicunit;

public class Camera extends Equipment {

    // CONSTRUCTORS
    public Camera() {
        super();
    }

    public Camera(String name, int operationalCost) {
        super("Camera", name, operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return super.toString().replace("=== Equipment ===", "=== Camera ===");
    }
}

