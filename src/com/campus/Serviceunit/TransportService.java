package com.campus.Serviceunit;

import com.campus.Interfaces.Schedulable;
import com.campus.core.ServiceUnit;

import java.io.Serializable;

public class TransportService extends ServiceUnit implements Schedulable, Serializable {

    // FIELDS
    private int numberOfBuses;
    private String normalRouteName;
    private String peakHourRouteName;
    private boolean isPeakHour = false;
    private String currentTime = "Not Set";
    private String[] peakTime = { "1pm", "2pm", "3pm" };

    // CONSTRUCTORS
    public TransportService() {
        super();
    }

    public TransportService(String entityID, String name, String location, int noOfStaff, int serviceHours,
            double baseHourlyRate, int numberOfBuses, String normalRouteName, String peakHourRouteName) {
        super(entityID, name, location, noOfStaff, serviceHours, baseHourlyRate);
        setNumberOfBuses(numberOfBuses);
        setNormalRouteName(normalRouteName);
        setPeakHourRouteName(peakHourRouteName);
    }

    // SETTERS
    public void setNumberOfBuses(int numberOfBuses) {
        if (numberOfBuses >= 0) {
            this.numberOfBuses = numberOfBuses;
        } else {
            System.out.println("Number of buses cannot be negative");
        }
    }

    public void setNormalRouteName(String normalRouteName) {
        if (normalRouteName != null && !normalRouteName.isEmpty()) {
            this.normalRouteName = normalRouteName;
        } else {
            System.out.println("Invalid Normal Route Name Entered!!!!");
        }
    }

    public void setPeakHourRouteName(String peakHourRouteName) {
        if (peakHourRouteName != null && !peakHourRouteName.isEmpty()) {
            this.peakHourRouteName = peakHourRouteName;
        } else {
            System.out.println("Invalid Peak Hour Route Name Entered!!!!");
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
    public int getNumberOfBuses() {
        return numberOfBuses;
    }

    public String getNormalRouteName() {
        return normalRouteName;
    }

    public String getPeakHourRouteName() {
        return peakHourRouteName;
    }

    public boolean isPeakHour() {
        return isPeakHour;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String[] getPeakTime() {
        return peakTime;
    }

    // Convenience getter that returns the currently active route based on peak hour
    // status
    public String getActiveRouteName() {
        return isPeakHour ? peakHourRouteName : normalRouteName;
    }

    // OTHER METHODS

    // Checks if the given time matches a peak hour and updates the route and
    // service mode accordingly
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

    // Returns the active route name based on peak hour status
    @Override
    public String generateSchedule() {
        if (isPeakHour) {
            return "Route shifted to peak hour route: " + peakHourRouteName + ".";
        } else {
            return "Route set to normal route: " + normalRouteName + ".";
        }
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Active Route      : %s\n" +
                        "  Normal Route      : %s\n" +
                        "  Peak Hour Route   : %s\n" +
                        "  Buses             : %d\n" +
                        "  Current Time      : %s\n" +
                        "  Peak Hour         : %s",
                super.toString(),
                getActiveRouteName(),
                normalRouteName,
                peakHourRouteName,
                numberOfBuses,
                currentTime,
                (isPeakHour ? "Yes" : "No"));
    }
}