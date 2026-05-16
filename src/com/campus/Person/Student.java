package com.campus.Person;

import java.io.Serializable;

public class Student implements Serializable {

    // FIELDS
    private static int studentCounter = 0;
    private final String studentID;
    private String name;
    private int semester;
    private String role = "STUDENT";

    // CONSTRUCTORS
    public Student() {
        studentCounter++;
        this.studentID = "FA25-BCS-" + studentCounter;
    }

    public Student(String name, int semester) {
        studentCounter++;
        this.studentID = "FA25-BCS-" + studentCounter;
        setName(name);
        setSemester(semester);
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
    public void setSemester(int semester) {
        if (semester < 1 || semester > 8) {
            System.out.println("Invalid semester entered");
        } else {
            this.semester = semester;
        }
    }
    public static void setStudentCounter(int value) { studentCounter = value; }


    // GETTERS
    public static int getStudentCounter() { return studentCounter; }
    public String getStudentID() { return studentID; }
    public String getName() { return name; }
    public int getSemester() { return semester; }
    public String getRole() { return role; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Student ===\n" +
                        "  ID       : %s\n" +
                        "  Name     : %s\n" +
                        "  Semester : %d",
                studentID,
                name,
                semester
        );
    }
}