package com.campus;

import com.campus.core.Facility;
import com.campus.core.ServiceUnit;

import java.util.ArrayList;

public class CampusZone {

    // FIELDS
    private String zoneName;
    private ArrayList<Facility> facilities = new ArrayList<>();
    private ArrayList<ServiceUnit> serviceUnits = new ArrayList<>();
    private CampusRepository<Facility> repoFacility = new CampusRepository<>();
    private CampusRepository<ServiceUnit> repoServiceUnit = new CampusRepository<>();

    // CONSTRUCTORS
    public CampusZone() {
    }

    public CampusZone(String zoneName, String zoneType) {
        setZoneName(zoneName);
    }

    // SETTERS
    public void setZoneName(String zoneName) {
        if (zoneName == null && zoneName.isEmpty()) {
            System.out.println("Invalid Zone Name Entered!!!!");
        } else {
            this.zoneName = zoneName;
        }
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        if (facilities == null) {
            System.out.println("Facilities list cannot be null");
        } else {
            this.facilities = facilities;
        }
    }

    public void setServiceUnits(ArrayList<ServiceUnit> serviceUnits) {
        if (serviceUnits == null) {
            System.out.println("Service units list cannot be null");
        } else {
            this.serviceUnits = serviceUnits;
        }
    }

    // GETTERS
    public String getZoneName() { return zoneName; }
    public ArrayList<Facility> getFacilities() { return facilities; }
    public ArrayList<ServiceUnit> getServiceUnits() { return serviceUnits; }

    // OTHER METHODS

    // Adds a facility to this zone's facility list
    public void addFacility(Facility facility) {
        repoFacility.add(facility);
    }

    // Removes a facility from this zone's facility list
    public void removeFacility(Facility facility) {
        repoFacility.remove(facility);
    }

    // Links a service unit to this zone
    public void addServiceUnit(ServiceUnit unit) {
        repoServiceUnit.add(unit);
    }

    // Removes a service unit from this zone's service unit list
    public void removeServiceUnit(ServiceUnit unit) {
        repoServiceUnit.remove(unit);
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Campus Zone ===\n" +
                        "  Name         : %s\n" +
                        "  Facilities   : %d\n" +
                        "  Services     : %d",
                zoneName,
                facilities.size(),
                serviceUnits.size()
        );
    }
}