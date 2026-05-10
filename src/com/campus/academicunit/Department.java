package com.campus.academicunit;

import com.campus.Interfaces.Reportable;
import com.campus.core.Academic_unit;
import java.util.*;

class Department extends Academic_unit implements Reportable {

    private Professor headOfDepartment;

    //Relations
    private ArrayList<Faculty> faculties = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();


    //CONSTRUCTORS

    public Department() {
    }

    public Department(String departmentName, Professor headOfDepartment) {
        this.entityName = departmentName;
        setHeadOfDepartment(headOfDepartment);
    }


    //SETTERS

    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            System.out.println("Invalid Department Name Entered!!!!");
        } else {
            this.entityName = departmentName;
        }
    }

    public void setHeadOfDepartment(Professor headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }


    //GETTERS

    public String getDepartmentName() {
        return entityName;
    }

    public Professor getHeadOfDepartment() {
        return headOfDepartment;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
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

    public void addFaculty(Faculty faculty) {
        if (faculty == null) {
            System.out.println("Invalid faculty");
            return;
        }
        faculties.add(faculty);
    }

    public void removeFaculty(Faculty faculty) {
        if (faculty == null) {
            System.out.println("Invalid faculty");
            return;
        }
        for (int i = 0; i < faculties.size(); i++) {
            if (faculties.get(i) == faculty) {
                faculties.remove(i);
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
        return faculties.size();
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
                " | HOD: " + headOfDepartment.getFirstName() + " " + headOfDepartment.getLastName() +
                " | Courses: " + courses.size() +
                " | Faculty: " + faculties.size() +
                " | Students: " + getNumberOfStudents() +
                " | Operational Cost: " + calculateOperationalCost();
    }

    @Override
    public String toString() {
        return "Department: " + entityName +
                " | HOD: " + headOfDepartment.getFirstName() + " " + headOfDepartment.getLastName() +
                " | Courses: " + courses.size() +
                " | Students: " + getNumberOfStudents();
    }
}

