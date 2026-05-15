package com.campus.core;

import java.util.ArrayList;

public abstract class Facility extends Campus_Entity {

    // FIELDS
    protected double maintenanceCost;
    protected double usageFrequency;
    protected int capacity;
    protected boolean isOpen;

    // CONSTRUCTORS
    public Facility() {
        super();
    }

    public Facility(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen) {
        super(entityID, entityName, location);
        setStatus(status);
        this.maintenanceCost = maintenanceCost;
        this.usageFrequency = usageFrequency;
        this.capacity = capacity;
        this.isOpen = isOpen;
    }

    // SETTERS
    public void setMaintenanceCost(double maintenanceCost) { this.maintenanceCost = maintenanceCost; }
    public void setUsageFrequency(double usageFrequency) { this.usageFrequency = usageFrequency; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setOpen(boolean isOpen) { this.isOpen = isOpen; }

    // GETTERS
    public double getMaintenanceCost() { return maintenanceCost; }
    public double getUsageFrequency() { return usageFrequency; }
    public int getCapacity() { return capacity; }
    public boolean isOpen() { return isOpen; }

    // OTHER METHODS

    // Overrides parent getStatus() to return a computed status based on availability and current usage
    @Override
    public String getStatus() {
        if (!isOpen) {
            return "Closed";
        }
        if (capacity <= usageFrequency) {
            return "Capacity is Full, Busy";
        }
        return "Open";
    }

    // Calculates operational cost as the sum of maintenance cost and usage frequency
    @Override
    public double calculateOperationalCost() {
        return maintenanceCost + usageFrequency;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Maintenance Cost : %.2f\n" +
                        "  Usage Frequency  : %.2f\n" +
                        "  Capacity         : %d\n" +
                        "  Open             : %s",
                super.toString(),
                maintenanceCost,
                usageFrequency,
                capacity,
                (isOpen ? "Yes" : "No")
        );
    }
}