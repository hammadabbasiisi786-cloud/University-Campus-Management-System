package com.campus.core;

import com.campus.academicunit.Equipment;

import java.util.ArrayList;

public abstract class Academic_unit extends Campus_Entity {
//    protected int numberOfStudents;
////    protected int numberofFaculty;
    protected ArrayList<Equipment> equipments = new ArrayList<>();//didnt need this becaue department already have equipmments which are of class and lab


    @Override
    //Method for calculating operational cost
    public double calculateOperationalCost() {
        double operationalSum = 0;

        for (Equipment eq : equipments) {
            operationalSum += eq.getOperationalCost();
        }

        double studentsum = 0;
        studentsum = numberOfStudents * 100;

        return operationalSum + studentsum;

    }

    //abstract
    public abstract int getNumberOfStudents();

}
