package com.campus.academicunit;

import com.campus.core.Academic_Unit;

import java.util.ArrayList;

public class Classroom extends Academic_Unit {

    // FIELDS
    private static int idCounter = 0;
    private final String classNumber;
    private int capacity;
    private boolean available = true;
    private ArrayList<String> occupiedSlots = new ArrayList<>();
    private Department department;

    // CONSTRUCTORS
    public Classroom() {
        super();
        idCounter++;
        this.classNumber = "Class-" + idCounter;
    }

    public Classroom(String entityID, String entityName, String location, int capacity, boolean available) {
        super(entityID, entityName, location);
        idCounter++;
        this.classNumber = "Class-" + idCounter;
        setCapacity(capacity);
        this.available = available;
    }

    // SETTERS
    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }
    public void setAvailable(boolean available) { this.available = available; }
    public void setOccupiedSlots(ArrayList<String> occupiedSlots) { this.occupiedSlots = occupiedSlots; }
    public void setDepartment(Department department) { this.department = department; }

    // GETTERS
    public String getClassNumber() { return classNumber; }
    public int getCapacity() { return capacity; }
    public boolean isAvailable() { return available; }
    public ArrayList<String> getOccupiedSlots() { return occupiedSlots; }
    public Department getDepartment() { return department; }

    // OTHER METHODS

    // Checks whether a specific day-time slot is free for booking in this classroom
    public boolean isSlotAvailable(String day, String time) {
        String schedule = day + "-" + time;
        return !occupiedSlots.contains(schedule);
    }

    // Books a specific day-time slot by adding it to the occupied slots list
    public void bookSlot(String day, String time) {
        String schedule = day + "-" + time;
        occupiedSlots.add(schedule);
    }

    // Returns classroom capacity as a proxy for the number of students it can hold
    @Override
    public int getNumberOfStudents() {
        return capacity;
    }

    // Marks classroom as unavailable, clears all booked slots, and returns the affected slots
    public ArrayList<String> markUnavailable() {
        this.available = false;
        ArrayList<String> affectedSlots = new ArrayList<>(occupiedSlots);
        occupiedSlots.clear();
        return affectedSlots;
    }

    // Calculates operational cost by summing the cost of all equipment in this classroom
    @Override
    public double calculateOperationalCost() {
        double operationalCost = 0;
        for (Equipment eq : equipments) {
            operationalCost += eq.getOperationalCost();
        }
        return operationalCost;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Class Number  : %s\n" +
                        "  Capacity      : %d\n" +
                        "  Available     : %s\n" +
                        "  Booked Slots  : %d\n" +
                        "  Department    : %s",
                super.toString(),
                classNumber,
                capacity,
                (available ? "Yes" : "No"),
                occupiedSlots.size(),
                (department != null ? department.getDepartmentName() : "Unassigned")
        );
    }
}