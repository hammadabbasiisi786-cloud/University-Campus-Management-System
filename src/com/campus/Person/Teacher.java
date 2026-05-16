package com.campus.Person;

import java.io.Serializable;

public class Teacher implements Serializable {

    // FIELDS
    private static int teacherCounter = 0;
    private final String teacherID;
    private String name;
    private double salary;
    private String qualification;
    private String role = "TEACHER";

    // CONSTRUCTORS
    public Teacher() {
        teacherCounter++;
        this.teacherID = "COMSATS-CSC-" + teacherCounter;
    }

    public Teacher(String name, double salary, String qualification) {
        teacherCounter++;
        this.teacherID = "COMSATS-CSC-" + teacherCounter;
        setName(name);
        setSalary(salary);
        setQualification(qualification);
    }

    // SETTERS

    // Name must not be null or empty
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name entered");
        } else {
            this.name = name;
        }
    }

    // Salary must be greater than 0
    public void setSalary(double salary) {
        if (salary > 0) {
            this.salary = salary;
        } else {
            System.out.println("Invalid salary entered");
        }
    }

    // Qualification must not be null or empty
    public void setQualification(String qualification) {
        if (qualification == null || qualification.isEmpty()) {
            System.out.println("Invalid qualification entered");
        } else {
            this.qualification = qualification;
        }
    }
    public static void setTeacherCounter(int value) { teacherCounter = value; }

    // GETTERS
    public static int getTeacherCounter() { return teacherCounter; }
    public String getTeacherID() { return teacherID; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public String getQualification() { return qualification; }
    public String getRole() { return role; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Teacher ===\n" +
                        "  ID            : %s\n" +
                        "  Name          : %s\n" +
                        "  Salary        : %.2f\n" +
                        "  Qualification : %s",
                teacherID,
                name,
                salary,
                qualification
        );
    }
}