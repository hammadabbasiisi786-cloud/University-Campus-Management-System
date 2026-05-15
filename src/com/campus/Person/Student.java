package com.campus.Person;

import java.io.Serializable;

public class Student implements Serializable {
    private static int studentcounter = 0;
    private final String studentID;
    private String name;
    private int semester;

    public Student() {
        studentcounter++;
        this.studentID = "FA25-BCS-" + studentcounter;
    }

    public Student(String name, int semester) {
        this.name = name;
        studentcounter++;
        this.studentID = "FA25-BCS-" + studentcounter;
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Student [Name: " + name + ", ID: " + studentID + ", Semester: " + semester + "]";
    }
}