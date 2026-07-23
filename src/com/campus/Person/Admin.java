package com.campus.Person;

import com.campus.Interfaces.Notifiable;

import java.io.Serializable;

public class Admin implements Notifiable, Serializable {

    // FIELDS
    private static int adminCounter = 0;
    private final String adminID;
    private String name;
    private String role = "ADMIN";

    // CONSTRUCTORS
    public Admin() {
        adminCounter++;
        this.adminID = "COMSATS-ADM-" + adminCounter;
    }

    public Admin(String name) {
        adminCounter++;
        this.adminID = "COMSATS-ADM-" + adminCounter;
        setName(name);
    }

    // SETTERS

    // GETTERS
    public static int getAdminCounter() {
        return adminCounter;
    }

    public static void setAdminCounter(int value) {
        adminCounter = value;
    }

    public String getAdminID() {
        return adminID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name entered");
        } else {
            this.name = name;
        }
    }

    public String getRole() {
        return role;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Admin ===\n" + "  ID   : %s\n" + "  Name : %s", adminID, name);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("[Admin Notification] To: " + name);
        System.out.println("Message: " + message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return adminID.equals(admin.adminID);
    }
}