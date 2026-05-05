package com.campus.academicunit;

import java.util.ArrayList;

class Lab {

    private static int idCounter = 0;
    private boolean isAvailable;
    private final String labNumber;
    private int capacity;

    //Relations
    private Lab_Assistant la;


    public Lab() {
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
    }

    public Lab(boolean isAvailable, int capacity, Lab_Assistant la) {
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
        setAvailability(isAvailable);
        setCapacity(capacity);
        setLabAssistant(la);
    }

    public void setAvailability(boolean isAvailable) {this.isAvailable = isAvailable;}

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }

    public void setLabAssistant(Lab_Assistant la) {this.la = la;}

    public boolean getAvailability() {return isAvailable;}

    public String getLabNumber() {return labNumber;}

    public int getCapacity() {return capacity;}

    public Lab_Assistant getLabAssistant() {return la;}



}
