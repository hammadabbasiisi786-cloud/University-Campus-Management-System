package com.campus.academicunit;

// Base class
public class Equipment {
    private String type;
    private String name;
    private int operationalCost;

    // Constructor
    public Equipment(String type, String name, int operationalCost) {
        this.type = type;
        this.name = name;
        this.operationalCost = operationalCost;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            System.out.println("Error: type is empty");
        } else {
            this.type = type;
        }
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Error: name is empty");
        } else {
            this.name = name;
        }
    }

    public void setOperationalCost(int operationalCost) {
        if (operationalCost > 0) {
            this.operationalCost = operationalCost;
        } else {
            this.operationalCost = 0;
        }
    }

    // Getters
    public String getType() {

        return type;
    }

    public String getName() {
        return name;
    }

    public int getOperationalCost() {
        return operationalCost;
    }

    // Operational cost method — can be overridden by subclasses
    public int calculateMonthlyCost(int hoursPerDay) {
        return operationalCost * hoursPerDay * 30;
    }

    public void displayInfo() {
        System.out.println("Type: " + type + " | Name: " + name + " | Cost/hr: " + operationalCost);
    }
}

class Projector extends Equipment {
    public Projector(String name, int operationalCost) {
        super("Projector", name, operationalCost);
    }
}

class AirConditioner extends Equipment {
    public AirConditioner(String name, int operationalCost) {
        super("Air Conditioner", name, operationalCost);
    }
}

class Computer extends Equipment {
    public Computer(String name, int operationalCost) {
        super("Computer", name, operationalCost);
    }
}

class Camera extends Equipment {
    public Camera(String name, int operationalCost) {
        super("Camera", name, operationalCost);
    }
}

class Fan extends Equipment {
    public Fan(String name, int operationalCost) {
        super("Fan", name, operationalCost);
    }
}

