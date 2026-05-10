package com.campus.academicunit;

import com.campus.core.Academic_unit;

import java.util.ArrayList;

class Classroom extends Academic_unit {

    private static int idCounter = 0;
    private final String classNumber;
    private int capacity;

    ArrayList<String> occupiedSlots = new ArrayList<String>();

    public Classroom() {
        idCounter++;
        this.classNumber = "Class-" + idCounter;
    }

    public Classroom(boolean isAvailable, int capacity) {
        idCounter++;
        this.classNumber = "LAB-" + idCounter;
        setCapacity(capacity);
    }

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }


    public String getClassNumber() {return classNumber;}

    public int getCapacity() {return capacity;}


    public boolean isSlotAvailable(String day, String time) {
        String schedule = day + "-" + time;

        if (occupiedSlots.contains(schedule)) {
            return false;
        } else {
            return true;
        }
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

}
