package com.campus.core;

import com.campus.CampusRepository;
import com.campus.academicunit.Equipment;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Academic_Unit extends Campus_Entity {

    // FIELDS
    protected CampusRepository<Equipment> repoEquipment = new CampusRepository<>();

    // CONSTRUCTORS
    public Academic_Unit() {
        super();
    }

    public Academic_Unit(String entityID, String entityName, String location) {
        super(entityID, entityName, location);
    }

    // GETTERS
    public ArrayList<Equipment> getEquipments() {
        return repoEquipment.getAll();
    }

    // SETTERS
    public void setEquipments(CampusRepository<Equipment> equipments) {
        if (equipments == null) {
            System.out.println("Equipment list cannot be null");
        } else {
            repoEquipment = equipments;
        }
    }

    // OTHER METHODS

    public void addEquipment(Equipment e) {
        repoEquipment.add(e);
    }

    public void removeEquipment(Equipment e) {
        repoEquipment.remove(e);
    }

    public abstract int getNumberOfStudents();

    @Override
    public abstract double calculateOperationalCost();

    // TO-STRING
    @Override
    public String toString() {
        return String.format("%s\n" + "  Equipment Count : %d", super.toString(), repoEquipment.getAll().size());
    }
}