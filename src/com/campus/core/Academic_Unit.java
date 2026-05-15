package com.campus.core;

import com.campus.academicunit.Equipment;
import java.util.ArrayList;

public abstract class Academic_Unit extends Campus_Entity {

    // FIELDS
    protected ArrayList<Equipment> equipments = new ArrayList<>();

    // CONSTRUCTORS
    public Academic_Unit() {
        super();
    }

    public Academic_Unit(String entityID, String entityName, String location) {
        super(entityID, entityName, location);
    }

    // SETTERS
    public void setEquipments(ArrayList<Equipment> equipments) {
        if (equipments == null) {
            System.out.println("Equipment list cannot be null");
        } else {
            this.equipments = equipments;
        }
    }

    // GETTERS
    public ArrayList<Equipment> getEquipments() { return equipments; }

    // OTHER METHODS
    // Defines the contract for how many students are enrolled in this specific academic unit
    public abstract int getNumberOfStudents();

    // Defines the contract for how each specific academic unit calculates its operational expenses
    @Override
    public abstract double calculateOperationalCost();

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Equipment Count : %d",
                super.toString(), equipments.size()
        );
    }
}