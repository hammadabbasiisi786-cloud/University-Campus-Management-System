package com.campus.academicunit;

public class Equipment {

    // FIELDS
    private String type;
    private String name;
    private int operationalCost;

    // CONSTRUCTORS
    public Equipment() {}

    public Equipment(String type, String name, int operationalCost) {
        setType(type);
        setName(name);
        setOperationalCost(operationalCost);
    }

    // SETTERS

    // Type must not be null or empty
    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            System.out.println("Error: type is empty");
        } else {
            this.type = type;
        }
    }

    // Name must not be null or empty
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Error: name is empty");
        } else {
            this.name = name;
        }
    }

    // Operational cost must be greater than 0
    public void setOperationalCost(int operationalCost) {
        if (operationalCost > 0) {
            this.operationalCost = operationalCost;
        } else {
            System.out.println("Error: operational cost must be greater than 0");
        }
    }

    // GETTERS
    public String getType() { return type; }
    public String getName() { return name; }
    public int getOperationalCost() { return operationalCost; }

    // OTHER METHODS

    // Calculates the total monthly cost based on daily usage hours (cost per hour × hours per day × 30 days)
    public int calculateMonthlyCost(int hoursPerDay) {
        return operationalCost * hoursPerDay * 30;
    }

    // Prints a brief summary of this equipment's type, name, and cost
    public void displayInfo() {
        System.out.println("Type: " + type + " | Name: " + name + " | Cost/hr: " + operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Equipment ===\n" +
                        "  Type             : %s\n" +
                        "  Name             : %s\n" +
                        "  Operational Cost : %d",
                type, name, operationalCost
        );
    }
}

// -------------------------------------------------------

class Projector extends Equipment {

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

// -------------------------------------------------------

class AirConditioner extends Equipment {

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

// -------------------------------------------------------

class Computer extends Equipment {

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

// -------------------------------------------------------

class Camera extends Equipment {

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

// -------------------------------------------------------

class Fan extends Equipment {

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