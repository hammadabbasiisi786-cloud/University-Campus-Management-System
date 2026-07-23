package com.campus.backend.core;

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

    // GETTERS
    public String getEntityID() {
        return entityID;
    }

    // SETTERS
    public void setEntityID(String entityID) {
        if (entityID == null || entityID.isEmpty()) {
            System.out.println("Invalid Entity ID Entered!!!!");
        } else {
            this.entityID = entityID;
        }
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        if (entityName == null || entityName.isEmpty()) {
            System.out.println("Invalid Entity Name Entered!!!!");
        } else {
            this.entityName = entityName;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            System.out.println("Invalid Location Entered!!!!");
        } else {
            this.location = location;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null && (status.equals("Active") || status.equals("Busy") || status.equals("Closed"))) {
            this.status = status;
        } else {
            System.out.println("Invalid Status! Allowed values: Active, Busy, Closed");
        }
    }

    // OTHER METHODS

    public abstract double calculateOperationalCost();

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Campus Entity ===\n" + "  ID       : %s\n" + "  Name     : %s\n" + "  Location : %s\n" + "  Status   : %s", entityID, entityName, location, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campus_Entity that = (Campus_Entity) o;
        if (this.entityID == null || that.entityID == null) return false;
        return this.entityID.equals(that.entityID);
    }
}
