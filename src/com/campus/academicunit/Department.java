package com.campus.academicunit;

import com.campus.Interfaces.Reportable;
import com.campus.Person.*;
import com.campus.core.Academic_unit;

import java.util.*;

class Department extends Academic_unit implements Reportable {

    private Teacher headOfDepartment;

    //Relations
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Classroom> departmentalClassrooms = new ArrayList<>();
    private ArrayList<Lab> deparmentalLabs = new ArrayList<>();

    //CONSTRUCTORS

    public Department() {
    }

    public Department(String departmentName, Teacher headOfDepartment) {
        this.entityName = departmentName;
        setHeadOfDepartment(headOfDepartment);
    }


    //SETTERS

    public String getDepartmentName() {
        return entityName;
    }

    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            System.out.println("Invalid Department Name Entered!!!!");
        } else {
            this.entityName = departmentName;
        }
    }


    //GETTERS

    public Teacher getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(Teacher headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }


    public void removeClassroom(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }
        for (int i = 0; i < departmentalClassrooms.size(); i++) {
            if (departmentalClassrooms.get(i) == classroom) {
                departmentalClassrooms.remove(i);
                System.out.println("Classroom removed successfully");
                return;
            }
        }
        System.out.println("Classroom not found in this department");
    }

    public ArrayList<Classroom> getDepartmentalClassrooms() {
        return departmentalClassrooms;
    }


    public void addClassroom(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }
        classroom.setDepartment(this); // CHANGE: classroom now knows its department
        departmentalClassrooms.add(classroom);//BIDIRECTIONAL RELATION b/w department and classroom
    }
    //OTHER METHODS

    public void addCourse(Course course) {
        if (course == null) {
            System.out.println("Invalid course");
            return;
        }
        courses.add(course);
    }

    public void removeCourse(Course course) {
        if (course == null) {
            System.out.println("Invalid course");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i) == course) {
                courses.remove(i);
                System.out.println("Course removed successfully");
                return;
            }
        }
        System.out.println("Course not found in this department");
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            System.out.println("Invalid faculty");
            return;
        }
        teachers.add(teacher);
    }

    public void removeFaculty(Teacher faculty) {
        if (faculty == null) {
            System.out.println("Invalid faculty");
            return;
        }
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i) == faculty) {
                teachers.remove(i);
                System.out.println("Faculty removed successfully");
                return;
            }
        }
        System.out.println("Faculty not found in this department");
    }

    @Override
    public int getNumberOfStudents() {
        int noOfStudents = 0;
        for (Course tempcourse : courses) {
            noOfStudents += tempcourse.getStudents().size();
        }
        return noOfStudents;
    }

    @Override
    public int getNumberofFaculty() {
        return teachers.size();
    }

    @Override
    public double calculateOperationalCost() {

        double operationalSum = 0;

        for (Equipment eq : equipments) {
            operationalSum += eq.getOperationalCost();
        }

        return operationalSum + (getNumberOfStudents() * 100);
    }


    //    @Override
    public String generateReport() {
        return "Department: " + entityName +
                " | HOD: " + headOfDepartment.getName() +
                " | Courses: " + courses.size() +
                " | Faculty: " + teachers.size() +
                " | Students: " + getNumberOfStudents() +
                " | Operational Cost: " + calculateOperationalCost();
    }

    @Override
    public String toString() {
        return "Department: " + entityName +
                " | HOD: " + headOfDepartment.getName() +
                " | Courses: " + courses.size() +
                " | Students: " + getNumberOfStudents();
    }


    public void handleClassroomUnavailable(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }

        System.out.println("Classroom " + classroom.getClassNumber()
                + " is now unavailable. Rescheduling affected courses...");

        ArrayList<String> affectedSlots = classroom.markUnavailable();

        removeClassroom(classroom);

        for (Course course : courses) {
            if (course.getClassroom() == classroom) {
                System.out.println("Rescheduling: " + course.getCourseName());

                String result = course.generateSchedule(course.getDay(), course.getTime());

                if (result.equals("No Slot Available")) {
                    System.out.println("WARNING: Could not reschedule " + course.getCourseName());
                }
            }
        }
    }
}

