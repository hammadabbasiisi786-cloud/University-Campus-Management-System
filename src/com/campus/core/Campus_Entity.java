package com.campus.core;

import java.io.Serializable;

public abstract class Campus_Entity implements Serializable {

    // FIELDS
    protected String entityID;
    protected String entityName;
    protected String location;
    protected String status = "Active";

    // CONSTRUCTORS
    public Campus_Entity() {
    }


    public Campus_Entity(String entityID, String entityName, String location) {
        setEntityID(entityID);
        setEntityName(entityName);
        setLocation(location);
    }

    // SETTERS
    public void setEntityID(String entityID) {
        if (entityID == null || entityID.isEmpty()) {
            System.out.println("Invalid Entity ID Entered!!!!");
        } else {
            this.entityID = entityID;
        }
    }

    public void setEntityName(String entityName) {
        if (entityName == null || entityName.isEmpty()) {
            System.out.println("Invalid Entity Name Entered!!!!");
        } else {
            this.entityName = entityName;
        }
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            System.out.println("Invalid Location Entered!!!!");
        } else {
            this.location = location;
        }
    }

    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            System.out.println("Invalid Status Entered!!!!");
        } else {
            this.status = status;
        }
    }

    // GETTERS
    public String getEntityID() {
        return entityID;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    // OTHER METHODS
    // Defines the contract for how each specific campus unit calculates its
    // operational expenses
    public abstract double calculateOperationalCost();

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Campus Entity ===\n" +
                        "  ID       : %s\n" +
                        "  Name     : %s\n" +
                        "  Location : %s\n" +
                        "  Status   : %s",
                entityID, entityName, location, status);
    }
}