package com.campus.Person;

public class Teacher {
    private String name;
    private double salary;
    private String qualification;

    public Teacher() {
    }

    public Teacher(String name, double salary, String qualification) {
        this.name = name;
        this.salary = salary;
        this.qualification = qualification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "Teacher [Name: " + name + ", Salary: " + salary + ", Qualification: " + qualification + "]";
    }
}