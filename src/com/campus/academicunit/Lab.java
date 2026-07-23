package com.campus.academicunit;

import com.campus.CampusRepository;
import com.campus.Person.*;
import com.campus.core.Academic_Unit;

import java.util.ArrayList;

public class Lab extends Academic_Unit {

    // FIELDS
    private static int idCounter = 0;
    private final String labNumber;
    private boolean isAvailable;
    private int capacity;
    private Teacher teacher;
    private CampusRepository<Student> repoStudent = new CampusRepository<>();

    // CONSTRUCTORS
    public Lab() {
        super();
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
    }

    public Lab(String entityID, String entityName, String location, boolean isAvailable, int capacity, Teacher teacher) {
        super(entityID, entityName, location);
        idCounter++;
        this.labNumber = "LAB-" + idCounter;
        setAvailability(isAvailable);
        setCapacity(capacity);
        setLabAssistant(teacher);
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int value) {
        idCounter = value;
    }

    // SETTERS
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setLabAssistant(Teacher teacher) {
        setTeacher(teacher);
    }

    // GETTERS
    public String getLabNumber() {
        return labNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            System.out.println("Capacity must be greater than 0");
        } else {
            this.capacity = capacity;
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        if (teacher == null) {
            System.out.println("Teacher cannot be null");
        } else {
            this.teacher = teacher;
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

    // OTHER METHODS

    @Override
    public int getNumberOfStudents() {
        return repoStudent.getAll().size();
    }

    public void addStudent(Student student) {
        if (repoStudent.getAll().size() >= capacity) {
            System.out.println("Maximum capacity reached");
            return;
        }
        repoStudent.add(student);
    }

    public void removeStudent(Student student) {
        repoStudent.remove(student);
    }

    @Override
    public double calculateOperationalCost() {
        double operationalCost = 0;
        for (Equipment eq : repoEquipment.getAll()) {
            operationalCost += eq.getOperationalCost();
        }
        operationalCost += getNumberOfStudents() * 50;
        return operationalCost;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("%s\n" + "  Lab Number    : %s\n" + "  Capacity      : %d\n" + "  Available     : %s\n" + "  Students      : %d\n" + "  Lab Assistant : %s", super.toString(), labNumber, capacity, (isAvailable ? "Yes" : "No"), repoStudent.getAll().size(), (teacher != null ? teacher.getName() : "Not Assigned"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lab lab = (Lab) o;
        if (this.getEntityID() == null || lab.getEntityID() == null) {
            return false;
        }
        return this.getEntityID().equals(lab.getEntityID());
    }
}