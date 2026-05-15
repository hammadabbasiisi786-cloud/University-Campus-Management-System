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
        this.numberOfPatrolCars = numberOfPatrolCars;
    }

    // SETTERS
    public void setNumberOfPatrolCars(int numberOfPatrolCars) { this.numberOfPatrolCars = numberOfPatrolCars; }
    public void setEmergencyMode(boolean emergencyMode) { this.emergencyMode = emergencyMode; }
    public void setCurrentEmergencyStatus(String currentEmergencyStatus) { this.currentEmergencyStatus = currentEmergencyStatus; }
    public void setEmergencyContacts(ArrayList<Notifiable> emergencyContacts) { this.emergencyContacts = emergencyContacts; }

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
        this.emergencyMode = true;
        this.currentEmergencyStatus = "EMERGENCY";

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