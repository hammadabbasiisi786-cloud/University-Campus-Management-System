package com.campus;

import com.campus.core.Facility;
import com.campus.core.ServiceUnit;

import java.io.Serializable;
import java.util.ArrayList;

public class CampusZone implements Serializable {

    // FIELDS
    private String zoneName;
    private CampusRepository<Facility> repoFacility = new CampusRepository<>();
    private CampusRepository<ServiceUnit> repoServiceUnit = new CampusRepository<>();

    // CONSTRUCTORS
    public CampusZone() {
    }

    public CampusZone(String zoneName) {
        setZoneName(zoneName);
    }

    // GETTERS
    public String getZoneName() {
        return zoneName;
    }

    // SETTERS
    public void setZoneName(String zoneName) {
        if (zoneName == null || zoneName.isEmpty()) {
            System.out.println("Invalid Zone Name Entered!!!!");
        } else {
            this.zoneName = zoneName;
        }
    }

    public ArrayList<Facility> getFacilities() {
        return repoFacility.getAll();
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        if (facilities == null) {
            System.out.println("Facilities list cannot be null");
        } else {
            repoFacility.setItems(facilities);
        }
    }

    public ArrayList<ServiceUnit> getServiceUnits() {
        return repoServiceUnit.getAll();
    }

    public void setServiceUnits(ArrayList<ServiceUnit> serviceUnits) {
        if (serviceUnits == null) {
            System.out.println("Service units list cannot be null");
        } else {
            repoServiceUnit.setItems(serviceUnits);
        }
    }

    // OTHER METHODS

    public void addFacility(Facility facility) {
        repoFacility.add(facility);
    }

    public void removeFacility(Facility facility) {
        repoFacility.remove(facility);
    }

    public void addServiceUnit(ServiceUnit unit) {
        repoServiceUnit.add(unit);
    }

    public void removeServiceUnit(ServiceUnit unit) {
        repoServiceUnit.remove(unit);
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Campus Zone ===\n" + "  Name         : %s\n" + "  Facilities   : %d\n" + "  Services     : %d", zoneName, repoFacility.getAll().size(), repoServiceUnit.getAll().size());
    }
}