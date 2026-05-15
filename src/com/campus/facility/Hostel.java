package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.Person.*;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Hostel extends Facility {
    private static int totalHostels =0 ;
    private int totalRooms;
    private int occupiedRooms;
    private ArrayList<Student> residents;
    private String wardenName;
    private String openingHours;
    private String contact;
    private double utilitiesPerRoom;
    private String hostelType;
    private CampusRepository<Student> repo = new CampusRepository<>();

    public Hostel(){}
    public Hostel(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, int totalRooms, int occupiedRooms, String wardenName, String openingHours, String contact,double utilitiesPerRoom, String hostelType) {
        super(entityID, entityName, location, status, maintenanceCost, usageFrequency, capacity, isOpen);
        this.totalRooms = totalRooms;
        this.occupiedRooms = occupiedRooms;
        this.wardenName = wardenName;
        this.openingHours = openingHours;
        this.contact = contact;
        this.utilitiesPerRoom = utilitiesPerRoom;
        this.hostelType = hostelType;
        residents = new ArrayList<>();
        totalHostels++;
    }

    public static void setTotalHostels(int totalHostels) {Hostel.totalHostels = totalHostels;}
    public void setTotalRooms(int totalRooms) {this.totalRooms = totalRooms;}
    public void setOccupiedRooms(int occupiedRooms) {this.occupiedRooms = occupiedRooms;}
    public void setResidents(ArrayList<Student> residents) {this.residents = residents;}
    public void setWardenName(String wardenName) {this.wardenName = wardenName;}
    public void setOpeningHours(String openingHours) {this.openingHours = openingHours;}
    public void setContact(String contact) {this.contact = contact;}
    public void setHostelType(String hostelType) {this.hostelType = hostelType;}

    public static int getTotalHostels() {return totalHostels;}
    public int getTotalRooms() {return totalRooms;}
    public int getOccupiedRooms() {return occupiedRooms;}
    public String getWardenName() {return wardenName;}
    public String getOpeningHours() {return openingHours;}
    public String getContact() {return contact;}
    public String getHostelType() {return hostelType;}


    public double calculateOperationalCost(){
        return super.calculateOperationalCost() + (occupiedRooms * utilitiesPerRoom);
    }

    public boolean isFull(){
        return occupiedRooms >= totalRooms;
    }
    public void addRooms(int count){
        if(count > 0){
        totalRooms+=count;
            System.out.println("Rooms added: " + count +"| Total rooms: " + totalRooms );
        }
    }
    public void addResident(Student student){
        repo.add(student);
    }
    public void removeResident(Student student){
        repo.remove(student);
    }
    public void searchResident(Student student){
        repo.search(student);
    }
    public ArrayList<Student> getResidents(){
        return residents;
    }

    public String toString(){
        return  super.toString()
                + "--------Hostel-------"
                + "\nTotal Rooms: " + totalRooms
                + "\nOccupied Rooms: " + occupiedRooms
                + "\nWarden Name: " + wardenName
                + "\nOpening Hours: " + openingHours
                + "\nContact: " + contact;
    }
}

