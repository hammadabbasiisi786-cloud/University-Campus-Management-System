package com.campus.academicunit;

import com.campus.Person.*;
import com.campus.core.Academic_Unit;

import java.util.ArrayList;

public class Lab extends Academic_Unit {

    // FIELDS
    private static int idCounter = 0;
    private final String labNumber;
    private boolean isAvailable;
    private int capacity;
    private Teacher teacher;
    private ArrayList<Student> students = new ArrayList<>();

    // CONSTRUCTORS
    public Lab() {
        super();
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
    }

    public Lab(String entityID, String entityName, String location, boolean isAvailable, int capacity, Teacher teacher) {
        super(entityID, entityName, location);
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
        setAvailability(isAvailable);
        setCapacity(capacity);
        setLabAssistant(teacher);
    }

    // SETTERS
    public void setAvailability(boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public void setLabAssistant(Teacher teacher) { this.teacher = teacher; }
    public void setStudents(ArrayList<Student> students) { this.students = students; }

    // GETTERS
    public String getLabNumber() { return labNumber; }
    public int getCapacity() { return capacity; }
    public boolean isAvailable() { return isAvailable; }
    public Teacher getTeacher() { return teacher; }
    public ArrayList<Student> getStudents() { return students; }

    // OTHER METHODS

    // Returns the number of students currently enrolled in this lab
    @Override
    public int getNumberOfStudents() {
        return students.size();
    }

    // Adds a student to the lab if not already present and capacity allows
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

    // Removes a student from the lab if they are found in the list
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

    // Calculates operational cost by summing the cost of all equipment in this lab
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
                        "  Lab Number    : %s\n" +
                        "  Capacity      : %d\n" +
                        "  Available     : %s\n" +
                        "  Students      : %d\n" +
                        "  Lab Assistant : %s",
                super.toString(),
                labNumber,
                capacity,
                (isAvailable ? "Yes" : "No"),
                students.size(),
                (teacher != null ? teacher.getName() : "Not Assigned")
        );
    }
}