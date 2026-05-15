package com.campus.core;

import com.campus.Person.Student;
import com.campus.academicunit.Equipment;

import java.util.ArrayList;

public abstract class Facility extends Campus_Entity {
    protected double maintenanceCost;
    protected double usageFrequency;
    protected int capacity;
    protected boolean isOpen;

    public Facility(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen) {
        super(entityID, entityName, location, status);
        this.maintenanceCost = maintenanceCost;
        this.usageFrequency = usageFrequency;
        this.capacity = capacity;
        this.isOpen = isOpen;
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
