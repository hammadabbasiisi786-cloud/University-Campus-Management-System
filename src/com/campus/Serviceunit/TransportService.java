package com.campus.Serviceunit;

import com.campus.Interfaces.Schedulable;
import com.campus.core.ServiceUnit;

import java.io.Serializable;

public class TransportService extends ServiceUnit implements Schedulable, Serializable {

    // FIELDS
    private int numberOfBuses;
    private String routeName;
    private boolean isPeakHour = false;
    private String currentTime = "Not Set";
    private String[] peakTime = {"1pm", "2pm", "3pm"};

    // CONSTRUCTORS
    public TransportService() {
        super();
    }

    public TransportService(String entityID, String name, String location, int noOfStaff, int serviceHours, double baseHourlyRate, int numberOfBuses, String routeName) {
        super(entityID, name, location, noOfStaff, serviceHours, baseHourlyRate);
        setNumberOfBuses(numberOfBuses);
        setRouteName(routeName);
    }

    // SETTERS
    public void setNumberOfBuses(int numberOfBuses) {
        if (numberOfBuses >= 0) {
            this.numberOfBuses = numberOfBuses;
        } else {
            System.out.println("Number of buses cannot be negative");
        }
    }

    public void setRouteName(String routeName) {
        if (routeName != null && !routeName.isEmpty()) {
            this.routeName = routeName;
        } else {
            System.out.println("Invalid Route Name Entered!!!!");
        }
    }

    public void setIsPeakHour(boolean isPeakHour) {
        this.isPeakHour = isPeakHour;
    }

    public void setCurrentTime(String currentTime) {
        if (currentTime != null && !currentTime.isEmpty()) {
            this.currentTime = currentTime;
        } else {
            System.out.println("Invalid Current Time Entered!!!!");
        }
    }

    public void setPeakTime(String[] peakTime) {
        if (peakTime != null) {
            this.peakTime = peakTime;
        } else {
            System.out.println("Peak time array cannot be null");
        }
    }

    // GETTERS
    public int getNumberOfBuses() { return numberOfBuses; }
    public String getRouteName() { return routeName; }
    public boolean isPeakHour() { return isPeakHour; }
    public String getCurrentTime() { return currentTime; }
    public String[] getPeakTime() { return peakTime; }

    // OTHER METHODS

    // Checks if the given time matches a peak hour and updates the route and service mode accordingly
    public void updateServiceByTime(String timeInput) {
        setCurrentTime(timeInput);
        boolean matchFound = false;

        for (String pTime : peakTime) {
            if (pTime.equalsIgnoreCase(timeInput)) {
                matchFound = true;
                break;
            }
        }

        setIsPeakHour(matchFound);

        String scheduleUpdate = generateSchedule();

        System.out.println("--- System Update ---");
        System.out.println("Current Time: " + currentTime);
        System.out.println("Mode: " + (isPeakHour ? "PEAK HOUR" : "NORMAL HOUR"));
        System.out.println("Status: " + scheduleUpdate);
    }

    // Adjusts the route to express mode during peak hours or resets to standard route otherwise
    @Override
    public String generateSchedule() {
        if (isPeakHour) {
            setRouteName("Express-Way");
            return "Route adjusted to Express-Way for peak traffic.";
        } else {
            setRouteName("Park Road (Standard)");
            return "Route reset to Park Road (Standard).";
        }
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Route Name    : %s\n" +
                        "  Buses         : %d\n" +
                        "  Current Time  : %s\n" +
                        "  Peak Hour     : %s",
                super.toString(),
                routeName,
                numberOfBuses,
                currentTime,
                (isPeakHour ? "Yes" : "No")
        );
    }
}