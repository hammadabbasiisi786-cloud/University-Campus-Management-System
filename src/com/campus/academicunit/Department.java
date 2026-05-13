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
    public void generateReport() {
        String separator = "==================================================";
        String subSeparator = "--------------------------------------------------";

        System.out.println(separator);
        System.out.println("            DEPARTMENTAL ACADEMIC REPORT          ");
        System.out.println(separator);

        // Basic Information
        System.out.printf("%-20s: %s\n", "Department Name", entityName);
        System.out.printf("%-20s: %s\n", "Head of Dept", (headOfDepartment != null ? headOfDepartment.getName() : "Not Assigned"));
        System.out.println(subSeparator);

        // Academic Statistics
        System.out.println("STATISTICS OVERVIEW:");
        System.out.printf(" - Total Faculty:    %d\n", teachers.size());
        System.out.printf(" - Total Courses:    %d\n", courses.size());
        System.out.printf(" - Total Students:   %d\n", getNumberOfStudents());
        System.out.printf(" - Classrooms/Labs:  %d/%d\n", departmentalClassrooms.size(), deparmentalLabs.size());
        System.out.println(subSeparator);

        // Faculty Roster (Brief List)
        System.out.println("FACULTY MEMBERS:");
        if (teachers.isEmpty()) {
            System.out.println(" [No faculty assigned]");
        } else {
            for (Teacher t : teachers) {
                System.out.println(" • " + t.getName() + " (" + t.getQualification() + ")");
            }
        }
        System.out.println(subSeparator);

        // Course List
        System.out.println("ACTIVE COURSES:");
        if (courses.isEmpty()) {
            System.out.println(" [No courses currently offered]");
        } else {
            for (Course c : courses) {
                // Assuming Course has a getName() or getCourseName() method
                System.out.println(" □ " + c.getCourseName());
            }
        }

        // Financial Summary
        System.out.println(subSeparator);
        System.out.printf("TOTAL OPERATIONAL COST: $%,.2f\n", calculateOperationalCost());
        System.out.println(separator);
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
                String result = course.generateSchedule();

                if (result.equals("No Slot Available")) {
                    System.out.println("WARNING: Could not reschedule " + course.getCourseName());
                }
            }
        }
    }




    public int getNumberOfStudents() {
        ArrayList<String> tempcurrentIDs = new ArrayList<>();
        int numberOfStudents = 0;

        for (Course course : courses) {
            ArrayList<Student> tempstudentarray = course.getStudents();

            for (Student student : tempstudentarray) {
                String studentID = student.getStudentID();
                if (!(tempcurrentIDs.contains(studentID))) {
                    tempcurrentIDs.add(studentID);
                }
            }
        }
        return numberOfStudents = tempcurrentIDs.size();

    }




    @Override
    public double calculateOperationalCost() {
        double totalSum =0;


        if (equipments != null) {
            for (Equipment eq : equipments) {
                totalSum += eq.getOperationalCost();
            }
        }


        if (equipments != null) {
            for (Classroom room : departmentalClassrooms) {
                totalSum += room.calculateOperationalCost();
            }
        }


        if (equipments != null) {
            for (Lab room : deparmentalLabs) {
                totalSum += room.calculateOperationalCost();
            }
        }

        int students = getNumberOfStudents();

        double operationalCost = totalSum + (students * 100);

        return operationalCost;
    }
}

