package com.campus.core;

import java.io.Serializable;

public abstract class ServiceUnit extends Campus_Entity {

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
        setStaffCount(noOfStaff);
        setServiceHours(serviceHours);
        setBaseHourlyRate(baseHourlyRate);
    }

    // GETTERS
    public int getStaffCount() {
        return noOfStaff;
    }

    // SETTERS
    public void setStaffCount(int noOfStaff) {
        if (noOfStaff < 0) {
            System.out.println("Staff count cannot be negative");
        } else {
            this.noOfStaff = noOfStaff;
        }
    }

    public int getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(int serviceHours) {
        if (serviceHours < 0) {
            System.out.println("Service hours cannot be negative");
        } else {
            this.serviceHours = serviceHours;
        }
    }

    public double getBaseHourlyRate() {
        return baseHourlyRate;
    }

    public void setBaseHourlyRate(double baseHourlyRate) {
        if (baseHourlyRate < 0) {
            System.out.println("Base hourly rate cannot be negative");
        } else {
            this.baseHourlyRate = baseHourlyRate;
        }
    }

    // OTHER METHODS

    @Override
    public double calculateOperationalCost() {
        return (this.noOfStaff * baseHourlyRate) * serviceHours;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("%s\n" + "  Staff Count   : %d\n" + "  Service Hours : %d\n" + "  Hourly Rate   : %.2f", super.toString(), noOfStaff, serviceHours, baseHourlyRate);
    }
}