package com.campus.backend.core;

import java.io.Serializable;

public abstract class Facility extends Campus_Entity {

    private static int totalFacilityUsage;
    // FIELDS
    protected double maintenanceCost;
    protected int capacity;

    // CONSTRUCTORS
    public Facility() {
        super();
    }

    public Facility(String entityID, String entityName, String location, String status, double maintenanceCost, int capacity) {
        super(entityID, entityName, location);
        setStatus(status);
        setMaintenanceCost(maintenanceCost);
        setCapacity(capacity);
    }

    public static void setFacilityCounter(int value) {
        totalFacilityUsage = value;
    }

    public static int getTotalFacilityUsage() {
        return totalFacilityUsage;
    }

    protected static void incrementFacilityUsage() {
        totalFacilityUsage++;
    }

    // GETTERS
    public double getMaintenanceCost() {
        return maintenanceCost;
    }

    // SETTERS
    public void setMaintenanceCost(double maintenanceCost) {
        if (maintenanceCost < 0) {
            System.out.println("Maintenance cost cannot be negative");
        } else {
            this.maintenanceCost = maintenanceCost;
        }
    }

    public int getCapacity() {
        return capacity;
    }

    // OTHER METHODS

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            System.out.println("Capacity cannot be negative");
        } else {
            this.capacity = capacity;
        }
    }

    @Override
    public abstract double calculateOperationalCost();

    // TO-STRING
    @Override
    public String toString() {
        return String.format("%s\n" + "  Maintenance Cost : %.2f\n" + "  Capacity         : %d", super.toString(), maintenanceCost, capacity);
    }
}
