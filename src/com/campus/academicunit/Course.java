package com.campus.academicunit;

import com.campus.Interfaces.Schedulable;
import java.util.*;

public class Course implements Schedulable {

    private static int idCounter = 0;
    private final String courseId;
    private String courseName;
    private int creditHour;
    private int maxcapacity;
    private String day;
    private String time;

    //Relations
    private Professor p;
    private Classroom c;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private ArrayList<Classroom> classrooms = new ArrayList<>();//issue how to initialize it???


    //CONSTRUCTORS

    public Course() {
        idCounter++;
        this.courseId = "CSC-" + idCounter;
    }

    public Course(String courseName, int creditHour, Professor p, Classroom c, String day, String time) {
        idCounter++;
        this.courseId = "CSC-" + idCounter;
        setCourseName(courseName);
        setCreditHour(creditHour);
        setProfessor(p);
        this.day = day;
        this.time = time;
        setClassroom(c);
        setMaxcapacity();
    }


    //SETTERS

    public void setCourseName(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            System.out.println("Invalid Course Name Entered!!!!");
        } else {
            this.courseName = courseName;
        }
    }

    public void setCreditHour(int creditHour) {
        if (creditHour < 0 || creditHour > 4) {
            System.out.println("Invalid Credit Hour Entered!!!!");
        } else {
            this.creditHour = creditHour;
        }
    }

    public void setMaxcapacity() {
        if (c != null) {
            this.maxcapacity = c.getCapacity();
        }
    }

    public void setProfessor(Professor p) {
        this.p = p;
    }

    public void setClassroom(Classroom c) {
        if (c.isSlotAvailable(day, time)) {
            c.bookSlot(day, time);
            this.c = c;
            setMaxcapacity();
        } else {
            System.out.println("Slot not available for " + courseName + ". Finding another slot...");
            generateSchedule(day, time);
            setMaxcapacity();
        }
    }

    public void setDay(String day) {
        if (day == null || day.isEmpty()) {
            System.out.println("Invalid day entered");
        } else {
            this.day = day;
        }
    }

    public void setTime(String time) {
        if (time == null || time.isEmpty()) {
            System.out.println("Invalid time entered");
        } else {
            this.time = time;
        }
    }


    //GETTERS

    public String getCourseId() { return courseId; }

    public String getCourseName() { return courseName; }

    public int getCreditHour() { return creditHour; }

    public int getMaxcapacity() { return maxcapacity; }

    public String getDay() { return day; }

    public String getTime() { return time; }

    public Professor getProfessor() { return p; }

    public Classroom getClassroom() { return c; }

    public ArrayList<Student> getStudents() { return students; }

    public ArrayList<Assignment> getAssignments() { return assignments; }

    public ArrayList<Classroom> getClassrooms() { return classrooms; }


    //OTHER METHODS

    public void addStudent(Student student) {
        for (int i = 0; i < students.size(); i++) {
            Student temp = students.get(i);
            if (temp.equals(student)) {
                System.out.println("Student already exists in this course");
                return;
            }
        }
        if (students.size() >= maxcapacity) {
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
        if (assignment == null) {
            System.out.println("Invalid assignment");
            return;
        }
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

    public void addClassroom(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }
        classrooms.add(classroom);
    }


    @Override
    // Rescheduling logic — finds new available slot
    public String generateSchedule(String day, String time) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] times = {"9-12", "12-2", "2-4"};

        for (String d : days) {
            for (String t : times) {
                for (Classroom room : classrooms) {
                    if (room.isSlotAvailable(d, t)) {
                        room.bookSlot(d, t);
                        this.c = room;
                        this.day = d;
                        this.time = t;
                        System.out.println("Course rescheduled to Room " + room.getClassNumber() + " on " + d + " at " + t);
                        return "Course: " + courseName + " | Room: " + room.getClassNumber() + " | Day: " + d + " | Time: " + t;
                    }
                }
            }
        }

        System.out.println("No available slot found for " + courseName);
        return "No Slot Available";
    }

    @Override
    public String toString() {
        return "ID: " + courseId +
                " | Course: " + courseName +
                " | Credits: " + creditHour +
                " | Room: " + (c != null ? c.getClassNumber() : "Not Assigned") +
                " | Day: " + day +
                " | Time: " + time +
                " | Professor: " + p.getFirstName() + " " + p.getLastName();
    }


}