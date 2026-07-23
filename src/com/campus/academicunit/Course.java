package com.campus.academicunit;

import com.campus.CampusRepository;
import com.campus.Interfaces.Schedulable;
import com.campus.Person.*;

import java.io.Serializable;
import java.util.*;

public class Course implements Schedulable, Serializable {

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
    private CampusRepository<Student> repoStudent = new CampusRepository<>();
    private CampusRepository<Assignment> repoAssignment = new CampusRepository<>();

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
        setDay(day);
        setTime(time);
        setClassroom(classroom);
        setMaxCapacity();
    }

    public static void setIdCounter(int value) {
        idCounter = value;
    }

    public static int getTotalCourses() {
        return idCounter;
    }

    public void setMaxCapacity() {
        if (classroom != null) {
            this.maxCapacity = classroom.getCapacity();
        }
    }

    public void setProfessor(Teacher teacher) {
        if (teacher == null) {
            System.out.println("Teacher cannot be null");
        } else {
            this.teacher = teacher;
        }
    }

    // GETTERS
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    // SETTERS
    public void setCourseName(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            System.out.println("Invalid Course Name Entered!!!!");
        } else {
            this.courseName = courseName;
        }
    }

    public int getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(int creditHour) {
        if (creditHour <= 0 || creditHour > 4) {
            System.out.println("Invalid Credit Hour Entered!!!!");
        } else {
            this.creditHour = creditHour;
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        if (day == null || day.isEmpty()) {
            System.out.println("Invalid day entered");
        } else {
            this.day = day;
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        if (time == null || time.isEmpty()) {
            System.out.println("Invalid time entered");
        } else {
            this.time = time;
        }
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        if (classroom == null) {
            System.out.println("Classroom cannot be null");
            return;
        }
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

    public ArrayList<Student> getStudents() {
        return repoStudent.getAll();
    }

    public void setStudents(ArrayList<Student> students) {
        if (students == null) {
            System.out.println("Student list cannot be null");
        } else {
            repoStudent.setItems(students);
        }
    }

    public ArrayList<Assignment> getAssignments() {
        return repoAssignment.getAll();
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        if (assignments != null) {
            repoAssignment.setItems(assignments);
        } else {
            System.out.println("Assignment list cannot be null");
        }
    }

    // OTHER METHODS

    public void addStudent(Student student) {
        if (repoStudent.getAll().size() >= maxCapacity) {
            System.out.println("Maximum Capacity Reached!!!");
            return;
        }
        repoStudent.add(student);
        student.addCourse(this); // Bidirectional relation
    }

    public void removeStudent(Student student) {
        repoStudent.remove(student);
        student.removeCourse(this); // Bidirectional relation
    }

    public void searchStudent(Student student) {
        repoStudent.search(student);
    }

    public void addAssignment(Assignment assignment) {
        repoAssignment.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        repoAssignment.remove(assignment);
    }

    public void searchAssignment(Assignment assignment) {
        repoAssignment.search(assignment);
    }

    @Override
    public String generateSchedule() {
        if (this.classroom == null || this.classroom.getDepartment() == null) {
            System.out.println("No department assigned to course: " + courseName + ". Cannot reschedule.");
            return "No Slot Available";
        }

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] times = {"9-12", "12-2", "2-4"};

        for (Classroom room : this.classroom.getDepartment().getDepartmentalClassrooms()) {
            for (String d : days) {
                for (String t : times) {
                    if (room.isSlotAvailable(d, t)) {
                        room.bookSlot(d, t);
                        this.classroom = room;
                        setDay(d);
                        setTime(t);
                        System.out.println("Course rescheduled → Room: " + room.getClassNumber() + " | Day: " + d + " | Time: " + t);
                        return "Course: " + courseName + " | Room: " + room.getClassNumber() + " | Day: " + d + " | Time: " + t;
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
        return String.format("=== Course ===\n" + "  ID          : %s\n" + "  Name        : %s\n" + "  Credits     : %d\n" + "  Teacher     : %s\n" + "  Classroom   : %s\n" + "  Day         : %s\n" + "  Time        : %s\n" + "  Students    : %d / %d", courseId, courseName, creditHour, (teacher != null ? teacher.getName() : "Not Assigned"), (classroom != null ? classroom.getClassNumber() : "Not Assigned"), day, time, repoStudent.getAll().size(), maxCapacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId.equals(course.courseId);
    }
}