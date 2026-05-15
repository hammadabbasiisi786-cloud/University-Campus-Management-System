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
        this.numberOfBuses = numberOfBuses;
        this.routeName = routeName;
    }

    // SETTERS
    public void setNumberOfBuses(int numberOfBuses) { this.numberOfBuses = numberOfBuses; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    public void setIsPeakHour(boolean isPeakHour) { this.isPeakHour = isPeakHour; }
    public void setCurrentTime(String currentTime) { this.currentTime = currentTime; }
    public void setPeakTime(String[] peakTime) { this.peakTime = peakTime; }

    // GETTERS
    public int getNumberOfBuses() { return numberOfBuses; }
    public String getRouteName() { return routeName; }
    public boolean isPeakHour() { return isPeakHour; }
    public String getCurrentTime() { return currentTime; }
    public String[] getPeakTime() { return peakTime; }

    // OTHER METHODS

    // Checks if the given time matches a peak hour and updates the route and service mode accordingly
    public void updateServiceByTime(String timeInput) {
        this.currentTime = timeInput;
        boolean matchFound = false;

        for (String pTime : peakTime) {
            if (pTime.equalsIgnoreCase(timeInput)) {
                matchFound = true;
                break;
            }
        }

        this.isPeakHour = matchFound;

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
            this.routeName = "Express-Way";
            return "Route adjusted to Express-Way for peak traffic.";
        } else {
            this.routeName = "Park Road (Standard)";
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