package com.campus.academicunit;

public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected String gender;
    protected String address;

    public Person() {
    }

    public Person(String firstName, String lastName, String gender, String address) {
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setAddress(address);
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            System.out.println("Invalid first name");
        } else {
            this.firstName = firstName;
        }
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            System.out.println("Invalid last name");
        } else {
            this.lastName = lastName;
        }
    }

    public void setGender(String gender) {
        if (gender != null && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
            this.gender = gender;
        } else {
            System.out.println("Invalid gender. Please enter 'Male' or 'Female'.");
        }
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Invalid address");
        } else {
            this.address = address;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }
}

class Student extends Person {
    private final String studentID;
    private int semester;
    private static int ID = 0;

    public Student() {
        ID++;
        this.studentID = String.valueOf(ID);
    }

    public Student(String firstName, String lastName, int semester, String gender, String address) {
        super(firstName, lastName, gender, address);

        setSemester(semester);
        ID++;
        this.studentID = String.valueOf(ID);
    }

    public void setSemester(int semester) {
        if (semester >= 1 && semester <= 8) {
            this.semester = semester;
        } else {
            System.out.println("Invalid semester. Must be between 1 and 8.");
        }
    }

    public String getStudentID() {
        return studentID;
    }

    public int getSemester() {
        return semester;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Student)) {
            return false;
        }
        Student other = (Student) obj;

        return this.studentID.equals(other.studentID);
    }
}

class Faculty extends Person {
    protected int experienceYears;
    protected String joiningDate;
    protected double salary;

    public Faculty() {
    }

    public Faculty(String firstName, String lastName, String gender, String address, int experienceYears, String joiningDate, double salary) {
        super(firstName, lastName, gender, address);
        setExperienceYears(experienceYears);
        setJoiningDate(joiningDate);
        setSalary(salary);

    }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            System.out.println("Invalid experienceYears. Please enter a positive integer.");
        } else {
            this.experienceYears = experienceYears;
        }
    }

    public void setJoiningDate(String joiningDate) {
        if (joiningDate == null || joiningDate.isEmpty()) {
            System.out.println("Invalid joiningDate. Please enter a positive integer.");
        } else {
            this.joiningDate = joiningDate;
        }
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            System.out.println("Invalid salary. Please enter a positive integer.");
        } else {
            this.salary = salary;
        }
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public double getSalary() {
        return salary;
    }
}

class Professor extends Faculty {
    //add equal method in professor
    private String designation;    // e.g., "Assistant Professor", "Associate Professor"
    private String qualification;  // e.g., "PhD", "MS"
    private String specialization;

    public Professor() {
    }

    public Professor(String firstName, String lastName, String gender, String address, int experienceYears, String joiningDate, double salary, String designation, String qualification, String specialization) {
        super(firstName, lastName, gender, address, experienceYears, joiningDate, salary);
        setDesignation(designation);
        setQualification(qualification);
        setSpecialization(specialization);

    }


    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.isEmpty()) {
            System.out.println("Invalid specialization entered.");
        } else {
            this.specialization = specialization;
        }
    }

    public void setQualification(String qualification) {
        if (qualification == null || qualification.isEmpty()) {
            System.out.println("Invalid qualification entered.");
        } else {
            this.qualification = qualification;
        }
    }

    public void setDesignation(String designation) {
        if (designation == null || designation.isEmpty()) {
            System.out.println("Invalid designation entered.");
        } else {
            this.designation = designation;
        }
    }

    public String getDesignation() {
        return designation;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSpecialization() {
        return specialization;
    }
}

class Lab_Assistant extends Faculty {
    //add equal method in professor
    private String labName;
    private String supervisorName; // Professor they assist
    private String qualification;  // e.g., "PhD", "MS"

    public Lab_Assistant() {
    }

    public Lab_Assistant(String firstName, String lastName, String gender, String address, int experienceYears, String joiningDate, double salary, String labName, String supervisorName, String qualification) {
        super(firstName, lastName, gender, address, experienceYears, joiningDate, salary);
        setLabName(labName);
        setSupervisorName(supervisorName);
        setQualification(qualification);

    }

    public void setLabName(String labName) {
        if (labName == null || labName.isEmpty()) {
            System.out.println("Invalid labName entered.");
        } else {
            this.labName = labName;
        }
    }

    public void setSupervisorName(String supervisorName) {
        if (supervisorName == null || supervisorName.isEmpty()) {
            System.out.println("Invalid supervisorName entered.");
        } else {
            this.supervisorName = supervisorName;
        }
    }

    public void setQualification(String qualification) {
        if (qualification == null || qualification.isEmpty()) {
            System.out.println("Invalid qualification entered.");
        } else {
            this.qualification = qualification;
        }
    }
}
