package com.campus.academicunit;

import com.campus.CampusRepository;
import com.campus.core.*;

import java.util.ArrayList;

public class Classroom extends Academic_Unit {

    // FIELDS
    private static int idCounter = 0;
    private final String classNumber;
    private int capacity;
    private boolean available = true;
    private CampusRepository<String> repoOccupiedSlots = new CampusRepository<>();
    private Department department;

    // CONSTRUCTORS
    public Classroom() {
        super();
        idCounter++;
        this.classNumber = "Class-" + idCounter;
    }

    public Classroom(String entityID, String entityName, String location, int capacity, boolean available) {
        super(entityID, entityName, location);
        idCounter++;
        this.classNumber = "Class-" + idCounter;
        setCapacity(capacity);
        setAvailable(available);
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int value) {
        idCounter = value;
    }

    // GETTERS
    public String getClassNumber() {
        return classNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    // SETTERS
    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            System.out.println("Capacity must be greater than 0");
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ArrayList<String> getOccupiedSlots() {
        return repoOccupiedSlots.getAll();
    }

    public void setOccupiedSlots(CampusRepository<String> occupiedSlots) {
        if (occupiedSlots == null) {
            System.out.println("Occupied slots list cannot be null");
        } else {
            repoOccupiedSlots = occupiedSlots;
        }
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        if (department == null) {
            System.out.println("Department cannot be null");
        } else {
            this.department = department;
        }
    }

    // OTHER METHODS

    public boolean isSlotAvailable(String day, String time) {
        String schedule = day + "-" + time;
        return !repoOccupiedSlots.contains(schedule);
    }

    public void bookSlot(String day, String time) {
        String schedule = day + "-" + time;
        repoOccupiedSlots.add(schedule);
    }

    public void unbookSlot(String day, String time) {
        String schedule = day + "-" + time;
        repoOccupiedSlots.remove(schedule);
    }

    @Override
    public int getNumberOfStudents() {
        return 0;
    }

    public ArrayList<String> markUnavailable() {
        setAvailable(false);
        setStatus("Closed");
        ArrayList<String> affectedSlots = new ArrayList<>(repoOccupiedSlots.getAll());
        repoOccupiedSlots.clear();
        return affectedSlots;
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
        return String.format(
                "%s\n" + "  Class Number  : %s\n" + "  Capacity      : %d\n" + "  Available     : %s\n"
                        + "  Booked Slots  : %d\n" + "  Department    : %s",
                super.toString(), classNumber, capacity, (available ? "Yes" : "No"), repoOccupiedSlots.size(),
                (department != null ? department.getDepartmentName() : "Unassigned"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Classroom classroom = (Classroom) o;
        if (this.getEntityID() == null || classroom.getEntityID() == null) {
            return false;
        }
        return this.getEntityID().equals(classroom.getEntityID());
    }
}