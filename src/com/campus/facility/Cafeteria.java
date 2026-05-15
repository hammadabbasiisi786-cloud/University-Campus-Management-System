package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Cafeteria extends Facility {
    protected ArrayList<String> menu;
    protected int staffCount;
    protected double staffSalary;
    protected double ingredientCost;
    protected String timing;
    protected static int totalCafeteria = 0;
    protected CampusRepository<String> repo = new CampusRepository<>();

    public Cafeteria() {}

    public Cafeteria(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, int staffCount, double staffSalary, double ingredientCost, String timing) {
        super(entityID, entityName, location, status, maintenanceCost, usageFrequency, capacity, isOpen);
        this.staffCount = staffCount;
        this.staffSalary = staffSalary;
        this.ingredientCost = ingredientCost;
        this.timing = timing;
        menu = new ArrayList<>();
    }

    public ArrayList<String> getMenu() {return menu;}
    public int getStaffCount() {return staffCount;}
    public double getStaffSalary() {return staffSalary;}
    public double getIngredientCost() {return ingredientCost;}
    public String getTiming() {return timing;}
    public static int getTotalCafeteria() {return totalCafeteria;}

    public void addStaff(){
        this.staffCount += staffCount;
        System.out.println("Staff added");
    }
    public void removeStaff(){
        this.staffCount -= staffCount;
        System.out.println("Staff removed");
    }

    public void addMenu(String menuItem){
        repo.add(menuItem);
    }
    public void removeMenu(String menuItem){
        repo.remove(menuItem);
    }
    public void searchMenu(String menuItem){
        repo.search(menuItem);
    }

    public double calculateOperationalCost(){
        return super.calculateOperationalCost() + (staffCount * staffSalary)+ ingredientCost;
    }
    public String toString(){
        return "---------Cafeteria--------"
                + super.toString()
                + "\nMenu: " + menu
                + "\nStatus: " + getStatus()
                + "\nStaff Count: " + staffCount
                + "\nMenu Items: " + menu.size()
                + "\nTiming: " + timing;
    }
}
