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

    // GETTERS
    public static int getTeacherCounter() {
        return teacherCounter;
    }

    public static void setTeacherCounter(int value) {
        teacherCounter = value;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name entered");
        } else {
            this.name = name;
        }
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary > 0) {
            this.salary = salary;
        } else {
            System.out.println("Invalid salary entered");
        }
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        if (qualification == null || qualification.isEmpty()) {
            System.out.println("Invalid qualification entered");
        } else {
            this.qualification = qualification;
        }
    }

    public String getRole() {
        return role;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Teacher ===\n" + "  ID            : %s\n" + "  Name          : %s\n" + "  Salary        : %.2f\n" + "  Qualification : %s", teacherID, name, salary, qualification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return teacherID.equals(teacher.teacherID);
    }
}