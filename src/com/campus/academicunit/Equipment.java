package com.campus.academicunit;

import java.io.Serializable;

public class Equipment implements Serializable {

    // FIELDS
    private String type;
    private String name;
    private int operationalCost;

    // CONSTRUCTORS
    public Equipment() {
    }

    public Equipment(String type, String name, int operationalCost) {
        setType(type);
        setName(name);
        setOperationalCost(operationalCost);
    }

    // SETTERS

    // GETTERS
    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            System.out.println("Error: type is empty");
        } else {
            this.type = type;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Error: name is empty");
        } else {
            this.name = name;
        }
    }

    public int getOperationalCost() {
        return operationalCost;
    }

    public void setOperationalCost(int operationalCost) {
        if (operationalCost > 0) {
            this.operationalCost = operationalCost;
        } else {
            System.out.println("Error: operational cost must be greater than 0");
        }
    }

    // OTHER METHODS

    public int calculateMonthlyCost(int hoursPerDay) {
        return operationalCost * hoursPerDay * 30;
    }

    public void displayInfo() {
        System.out.println("Type: " + type + " | Name: " + name + " | Cost/hr: " + operationalCost);
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Equipment ===\n" + "  Type             : %s\n" + "  Name             : %s\n"
                + "  Operational Cost : %d", type, name, operationalCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Equipment equipment = (Equipment) o;
        return (type != null && type.equals(equipment.type)) && (name != null && name.equals(equipment.name));
    }
}