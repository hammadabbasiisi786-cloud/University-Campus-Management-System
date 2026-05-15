package com.campus.Person;

public class Teacher {

    // FIELDS
    private String name;
    private double salary;
    private String qualification;

    // CONSTRUCTORS
    public Teacher() {}

    public Teacher(String name, double salary, String qualification) {
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

    // GETTERS
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public String getQualification() { return qualification; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Teacher ===\n" +
                        "  Name          : %s\n" +
                        "  Salary        : %.2f\n" +
                        "  Qualification : %s",
                name,
                salary,
                qualification
        );
    }
}