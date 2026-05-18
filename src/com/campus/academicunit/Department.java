package com.campus.academicunit;

import com.campus.CampusRepository;
import com.campus.Interfaces.Reportable;
import com.campus.Person.*;
import com.campus.core.Academic_Unit;

import java.io.Serializable;
import java.util.*;

public class Department extends Academic_Unit implements Reportable {

    // FIELDS
    private Teacher headOfDepartment;
    private CampusRepository<Lab> repoLab = new CampusRepository<>();
    private CampusRepository<Course> repoCourse = new CampusRepository<>();
    private CampusRepository<Teacher> repoTeacher = new CampusRepository<>();
    private CampusRepository<Classroom> repoClassroom = new CampusRepository<>();

    // CONSTRUCTORS
    public Department() {
        super();
    }

    public Department(String departmentName, Teacher headOfDepartment) {
        super();
        setDepartmentName(departmentName);
        setHeadOfDepartment(headOfDepartment);
    }

    public Department(String entityID, String entityName, String location, Teacher headOfDepartment) {
        super(entityID, entityName, location);
        setHeadOfDepartment(headOfDepartment);
    }

    // SETTERS
    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            System.out.println("Invalid Department Name Entered!!!!");
        } else {
            this.entityName = departmentName;
        }
    }

    public void setHeadOfDepartment(Teacher headOfDepartment) {
        if (headOfDepartment == null) {
            System.out.println("Head of Department cannot be null");
        } else {
            this.headOfDepartment = headOfDepartment;
        }
    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        if (teachers == null) {
            System.out.println("Teachers list cannot be null");
        } else {
            repoTeacher.setItems(teachers);
        }
    }

    public void setCourses(ArrayList<Course> courses) {
        if (courses == null) {
            System.out.println("Courses list cannot be null");
        } else {
            repoCourse.setItems(courses);
        }
    }

    public void setDepartmentalClassrooms(ArrayList<Classroom> departmentalClassrooms) {
        if (departmentalClassrooms == null) {
            System.out.println("Classrooms list cannot be null");
        } else {
            repoClassroom.setItems(departmentalClassrooms);
        }
    }

    public void setDepartmentalLabs(ArrayList<Lab> departmentalLabs) {
        if (departmentalLabs == null) {
            System.out.println("Labs list cannot be null");
        } else {
            repoLab.setItems(departmentalLabs);
        }
    }

    // GETTERS
    public String getDepartmentName() {
        return entityName;
    }

    public Teacher getHeadOfDepartment() {
        return headOfDepartment;
    }

    public ArrayList<Teacher> getTeachers() {
        return repoTeacher.getAll();
    }

    public ArrayList<Course> getCourses() {
        return repoCourse.getAll();
    }

    public ArrayList<Classroom> getDepartmentalClassrooms() {
        return repoClassroom.getAll();
    }

    public ArrayList<Lab> getDepartmentalLabs() {
        return repoLab.getAll();
    }

    // OTHER METHODS

    // Returns the total number of unique students enrolled across all courses in
    // this department
    @Override
    public int getNumberOfStudents() {
        ArrayList<String> countedIDs = new ArrayList<>();
        for (Course course : repoCourse.getAll()) {
            for (Student student : course.getStudents()) {
                String studentID = student.getStudentID();
                if (!countedIDs.contains(studentID)) {
                    countedIDs.add(studentID);
                }
            }
        }
        return countedIDs.size();
    }

    // Calculates total operational cost by summing equipment, classroom, lab costs
    // and a per-student fee
    @Override
    public double calculateOperationalCost() {
        double totalCost = 0;

        for (Equipment eq : repoEquipment.getAll()) {
            totalCost += eq.getOperationalCost();
        }

        for (Classroom room : repoClassroom.getAll()) {
            totalCost += room.calculateOperationalCost();
        }

        for (Lab room : repoLab.getAll()) {
            totalCost += room.calculateOperationalCost();
        }

        totalCost += getNumberOfStudents() * 100;

        return totalCost;
    }

    // Adds a course to this department's course list
    public void addCourse(Course course) {
        repoCourse.add(course);
    }

    // Removes a course from this department's course list
    public void removeCourse(Course course) {
        repoCourse.remove(course);
    }

    // Adds a teacher to this department's faculty list
    public void addTeacher(Teacher teacher) {
        repoTeacher.add(teacher);
    }

    // Removes a teacher from this department's faculty list
    public void removeTeacher(Teacher teacher) {
        repoTeacher.remove(teacher);
    }

    // Adds a classroom to the department and sets the bidirectional relationship
    public void addClassroom(Classroom classroom) {
        if (classroom != null) {
            classroom.setDepartment(this);
            repoClassroom.add(classroom);
        }
    }

    // Removes a classroom from this department's classroom list
    public void removeClassroom(Classroom classroom) {
        repoClassroom.remove(classroom);
    }

    // Adds a lab to the department
    public void addLab(Lab lab) {
        repoLab.add(lab);
    }

    // Removes a lab from the department
    public void removeLab(Lab lab) {
        repoLab.remove(lab);
    }

    // Marks a classroom as unavailable, removes it, and attempts to reschedule any
    // affected courses
    public void handleClassroomUnavailable(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom");
            return;
        }

        System.out.println("Classroom " + classroom.getClassNumber()
                + " is now unavailable. Rescheduling affected courses...");

        classroom.markUnavailable();
        removeClassroom(classroom);

        for (Course course : repoCourse.getAll()) {
            if (course.getClassroom() != null && course.getClassroom().equals(classroom)) {
                System.out.println("Rescheduling: " + course.getCourseName());
                String result = course.generateSchedule();

                if (result.equals("No Slot Available")) {
                    System.out.println("WARNING: Could not reschedule " + course.getCourseName());
                }
            }
        }
    }

    // Prints a full formatted academic report for this department including
    // faculty, courses, and financials
    @Override
    public void generateReport() {
        String separator = "==================================================";
        String subSeparator = "--------------------------------------------------";

        System.out.println(separator);
        System.out.println("            DEPARTMENTAL ACADEMIC REPORT          ");
        System.out.println(separator);

        System.out.printf("%-20s: %s\n", "Department Name", entityName);
        System.out.printf("%-20s: %s\n", "Head of Dept",
                (headOfDepartment != null ? headOfDepartment.getName() : "Not Assigned"));
        System.out.println(subSeparator);

        System.out.println("STATISTICS OVERVIEW:");
        System.out.printf(" - Total Faculty:    %d\n", repoTeacher.getAll().size());
        System.out.printf(" - Total Courses:    %d\n", repoCourse.getAll().size());
        System.out.printf(" - Total Students:   %d\n", getNumberOfStudents());
        System.out.printf(" - Classrooms/Labs:  %d/%d\n", repoClassroom.getAll().size(), repoLab.getAll().size());
        System.out.println(subSeparator);

        System.out.println("FACULTY MEMBERS:");
        if (repoTeacher.getAll().isEmpty()) {
            System.out.println(" [No faculty assigned]");
        } else {
            for (Teacher t : repoTeacher.getAll()) {
                System.out.println(" • " + t.getName() + " (" + t.getQualification() + ")");
            }
        }
        System.out.println(subSeparator);

        System.out.println("ACTIVE COURSES:");
        if (repoCourse.getAll().isEmpty()) {
            System.out.println(" [No courses currently offered]");
        } else {
            for (Course c : repoCourse.getAll()) {
                System.out.println(" □ " + c.getCourseName());
            }
        }

        System.out.println(subSeparator);
        System.out.printf("TOTAL OPERATIONAL COST: $%,.2f\n", calculateOperationalCost());
        System.out.println(separator);
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
                repoTeacher.getAll().size(),
                repoCourse.getAll().size(),
                getNumberOfStudents(),
                repoClassroom.getAll().size(),
                repoLab.getAll().size());
    }
}