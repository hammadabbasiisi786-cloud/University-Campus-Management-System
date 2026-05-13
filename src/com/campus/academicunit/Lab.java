package com.campus.academicunit;

import com.campus.Person.*;

import com.campus.core.Academic_unit;

import java.util.ArrayList;

class Lab extends Academic_unit {

    private static int idCounter = 0;
    private final String labNumber;
    private boolean isAvailable;
    private int capacity;


    //Relations
    private Teacher teacher;
    private ArrayList<Student> students = new ArrayList<>();


    //CONSTRUCTORS

    public Lab() {
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
    }

    public Lab(boolean isAvailable, int capacity, Teacher teacher) {
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
        setAvailability(isAvailable);
        setCapacity(capacity);
        setLabAssistant(teacher);
    }


    //SETTERS

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    //GETTERS
    public String getLabNumber() {
        return labNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher la) {
        this.teacher = la;
    }

    public void setLabAssistant(Teacher la) {
        this.teacher = la;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    @Override
    public int getNumberOfStudents() {
        return students.size();
    }


    //OTHER METHODS

    public void addStudent(Student student) {
        if (student == null) {
            System.out.println("Invalid student");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).equals(student)) {
                System.out.println("Student already exists in this lab");
                return;
            }
        }

        if (students.size() >= capacity) {
            System.out.println("Maximum capacity reached");
            return;
        }

        students.add(student);
        System.out.println("Student added successfully");
    }

    public void removeStudent(Student student) {
        if (student == null) {
            System.out.println("Invalid student");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).equals(student)) {
                students.remove(i);
                System.out.println("Student removed successfully");
                return;
            }
        }

        System.out.println("Student not found in this lab");
    }



    @Override
    public String toString() {
        return "Lab: " + labNumber + " | Capacity: " + capacity + " | Students: " + students.size() + " | Available: " + isAvailable + " | Lab Assistant: " + (teacher != null ? teacher.getName() : "Not Assigned");
    }

    @Override
    public double calculateOperationalCost() {
        double operationalCost = 0;
        for(Equipment eq : equipments) {
            operationalCost += eq.getOperationalCost();
        }
        return operationalCost ;
    }

}