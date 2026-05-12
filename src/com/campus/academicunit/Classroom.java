package com.campus.academicunit;

import com.campus.core.Academic_unit;

import java.util.ArrayList;

class Classroom extends Academic_unit {

    private static int idCounter = 0;
    private final String classNumber;
    ArrayList<String> occupiedSlots = new ArrayList<String>();
    private int capacity;
    private boolean available = true;

    private Department department;

    public Classroom() {
        idCounter++;
        this.classNumber = "Class-" + idCounter;
    }

    public Classroom(boolean available, int capacity) {
        idCounter++;
        this.classNumber = "LAB-" + idCounter;
        setCapacity(capacity);
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }

    public boolean isSlotAvailable(String day, String time) {
        String schedule = day + "-" + time;
        return !occupiedSlots.contains(schedule);
    }

    public void bookSlot(String day, String time) {
        String schedule = day + "-" + time;
        occupiedSlots.add(schedule);

    }

    public double calculateOperationalCost() {

        double operationalSum = 0;

        for (Equipment eq : equipments) {
            operationalSum += eq.getOperationalCost();
        }

        return operationalSum + (capacity * 100);
    }

    public ArrayList<String> markUnavailable() {
        this.available = false;
        ArrayList<String> affectedSlots = new ArrayList<>(occupiedSlots);
        occupiedSlots.clear(); // free all slots since room is gone
        return affectedSlots;  // department uses this to find affected courses
    }


}
