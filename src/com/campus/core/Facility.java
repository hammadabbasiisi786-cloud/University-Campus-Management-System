package com.campus.core;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Facility extends Campus_Entity  {

    // FIELDS
    protected static int totalFacilityUsage = 0;
    protected double maintenanceCost;
    protected double usageFrequency;
    protected int capacity;
    protected boolean isOpen;

    // CONSTRUCTORS
    public Facility() {
        super();
        totalFacilityUsage++;
    }

    public Facility(String entityID, String entityName, String location, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen) {
        super(entityID, entityName, location);
        setMaintenanceCost(maintenanceCost);
        setUsageFrequency(usageFrequency);
        setCapacity(capacity);
        setOpen(isOpen);
    }

    public String getStatus(){
        if(!isOpen){
            return "Closed";
        }
        if(capacity<=usageFrequency){
            return "Capacity is Full, Busy";
        }
        return "Open";
    }

    public void setMaintenanceCost(double maintenanceCost) {
        if (maintenanceCost < 0) {
            System.out.println("Maintenance cost cannot be negative");
        } else {
            this.maintenanceCost = maintenanceCost;
        }
    }

    public void setUsageFrequency(double usageFrequency) {
        if (usageFrequency < 0) {
            System.out.println("Usage frequency cannot be negative");
        } else {
            this.usageFrequency = usageFrequency;
        }
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            System.out.println("Capacity cannot be negative");
        } else {
            this.capacity = capacity;
        }
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    // GETTERS
    public double getMaintenanceCost() {
        return maintenanceCost;
    }
    public double getUsageFrequency() {
        return usageFrequency;
    }
    public int getCapacity() {
        return capacity;
    }
    public boolean isOpen() {
        return isOpen;
    }

    public double calculateOperationalCost() {
        return maintenanceCost + usageFrequency;
    }

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