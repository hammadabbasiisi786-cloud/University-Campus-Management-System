package com.campus.facility.hostel;

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
    private String hostelType;

    public Hostel(double usageFrequency, double maintenanceCost, int capacity, boolean isOpen, int totalRooms, int occupiedRooms, String wardenName, String openingHours, String contact, String hostelType) {
        super(usageFrequency, maintenanceCost, capacity, isOpen);
        this.totalRooms = totalRooms;
        this.occupiedRooms = 0;
        this.wardenName = wardenName;
        this.openingHours = openingHours;
        this.contact = contact;
        this.hostelType = hostelType;
        //residents = new Arraylist<>();
        totalHostels++;
    }
    public Hostel(){}

    public double calculateOperationalCost(){
        //Needs amendment
        return 0;
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
    public boolean addStudent(Student student){
        if(!isFull()){
            System.out.println("No Vacancy!");
        }
        if(findResident(student.getStudentID())) !=null){
            System.out.println(student.getName() + " is already a resident");
            return false;
        }
        return true;
    }
    //////////////////////////////////////

    public Student findResident(String id) {
        for (Student s : residents) {
            if (s.getStudentID().equals(id)) {
                return s;
            }
        }
        return null;
    }
}

