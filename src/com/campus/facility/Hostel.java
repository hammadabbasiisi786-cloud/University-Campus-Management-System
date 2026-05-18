package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.Person.*;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Hostel extends Facility {

    // FIELDS
    private int hostelUsage;
    private int totalRooms;
    private int occupiedRooms;
    private String wardenName;
    private String timing;
    private CampusRepository<Student> repoHostelStudents = new CampusRepository<>();

    // CONSTRUCTORS
    public Hostel() {
        super();
    }

    public Hostel(String entityID, String entityName, String location, String status, double maintenanceCost, int capacity, int totalRooms, int occupiedRooms, String wardenName, String timing) {
        super(entityID, entityName, location, status, maintenanceCost, capacity);
        setTotalRooms(totalRooms);
        setOccupiedRooms(occupiedRooms);
        setWardenName(wardenName);
        setTiming(timing);
    }

    // SETTERS
    public void setTotalRooms(int totalRooms) {
        if (totalRooms < 0) {
            System.out.println("Total rooms cannot be negative");
        } else {
            this.totalRooms = totalRooms;
        }
    }

    public void setOccupiedRooms(int occupiedRooms) {
        if (occupiedRooms < 0 || occupiedRooms > totalRooms) {
            System.out.println("Invalid occupied rooms count");
        } else {
            this.occupiedRooms = occupiedRooms;
        }
    }

    public void setWardenName(String wardenName) {
        if (wardenName == null || wardenName.isEmpty()) {
            System.out.println("Invalid Warden Name entered");
        } else {
            this.wardenName = wardenName;
        }
    }

    public void setTiming(String timing) {
        if (timing == null || timing.isEmpty()) {
            System.out.println("Invalid timing entered");
        } else {
            this.timing = timing;
        }
    }

    // GETTERS
    public int getTotalRooms()      { return totalRooms; }
    public int getOccupiedRooms()   { return occupiedRooms; }
    public String getWardenName()   { return wardenName; }
    public String getTiming()       { return timing; }
    public int getHostelUsage()     { return hostelUsage; }

    // OTHER METHODS

    // Returns true if all rooms in the hostel are occupied
    public boolean isFull() {
        return occupiedRooms >= totalRooms;
    }

    // Adds a given number of rooms to the hostel's total room count
    public void addRooms(int count) {
        if (count > 0) {
            totalRooms += count;
            System.out.println("Rooms added: " + count + " | Total rooms: " + totalRooms);
        } else {
            System.out.println("Invalid room count entered");
        }
    }

    // Adds a student to the hostel's resident repository — increments usage counters if successful
    public void addResident(Student student) {
        if (isFull()) {
            System.out.println("Hostel is full. Cannot add more residents.");
            return;
        }
        if (repoHostelStudents.add(student)) {
            occupiedRooms++;
            hostelUsage++;
            incrementFacilityUsage();
            System.out.println("Resident added. Hostel Usage: " + hostelUsage +
                    " | Total Facility Usage: " + getTotalFacilityUsage());
        }
    }

    // Removes a student from the hostel's resident repository
    public void removeResident(Student student) {
        if (repoHostelStudents.remove(student)) {
            if (occupiedRooms > 0) {
                occupiedRooms--;
                hostelUsage++;
                incrementFacilityUsage();
            }
        }
    }

    // Returns all residents in the hostel
    public ArrayList<Student> getResidents() {
        return repoHostelStudents.getAll();
    }

    // Returns maintenanceCost multiplied by hostelUsage as operational cost
    @Override
    public double calculateOperationalCost() {
        return maintenanceCost * hostelUsage;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Total Rooms    : %d\n" +
                        "  Occupied Rooms : %d\n" +
                        "  Warden         : %s\n" +
                        "  Timing         : %s\n" +
                        "  Hostel Usage   : %d",
                super.toString(),
                totalRooms,
                occupiedRooms,
                wardenName,
                timing,
                hostelUsage
        );
    }
}