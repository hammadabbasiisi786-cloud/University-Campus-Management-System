package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Cafeteria extends Facility {

    // FIELDS
    private int staffCount;
    private double staffSalary;
    private double ingredientCost;
    private String timing;
    private static int totalCafeteria = 0;
    private CampusRepository<String> repoMenu = new CampusRepository<>();

    public Cafeteria() {
        super();
        totalCafeteria++;
    }

    public Cafeteria(String entityID, String entityName, String location, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, int staffCount, double staffSalary, double ingredientCost, String timing, CampusRepository<String> repoMenu) {
        super(entityID, entityName, location, maintenanceCost, usageFrequency, capacity, isOpen);
        totalCafeteria++;
        setStaffCount(staffCount);
        setStaffSalary(staffSalary);
        setIngredientCost(ingredientCost);
        setTiming(timing);
    }

    // SETTERS
    public void setStaffCount(int staffCount) {
        if (staffCount < 0) {
            System.out.println("Staff count cannot be negative");
        } else {
            this.staffCount = staffCount;
        }
    }

    public void setStaffSalary(double staffSalary) {
        if (staffSalary < 0) {
            System.out.println("Staff salary cannot be negative");
        } else {
            this.staffSalary = staffSalary;
        }
    }

    public void setIngredientCost(double ingredientCost) {
        if (ingredientCost < 0) {
            System.out.println("Ingredient cost cannot be negative");
        } else {
            this.ingredientCost = ingredientCost;
        }
    }

    public void setTiming(String timing) {
        if (timing == null && timing.isEmpty()) {
            System.out.println("Invalid timing entered");
        } else {
            this.timing = timing;
        }
    }

    public void setRepo(CampusRepository<String> repoMenu) {
        if (repoMenu == null) {
            System.out.println("Repository cannot be null");
        } else {
            this.repoMenu = repoMenu;
        }
    }

    // GETTERS
    public ArrayList<String> getMenu() {
        return repoMenu.getAll();
    }
    public int getStaffCount() {return staffCount;}
    public double getStaffSalary() {return staffSalary;}
    public double getIngredientCost() {return ingredientCost;}
    public String getTiming() {return timing;}
    public static int getTotalCafeteria() {return totalCafeteria;}

    // Increases staff count by the given number
    public void addStaff(int count) {
        if (count > 0) {
            setStaffCount(staffCount + count);
            System.out.println("Staff added. Total staff: " + staffCount);
        } else {
            System.out.println("Invalid staff count entered");
        }
    }

    // Decreases staff count by the given number if enough staff exist
    public void removeStaff(int count) {
        if (count > 0 && count <= staffCount) {
            setStaffCount(staffCount - count);
            System.out.println("Staff removed. Total staff: " + staffCount);
        } else {
            System.out.println("Invalid staff count entered");
        }
    }

    // Adds a menu item to the cafeteria's menu repository
    public void addMenu(String menuItem) {
        repoMenu.add(menuItem);
    }

    // Removes a menu item from the cafeteria's menu repository
    public void removeMenu(String menuItem) {
        repoMenu.remove(menuItem);
    }

    // Searches for a menu item in the cafeteria's menu repository
    public void searchMenu(String menuItem) {
        repoMenu.search(menuItem);
    }

    @Override
    public double calculateOperationalCost(){
        return super.calculateOperationalCost() + (staffCount * staffSalary) + ingredientCost;
    }

    @Override
    public String toString(){
        return String.format(
                "%s\n" +
                        "  Staff Count     : %d\n" +
                        "  Staff Salary    : %.2f\n" +
                        "  Ingredient Cost : %.2f\n" +
                        "  Timing          : %s\n" +
                        "  Menu Items      : %d\n" +
                        "  Status          : %s",
                super.toString(),
                staffCount,
                staffSalary,
                ingredientCost,
                timing,
                repoMenu.getAll().size(),
                getStatus()
        );
    }
}