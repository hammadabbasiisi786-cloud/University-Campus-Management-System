package com.campus;

import com.campus.core.Campus_Entity;
import com.campus.core.ServiceUnit;

import java.util.ArrayList;

public class CampusZone {

    // FIELDS
    private String zoneName;
    private ArrayList<Campus_Entity> facilities = new ArrayList<>();
    private ArrayList<ServiceUnit> serviceUnits = new ArrayList<>();

    // CONSTRUCTORS
    public CampusZone() {
    }

    public CampusZone(String zoneName, String zoneType) {
        this.zoneName = zoneName;
    }

    // SETTERS
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public void setFacilities(ArrayList<Campus_Entity> facilities) { this.facilities = facilities; }
    public void setServiceUnits(ArrayList<ServiceUnit> serviceUnits) { this.serviceUnits = serviceUnits; }

    // GETTERS
    public String getZoneName() { return zoneName; }
    public ArrayList<Campus_Entity> getFacilities() { return facilities; }
    public ArrayList<ServiceUnit> getServiceUnits() { return serviceUnits; }

    // OTHER METHODS

    // Adds a facility to this zone's facility list
    public void addFacility(Campus_Entity facility) {
        if (facility != null) {
            this.facilities.add(facility);
        }
    }

    // Removes a facility from this zone's facility list
    public void removeFacility(Campus_Entity facility) {
        if (facility != null) {
            this.facilities.remove(facility);
        }
    }

    // Links a service unit to this zone
    public void addServiceUnit(ServiceUnit unit) {
        if (unit != null) {
            this.serviceUnits.add(unit);
        }
    }

    // Removes a service unit from this zone's service unit list
    public void removeServiceUnit(ServiceUnit unit) {
        if (unit != null) {
            this.serviceUnits.remove(unit);
        }
    }




    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Campus Zone ===\n" +
                        "  Name         : %s\n" +
                        "  Facilities   : %d\n" +
                        "  Services     : %d\n" +
                zoneName,
                facilities.size(),
                serviceUnits.size()
        );
    }
}