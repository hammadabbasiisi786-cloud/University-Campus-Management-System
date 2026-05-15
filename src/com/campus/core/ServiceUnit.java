package com.campus.core;

import java.io.Serializable;

public abstract class ServiceUnit extends Campus_Entity implements Serializable {

    // FIELDS
    protected int serviceHours;
    protected int noOfStaff;
    protected double baseHourlyRate;

    // CONSTRUCTORS
    public ServiceUnit() {
        super();
    }

    public ServiceUnit(String entityID, String name, String location, int noOfStaff, int serviceHours, double baseHourlyRate) {
        super(entityID, name, location);
        this.noOfStaff = noOfStaff;
        this.serviceHours = serviceHours;
        this.baseHourlyRate = baseHourlyRate;
    }

    // SETTERS
    public void setStaffCount(int noOfStaff) { this.noOfStaff = noOfStaff; }
    public void setServiceHours(int serviceHours) { this.serviceHours = serviceHours; }
    public void setBaseHourlyRate(double baseHourlyRate) { this.baseHourlyRate = baseHourlyRate; }

    // GETTERS
    public int getStaffCount() { return noOfStaff; }
    public int getServiceHours() { return serviceHours; }
    public double getBaseHourlyRate() { return baseHourlyRate; }

    // OTHER METHODS

    // Calculates operational cost as total staff multiplied by hourly rate and service hours
    @Override
    public double calculateOperationalCost() {
        return (this.noOfStaff * baseHourlyRate) * serviceHours;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Staff Count   : %d\n" +
                        "  Service Hours : %d\n" +
                        "  Hourly Rate   : %.2f",
                super.toString(),
                noOfStaff,
                serviceHours,
                baseHourlyRate
        );
    }
}