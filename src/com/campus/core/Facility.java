package com.campus.core;

import java.io.Serializable;

public abstract class Facility extends Campus_Entity {

    // FIELDS
    protected double maintenanceCost;
    protected int capacity;
    private static int totalFacilityUsage;

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

    // SETTERS
    public void setMaintenanceCost(double maintenanceCost) {
        if (maintenanceCost < 0) {
            System.out.println("Maintenance cost cannot be negative");
        } else {
            this.maintenanceCost = maintenanceCost;
        }
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            System.out.println("Capacity cannot be negative");
        } else {
            this.capacity = capacity;
        }
    }

    public static void setFacilityCounter(int value) {
        totalFacilityUsage = value;
    }

    // GETTERS
    public double getMaintenanceCost() { return maintenanceCost; }
    public int getCapacity()           { return capacity; }
    public static int getTotalFacilityUsage() { return totalFacilityUsage; }

    // OTHER METHODS

    // Called by child classes whenever their facility is used — increments system-wide counter
    protected static void incrementFacilityUsage() {
        totalFacilityUsage++;
    }

    // Base implementation returns maintenanceCost — children override to add their own costs
    @Override
    public abstract double calculateOperationalCost() ;
    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Maintenance Cost : %.2f\n" +
                        "  Capacity         : %d",
                super.toString(),
                maintenanceCost,
                capacity
        );
    }
}