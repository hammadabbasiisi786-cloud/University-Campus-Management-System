package com.campus.academicunit;

import com.campus.Interfaces.Schedulable;
import com.campus.Person.*;

import java.util.*;

public class Course implements Schedulable {

    // FIELDS
    private static int idCounter = 0;
    private final String courseId;
    private String courseName;
    private int creditHour;
    private int maxCapacity;
    private String day;
    private String time;
    private Teacher teacher;
    private Classroom classroom;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();

    // CONSTRUCTORS
    public Course() {
        idCounter++;
        this.courseId = "CSC-" + idCounter;
    }

    public Course(String courseName, int creditHour, Teacher teacher, Classroom classroom, String day, String time) {
        idCounter++;
        this.courseId = "CSC-" + idCounter;
        setCourseName(courseName);
        setCreditHour(creditHour);
        setProfessor(teacher);
        this.day = day;
        this.time = time;
        setClassroom(classroom);
        setMaxCapacity();
    }

    // SETTERS
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

    // Syncs the max capacity of this course with its assigned classroom's capacity
    public void setMaxCapacity() {
        if (classroom != null) {
            this.maxCapacity = classroom.getCapacity();
        }
    }

    public void setProfessor(Teacher teacher) { this.teacher = teacher; }

    // Assigns a classroom to this course and books the slot; finds another slot if unavailable
    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
        if (classroom.isSlotAvailable(day, time)) {
            classroom.bookSlot(day, time);
            setMaxCapacity();
        } else {
            System.out.println("Slot not available for " + courseName + ". Finding another slot...");
            String result = generateSchedule();
            if (result.equals("No Slot Available")) {
                this.classroom = null;
                System.out.println("Could not assign any classroom to: " + courseName);
            }
            setMaxCapacity();
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

    public void setStudents(ArrayList<Student> students) { this.students = students; }
    public void setAssignments(ArrayList<Assignment> assignments) { this.assignments = assignments; }

    // GETTERS
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCreditHour() { return creditHour; }
    public int getMaxCapacity() { return maxCapacity; }
    public String getDay() { return day; }
    public String getTime() { return time; }
    public Teacher getTeacher() { return teacher; }
    public Classroom getClassroom() { return classroom; }
    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Assignment> getAssignments() { return assignments; }

    // OTHER METHODS

    // Adds a student to the course if not already enrolled and capacity allows
    public void addStudent(Student student) {
        if (student == null) {
            System.out.println("Invalid student");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).equals(student)) {
                System.out.println("Student already exists in this course");
                return;
            }
        }
        if (students.size() >= maxCapacity) {
            System.out.println("Maximum Capacity Reached!!!");
            return;
        }
        students.add(student);
    }

    // Removes a student from the course if they are found in the list
    public void removeStudent(Student student) {
        if (student == null) {
            System.out.println("Invalid student");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).equals(student)) {
                students.remove(i);
                System.out.println("Student removed successfully");
                return;
            }
        }
        System.out.println("Student not found in this course");
    }

    // Adds an assignment to this course's assignment list
    public void addAssignment(Assignment assignment) {
        if (assignment == null) {
            System.out.println("Invalid assignment");
            return;
        }
        assignments.add(assignment);
    }

    // Removes an assignment from this course's assignment list
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

    // Attempts to find and book an available classroom slot from the department's classrooms
    @Override
    public String generateSchedule() {
        if (this.classroom == null || this.classroom.getDepartment() == null) {
            System.out.println("No department assigned to course: " + courseName + ". Cannot reschedule.");
            return "No Slot Available";
        }

        String[] days  = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] times = {"9-12", "12-2", "2-4"};

        for (Classroom room : this.classroom.getDepartment().getDepartmentalClassrooms()) {
            for (String d : days) {
                for (String t : times) {
                    if (room.isSlotAvailable(d, t)) {
                        room.bookSlot(d, t);
                        this.classroom = room;
                        this.day = d;
                        this.time = t;
                        System.out.println("Course rescheduled → Room: " + room.getClassNumber()
                                + " | Day: " + d + " | Time: " + t);
                        return "Course: " + courseName
                                + " | Room: " + room.getClassNumber()
                                + " | Day: "  + d
                                + " | Time: " + t;
                    }
                }
            }
        }

        System.out.println("No available classroom found in department for: " + courseName);
        return "No Slot Available";
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Course ===\n" +
                        "  ID          : %s\n" +
                        "  Name        : %s\n" +
                        "  Credits     : %d\n" +
                        "  Teacher     : %s\n" +
                        "  Classroom   : %s\n" +
                        "  Day         : %s\n" +
                        "  Time        : %s\n" +
                        "  Students    : %d / %d",
                courseId,
                courseName,
                creditHour,
                (teacher   != null ? teacher.getName()        : "Not Assigned"),
                (classroom != null ? classroom.getClassNumber() : "Not Assigned"),
                day,
                time,
                students.size(),
                maxCapacity
        );
    }
}