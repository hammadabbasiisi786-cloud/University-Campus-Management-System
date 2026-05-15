package com.campus.Serviceunit;

import com.campus.Interfaces.Notifiable;
import com.campus.core.*;

import java.util.ArrayList;

public class SecurityService extends ServiceUnit implements Notifiable {

    // FIELDS
    private int numberOfPatrolCars;
    private boolean emergencyMode = false;
    private String currentEmergencyStatus = "NORMAL";
    private ArrayList<Notifiable> emergencyContacts = new ArrayList<>();

    // CONSTRUCTORS
    public SecurityService() {
        super();
    }

    public SecurityService(String entityID, String name, String location, int noOfStaff, int serviceHours, double baseHourlyRate, int numberOfPatrolCars) {
        super(entityID, name, location, noOfStaff, serviceHours, baseHourlyRate);
        setNumberOfPatrolCars(numberOfPatrolCars);
    }

    // SETTERS
    public void setNumberOfPatrolCars(int numberOfPatrolCars) {
        if (numberOfPatrolCars >= 0) {
            this.numberOfPatrolCars = numberOfPatrolCars;
        } else {
            System.out.println("Number of patrol cars cannot be negative");
        }
    }

    public void setEmergencyMode(boolean emergencyMode) {
        this.emergencyMode = emergencyMode;
    }

    public void setCurrentEmergencyStatus(String currentEmergencyStatus) {
        if (currentEmergencyStatus != null && !currentEmergencyStatus.isEmpty()) {
            this.currentEmergencyStatus = currentEmergencyStatus;
        } else {
            System.out.println("Invalid emergency status entered");
        }
    }

    public void setEmergencyContacts(ArrayList<Notifiable> emergencyContacts) {
        if (emergencyContacts != null) {
            this.emergencyContacts = emergencyContacts;
        } else {
            System.out.println("Emergency contacts list cannot be null");
        }
    }

    // GETTERS
    public int getNumberOfPatrolCars() { return numberOfPatrolCars; }
    public boolean isEmergencyMode() { return emergencyMode; }
    public String getCurrentEmergencyStatus() { return currentEmergencyStatus; }
    public ArrayList<Notifiable> getEmergencyContacts() { return emergencyContacts; }

    // OTHER METHODS

    // Registers a notifiable unit (e.g. HealthCenter) as an emergency contact
    public void addEmergencyContact(Notifiable unit) {
        if (unit != null) {
            emergencyContacts.add(unit);
        }
    }

    // Activates emergency mode and notifies all registered emergency contacts with the given message
    public void handleEmergencySituation(String message) {
        setEmergencyMode(true);
        setCurrentEmergencyStatus("EMERGENCY");

        System.out.println("--- SECURITY PROTOCOL ACTIVATED ---");

        for (Notifiable contact : emergencyContacts) {
            contact.sendNotification(message);
        }
    }

    // Receives and displays a security alert notification
    @Override
    public void sendNotification(String message) {
        System.out.println("[Security Alert] Received: " + message);
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Patrol Cars      : %d\n" +
                        "  Emergency Mode   : %s\n" +
                        "  Emergency Status : %s\n" +
                        "  Alert Contacts   : %d",
                super.toString(),
                numberOfPatrolCars,
                (emergencyMode ? "Active" : "Inactive"),
                currentEmergencyStatus,
                emergencyContacts.size()
        );
    }
}