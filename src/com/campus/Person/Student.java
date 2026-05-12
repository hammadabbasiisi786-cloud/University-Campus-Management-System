package com.campus.Person;

public class Student {
    private String name;
    private static int studentcounter = 0;
    private final int studentID;
    private int semester;

    public Student() {
        studentcounter++;
        this.studentID = studentcounter;
    }

    public Student(String name, int semester) {
        this.name = name;
        studentcounter++;
        this.studentID = studentcounter;
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentID() {
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