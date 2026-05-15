package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.Person.*;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Hostel extends Facility {

    // FIELDS
    private static int totalHostels = 0;
    private int totalRooms;
    private int occupiedRooms;
    private ArrayList<Student> residents = new ArrayList<>();
    private String wardenName;
    private String openingHours;
    private String contact;
    private double utilitiesPerRoom;
    private String hostelType;
    private CampusRepository<Student> repo = new CampusRepository<>();

    // CONSTRUCTORS
    public Hostel() {
        super();
    }

    public Hostel(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, int totalRooms, int occupiedRooms, String wardenName, String openingHours, String contact, double utilitiesPerRoom, String hostelType) {
        super(entityID, entityName, location, status, maintenanceCost, usageFrequency, capacity, isOpen);
        this.totalRooms = totalRooms;
        this.occupiedRooms = occupiedRooms;
        this.wardenName = wardenName;
        this.openingHours = openingHours;
        this.contact = contact;
        this.utilitiesPerRoom = utilitiesPerRoom;
        this.hostelType = hostelType;
        totalHostels++;
    }

    // SETTERS
    public static void setTotalHostels(int totalHostels) { Hostel.totalHostels = totalHostels; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }
    public void setOccupiedRooms(int occupiedRooms) { this.occupiedRooms = occupiedRooms; }
    public void setResidents(ArrayList<Student> residents) { this.residents = residents; }
    public void setWardenName(String wardenName) { this.wardenName = wardenName; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    public void setContact(String contact) { this.contact = contact; }
    public void setUtilitiesPerRoom(double utilitiesPerRoom) { this.utilitiesPerRoom = utilitiesPerRoom; }
    public void setHostelType(String hostelType) { this.hostelType = hostelType; }

    // GETTERS
    public static int getTotalHostels() { return totalHostels; }
    public int getTotalRooms() { return totalRooms; }
    public int getOccupiedRooms() { return occupiedRooms; }
    public ArrayList<Student> getResidents() { return residents; }
    public String getWardenName() { return wardenName; }
    public String getOpeningHours() { return openingHours; }
    public String getContact() { return contact; }
    public double getUtilitiesPerRoom() { return utilitiesPerRoom; }
    public String getHostelType() { return hostelType; }

    // OTHER METHODS

    // Calculates operational cost by adding parent cost with per-room utility charges for all occupied rooms
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

    // TO-STRING
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