package com.campus.backend.serviceunit;

import com.campus.backend.interfaces.Notifiable;
import com.campus.backend.core.ServiceUnit;

import java.io.Serializable;

public class HealthCenter extends ServiceUnit implements Notifiable, Serializable {

    // FIELDS
    private int availableBeds;
    private int noOfDoctors;

    // CONSTRUCTORS
    public HealthCenter() {
        super();
    }

    public HealthCenter(String entityID, String name, String location, int noOfStaff, int serviceHours, double baseHourlyRate, int availableBeds, int noOfDoctors) {
        super(entityID, name, location, noOfStaff, serviceHours, baseHourlyRate);
        setAvailableBeds(availableBeds);
        setNoOfDoctors(noOfDoctors);
    }

    // GETTERS
    public int getAvailableBeds() {
        return availableBeds;
    }

    // SETTERS
    public void setAvailableBeds(int availableBeds) {
        if (availableBeds >= 0) {
            this.availableBeds = availableBeds;
        } else {
            System.out.println("Available beds cannot be negative");
        }
    }

    public int getNoOfDoctors() {
        return noOfDoctors;
    }

    public void setNoOfDoctors(int noOfDoctors) {
        if (noOfDoctors >= 0) {
            this.noOfDoctors = noOfDoctors;
        } else {
            System.out.println("Number of doctors cannot be negative");
        }
    }

    // OTHER METHODS

    @Override
    public void sendNotification(String message) {
        System.out.println("[Health Center Notification]");
        System.out.println("Alert: " + message);
        System.out.println("Response: Alerting " + noOfDoctors + " on-call doctors. Preparing " + availableBeds + " beds.");
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("%s\n" + "  Available Beds : %d\n" + "  Doctors        : %d", super.toString(), availableBeds, noOfDoctors);
    }
}
