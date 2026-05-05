package com.campus.academicunit;

import java.util.*;

public class Course {

    private static int idCounter = 0;
    private final String courseId;
    private String courseName;
    private int creditHour;
    private int maxcapacity;

    //Relations
    private Professor p;
    private Classroom c;
    private ArrayList<Student> students;
    private ArrayList<Assignment> assignments;
    private ArrayList<Classroom> classrooms;

    //If a classroom becomes unavailable → system reschedules affected classes

    public Course() {
        idCounter++;
        this.courseId = "CSC-" + idCounter;
    }

    public Course(String courseName, int creditHour, int maxcapacity, Professor p , Classroom c) {
        idCounter++;
        this.courseId = "CSC-" + idCounter;
        setCourseName(courseName);
        setCreditHour(creditHour);
        setMaxcapacity(maxcapacity);
        setProfessor(p);
        setClassroom(c);
    }

    //SETTERS

    public void setCourseName(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            System.out.println("Ivalid Course Name Entered!!!!");
        } else {
            this.courseName = courseName;
        }
    }

    public void setCreditHour(int creditHour) {
        if (creditHour < 0 || creditHour > 4) {
            System.out.println("Ivalid Credit Hour Entered!!!!");
        } else {
            this.creditHour = creditHour;
        }
    }

    public void setMaxcapacity(int maxcapacity) {
        if (maxcapacity < 0 ) {
            System.out.println("Capacity must be greater than 0");
        } else {
            this.maxcapacity = maxcapacity;
        }
    }

    public void setProfessor(Professor p) {
        this.p = p;
    }

    public void setClassroom(Classroom c) {
        if(c.isAvailable()==true){
            this.c =c;
        } else{
            System.out.println("Current Classroom is not Available.\nAssigning other classroom");

            for(int i=0;i<classrooms.size();i++){
                Classroom temp =classrooms.get(i);
                if(temp.isAvailable()==true){
                    this.c = temp;
                    System.out.println("Classroom " + c.getClassNumber() + " is available.\nAssigning this classroom");
                }
            }
        }

    }


    //GETTERS

    public String getCourseId() {return courseId;}

    public String getCourseName() {return courseName;}

    public int getCreditHour() {return creditHour;}

    public int getMaxcapacity() {return maxcapacity;}

    public Professor getProfessor() {return p;}

    public Classroom getClassroom() {return c;}

    public ArrayList<Student> getStudents() {return students;}

    public ArrayList<Assignment> getAssignments() {return assignments;}

    public ArrayList<Classroom> getClassrooms() {return classrooms;}


    //OTHER METHODS

    public void addStudent(Student student) {
        for(int i=0;i<students.size();i++){
            Student temp = students.get(i);
            if(temp.equals(student)){
                System.out.println("Student already exists in this course");
                return;
            }
        }
        if(students.size()>=maxcapacity) {
            System.out.println("Maximum Capacity Reached!!!");
            return;
        }
        students.add(student);

    }

    public void removeStudent(Student student) {

        if (student == null) {
            System.out.println("Invalid student");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            Student temp = students.get(i);

            if (temp.equals(student)) {
                students.remove(i);
                System.out.println("Student removed successfully");
                return;
            }
        }

        System.out.println("Student not found in this course");
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {

        if (assignment == null) {
            System.out.println("Invalid assignment");
            return;
        }

        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i) == assignment) {
                assignments.remove(i);
                System.out.println("Assignment removed successfully");
                return;
            }
        }

        System.out.println("Assignment not found");
    }



}
