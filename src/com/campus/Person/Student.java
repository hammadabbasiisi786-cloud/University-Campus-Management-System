package com.campus.Person;

import java.io.Serializable;
import java.util.ArrayList;

import com.campus.academicunit.Assignment;
import com.campus.academicunit.Course;

public class Student implements Serializable {

    // FIELDS
    private static int studentCounter = 0;
    private final String studentID;
    private String name;
    private int semester;
    private String role = "STUDENT";
    private ArrayList<Course> enrolledCourses = new ArrayList<>();

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

    public static void setStudentCounter(int value) {
        studentCounter = value;
    }

    // GETTERS
    public static int getStudentCounter() {
        return studentCounter;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public String getRole() {
        return role;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void addCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public void removeCourse(Course course) {
        enrolledCourses.remove(course);
    }

    // TO-STRING
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Student ===\n")
                .append("  ID       : ").append(studentID).append("\n")
                .append("  Name     : ").append(name).append("\n")
                .append("  Semester : ").append(semester).append("\n")
                .append("  Courses  : ");

        if (enrolledCourses.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < enrolledCourses.size(); i++) {
                sb.append(enrolledCourses.get(i).getCourseName());
                if (i < enrolledCourses.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    // Returns all assignments for one specific enrolled course
    public ArrayList<Assignment> getAssignmentsForCourse(Course course) {
        if (course == null || !enrolledCourses.contains(course)) {
            System.out.println("Student is not enrolled in this course.");
            return new ArrayList<>();
        }
        return course.getAssignments();
    }

    public ArrayList<Assignment> getAllAssignments() {
        ArrayList<Assignment> all = new ArrayList<>();
        for (Course c : enrolledCourses) {
            all.addAll(c.getAssignments());
        }
        return all;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Student student = (Student) o;
        return studentID.equals(student.studentID);
    }
}