package com.campus.Person;

import com.campus.Interfaces.Notifiable;

import java.io.Serializable;

public class Admin implements Notifiable, Serializable {

    // FIELDS
    private String name;
    private String role = "ADMIN";

    // CONSTRUCTORS
    public Admin() {}

    public Admin(String name) {
        setName(name);
    }

    // SETTERS

    // Name must not be null or empty
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name entered");
        } else {
            this.name = name;
        }
    }


    // GETTERS
    public String getName() { return name; }
    public String getRole() { return role; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Admin ===\n" +
                        "  Name : %s",
                name
        );
    }

    // Receives and displays an admin notification
    @Override
    public void sendNotification(String message) {
        System.out.println("[Admin Notification] To: " + name);
        System.out.println("Message: " + message);
    }
}