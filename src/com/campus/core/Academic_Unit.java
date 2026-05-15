package com.campus.core;

import com.campus.CampusRepository;
import com.campus.academicunit.Equipment;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Academic_Unit extends Campus_Entity  {

    // FIELDS
    CampusRepository<Equipment> repoEquipment = new CampusRepository<>();

    // CONSTRUCTORS
    public Academic_Unit() {
        super();
    }

    public Academic_Unit(String entityID, String entityName, String location) {
        super(entityID, entityName, location);
    }

    // SETTERS
    public void setEquipments(CampusRepository<Equipment> equipments) {
        if (equipments == null) {
            System.out.println("Equipment list cannot be null");
        } else {
            repoEquipment = equipments;
        }
    }

    // GETTERS
    public ArrayList<Equipment> getEquipments() { return repoEquipment.getAll(); }

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
                super.toString(), repoEquipment.getAll().size()
        );
    }
}