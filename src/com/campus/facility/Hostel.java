package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.Person.*;
import com.campus.core.Facility;

import java.io.Serializable;
import java.util.ArrayList;

public class Hostel extends Facility  {

    // FIELDS
    private static int totalHostels = 0;
    private int totalRooms;
    private int occupiedRooms;
    private String wardenName;
    private String openingHours;
    private String contact;
    private double utilitiesPerRoom;
    private String hostelType;
    private CampusRepository<Student> repo = new CampusRepository<>();

    public Hostel() {
        totalHostels++;
    }

    public Hostel(String entityID, String entityName, String location, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, int totalRooms, int occupiedRooms, String wardenName, String openingHours, String contact, double utilitiesPerRoom, String hostelType) {
        super(entityID, entityName, location, maintenanceCost, usageFrequency, capacity, isOpen);
        setTotalRooms(totalRooms);
        setOccupiedRooms(occupiedRooms);
        setWardenName(wardenName);
        setOpeningHours(openingHours);
        setContact(contact);
        setUtilitiesPerRoom(utilitiesPerRoom);
        setHostelType(hostelType);
        totalHostels++;
    }

    // SETTERS
    public static void setTotalHostels(int totalHostels) {
        if (totalHostels < 0) {
            System.out.println("Total hostels cannot be negative");
        } else {
            Hostel.totalHostels = totalHostels;
        }
    }

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

    public void setOpeningHours(String openingHours) {
        if (openingHours == null || openingHours.isEmpty()) {
            System.out.println("Invalid opening hours entered");
        } else {
            this.openingHours = openingHours;
        }
    }

    public void setContact(String contact) {
        if (contact == null || contact.isEmpty()) {
            System.out.println("Invalid contact information entered");
        } else {
            this.contact = contact;
        }
    }

    public void setUtilitiesPerRoom(double utilitiesPerRoom) {
        if (utilitiesPerRoom < 0) {
            System.out.println("Utilities cost cannot be negative");
        } else {
            this.utilitiesPerRoom = utilitiesPerRoom;
        }
    }

    public void setHostelType(String hostelType) {
        if (hostelType == null || hostelType.isEmpty()) {
            System.out.println("Invalid hostel type entered");
        } else {
            this.hostelType = hostelType;
        }
    }

    // GETTERS
    public static int getTotalHostels() { return totalHostels; }
    public int getTotalRooms() { return totalRooms; }
    public int getOccupiedRooms() { return occupiedRooms; }
    public String getWardenName() { return wardenName; }
    public String getOpeningHours() { return openingHours; }
    public String getContact() { return contact; }
    public String getHostelType() { return hostelType; }
    public double getUtilitiesPerRoom() { return utilitiesPerRoom; }

    // OTHER METHODS
    @Override
    public double calculateOperationalCost() {
        return super.calculateOperationalCost() + (occupiedRooms * utilitiesPerRoom);
    }

    // Returns true if all rooms in the hostel are occupied
    public boolean isFull() {
        return occupiedRooms >= totalRooms;
    }

    // Adds a given number of rooms to the hostel's total room count
    public void addRooms(int count) {
        if (count > 0) {
            totalRooms += count;
            System.out.println("Rooms added: " + count + " | Total rooms: " + totalRooms);
        }
    }

    // Adds a student to the hostel's resident repository
    public void addResident(Student student) {
        repo.add(student);
    }

    // Removes a student from the hostel's resident repository
    public void removeResident(Student student) {
        repo.remove(student);
    }

    // Searches for a student in the hostel's resident repository
    public void searchResident(Student student) {
        repo.search(student);
    }

    public ArrayList<Student> getResidents() {
        return repo.getAll();
    }

    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Hostel Type    : %s\n" +
                        "  Total Rooms    : %d\n" +
                        "  Occupied Rooms : %d\n" +
                        "  Warden         : %s\n" +
                        "  Opening Hours  : %s\n" +
                        "  Contact        : %s\n" +
                        "  Utilities/Room : %.2f",
                super.toString(),
                hostelType,
                totalRooms,
                occupiedRooms,
                wardenName,
                openingHours,
                contact,
                utilitiesPerRoom
        );
    }
}