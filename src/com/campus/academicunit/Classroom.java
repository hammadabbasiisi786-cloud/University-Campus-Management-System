package com.campus.academicunit;

import java.util.ArrayList;

class Classroom {

    private static int idCounter = 0;
    private boolean isAvailable;
    private final String classNumber;
    private int capacity;

    public Classroom() {
        idCounter++;
        this.classNumber = "Class-" + idCounter;
    }

    public Classroom(boolean isAvailable, int capacity) {
        idCounter++;
        this.classNumber = "LAB-" + idCounter;
        isAvailable(isAvailable);
        setCapacity(capacity);
    }

    public void isAvailable(boolean isAvailable) {this.isAvailable = isAvailable;}

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }


    public boolean isAvailable() {return isAvailable;}

    public String getClassNumber() {return classNumber;}

    public int getCapacity() {return capacity;}


}
