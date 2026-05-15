package com.campus.academicunit;

import com.campus.Interfaces.Reportable;
import com.campus.Person.*;
import com.campus.core.Academic_Unit;

import java.util.*;

public class Department extends Academic_Unit implements Reportable {

    // FIELDS
    private Teacher headOfDepartment;
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Classroom> departmentalClassrooms = new ArrayList<>();
    private ArrayList<Lab> departmentalLabs = new ArrayList<>();

    // CONSTRUCTORS
    public Department() {
        super();
    }

    public Department(String entityID, String entityName, String location, Teacher headOfDepartment) {
        super(entityID, entityName, location);
        this.headOfDepartment = headOfDepartment;
    }

    // SETTERS
    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            System.out.println("Invalid Department Name Entered!!!!");
        } else {
            this.entityName = departmentName;
        }
    }
    public void setHeadOfDepartment(Teacher headOfDepartment) { this.headOfDepartment = headOfDepartment; }
    public void setTeachers(ArrayList<Teacher> teachers) { this.teachers = teachers; }
    public void setCourses(ArrayList<Course> courses) { this.courses = courses; }
    public void setDepartmentalClassrooms(ArrayList<Classroom> departmentalClassrooms) { this.departmentalClassrooms = departmentalClassrooms; }
    public void setDepartmentalLabs(ArrayList<Lab> departmentalLabs) { this.departmentalLabs = departmentalLabs; }

    // GETTERS
    public String getDepartmentName() { return entityName; }
    public Teacher getHeadOfDepartment() { return headOfDepartment; }
    public ArrayList<Teacher> getTeachers() { return teachers; }
    public ArrayList<Course> getCourses() { return courses; }
    public ArrayList<Classroom> getDepartmentalClassrooms() { return departmentalClassrooms; }
    public ArrayList<Lab> getDepartmentalLabs() { return departmentalLabs; }

    // OTHER METHODS

    // Returns the total number of unique students enrolled across all courses in this department
    @Override
    public int getNumberOfStudents() {
        ArrayList<String> countedIDs = new ArrayList<>();

        for (Course course : courses) {
            for (Student student : course.getStudents()) {
                String studentID = student.getStudentID();
                if (!countedIDs.contains(studentID)) {
                    countedIDs.add(studentID);
                }
            }
        }
        return countedIDs.size();
    }

    // Calculates total operational cost by summing equipment, classroom, lab costs and a per-student fee
    @Override
    public double calculateOperationalCost() {
        double totalCost = 0;

        for (Equipment eq : equipments) {
            totalCost += eq.getOperationalCost();
        }

        for (Classroom room : departmentalClassrooms) {
            totalCost += room.calculateOperationalCost();
        }

        for (Lab room : departmentalLabs) {
            totalCost += room.calculateOperationalCost();
        }

        totalCost += getNumberOfStudents() * 100;

        return totalCost;
    }

    // Adds a course to this department's course list
    public void addCourse(Course course) {
        if (course == null) {
            System.out.println("Invalid course");
            return;
        }
        courses.add(course);
    }

    // Removes a course from this department's course list
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

    // Adds a teacher to this department's faculty list
    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            System.out.println("Invalid faculty");
            return;
        }
        teachers.add(teacher);
    }

    // Removes a teacher from this department's faculty list
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

    // Adds a classroom to the department and sets the bidirectional relationship
    public void addClassroom(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }
        classroom.setDepartment(this);
        departmentalClassrooms.add(classroom);
    }

    // Removes a classroom from this department's classroom list
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

    // Marks a classroom as unavailable, removes it, and attempts to reschedule any affected courses
    public void handleClassroomUnavailable(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }

        System.out.println("Classroom " + classroom.getClassNumber()
                + " is now unavailable. Rescheduling affected courses...");

        classroom.markUnavailable();
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

    // Prints a full formatted academic report for this department including faculty, courses, and financials
    @Override
    public void generateReport() {
        System.out.println("==================================================");
        System.out.println("            DEPARTMENTAL ACADEMIC REPORT          ");
        System.out.println("==================================================");

        System.out.printf("%-20s: %s\n", "Department Name", entityName);
        System.out.printf("%-20s: %s\n", "Head of Dept",
                (headOfDepartment != null ? headOfDepartment.getName() : "Not Assigned"));
        System.out.println("--------------------------------------------------");

        System.out.println("STATISTICS OVERVIEW:");
        System.out.printf(" - Total Faculty:    %d\n", teachers.size());
        System.out.printf(" - Total Courses:    %d\n", courses.size());
        System.out.printf(" - Total Students:   %d\n", getNumberOfStudents());
        System.out.printf(" - Classrooms/Labs:  %d/%d\n", departmentalClassrooms.size(), departmentalLabs.size());
        System.out.println("--------------------------------------------------");

        System.out.println("FACULTY MEMBERS:");
        if (teachers.isEmpty()) {
            System.out.println(" [No faculty assigned]");
        } else {
            for (Teacher t : teachers) {
                System.out.println(" • " + t.getName() + " (" + t.getQualification() + ")");
            }
        }
        System.out.println("--------------------------------------------------");

        System.out.println("ACTIVE COURSES:");
        if (courses.isEmpty()) {
            System.out.println(" [No courses currently offered]");
        } else {
            for (Course c : courses) {
                System.out.println(" □ " + c.getCourseName());
            }
        }
        System.out.println("--------------------------------------------------");

        System.out.printf("TOTAL OPERATIONAL COST: $%,.2f\n", calculateOperationalCost());
        System.out.println("==================================================");
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Head of Dept  : %s\n" +
                        "  Teachers      : %d\n" +
                        "  Courses       : %d\n" +
                        "  Students      : %d\n" +
                        "  Classrooms    : %d\n" +
                        "  Labs          : %d",
                super.toString(),
                (headOfDepartment != null ? headOfDepartment.getName() : "Not Assigned"),
                teachers.size(),
                courses.size(),
                getNumberOfStudents(),
                departmentalClassrooms.size(),
                departmentalLabs.size()
        );
    }
}