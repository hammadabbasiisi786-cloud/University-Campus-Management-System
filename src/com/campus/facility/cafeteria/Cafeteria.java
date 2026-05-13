package com.campus.facility.cafeteria;

import com.campus.core.Facility;

import java.util.ArrayList;

public class Cafeteria extends Facility {
    protected ArrayList<String> menu;
    protected int staffCount;
    protected double staffSalary;
    protected double ingredientCost;
    protected String timing;
    protected static int totalCafeteria = 0;


    public Cafeteria(double usageFrequency, double maintenanceCost, int capacity, boolean isOpen, ArrayList<String> menu, int staffCount, double staffSalary, double ingredientCost, String timing) {
        super(usageFrequency, maintenanceCost, capacity, isOpen);
        this.menu = menu;
        this.staffCount = staffCount;
        this.staffSalary = staffSalary;
        this.ingredientCost = ingredientCost;
        this.timing = timing;
        totalCafeteria++;
    }
    public Cafeteria() {}
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

    public String toString(){
        return super.toString() +
                "Menu: " + menu
                + "Status: " + getStatus()
                + " Staff Count: " + staffCount
                + " Menu Items: " + menu.size()
                + " Timing: " + timing;
    }
}
