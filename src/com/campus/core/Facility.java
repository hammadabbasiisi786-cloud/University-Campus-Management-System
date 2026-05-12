package com.campus.core;

/// ///////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class Facility extends Campus_Entity {
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

    public String getStatus(){
        if(!isOpen){
            return "Closed";
        }
        if(capacity<=usageFrequency){
            return "Capcity is Full, Busy";
        }
        return "Open";
    }

    public Facility() {
    }

    public double calculateOperationalCost() {
        return maintenanceCost + usageFrequency;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nmaintenanceCost=" + maintenanceCost +
                ", usageFrequency=" + usageFrequency +
                ", capacity=" + capacity +
                ", isOpen=" + isOpen
                ;
    }

}
