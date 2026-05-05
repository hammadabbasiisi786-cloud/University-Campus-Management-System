package com.campus.core;

/// ///////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class Facility extends CampusEntity {
    protected double maintenanceCost;
    protected double usageFrequency;
    protected int capacity;
    protected boolean isOpen;

    public Facility(double usageFrequency, double maintenanceCost, int capacity, boolean isOpen) {
        this.usageFrequency = usageFrequency;
        this.maintenanceCost = maintenanceCost;
        this.capacity = capacity;
        this.isOpen = isOpen;
    }

    public Facility() {
    }

    public double calculateOperationalCost() {
        return maintenanceCost + usageFrequency;
    }
}
