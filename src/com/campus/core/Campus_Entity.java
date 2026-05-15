package com.campus.core;

import java.io.Serializable;

public abstract class Campus_Entity implements Serializable {

    // FIELDS
    protected String entityID;
    protected String entityName;
    protected String location;
    protected String status = "Active";

    // CONSTRUCTORS
    public Campus_Entity() {}

    public Campus_Entity(String entityID, String entityName, String location) {
        this.entityID = entityID;
        this.entityName = entityName;
        this.location = location;
    }

    // SETTERS
    public void setEntityID(String entityID) { this.entityID = entityID; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(String status) { this.status = status; }

    // GETTERS
    public String getEntityID() { return entityID; }
    public String getEntityName() { return entityName; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }

    // OTHER METHODS
    // Defines the contract for how each specific campus unit calculates its operational expenses
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
                entityID, entityName, location, status
        );
    }
}