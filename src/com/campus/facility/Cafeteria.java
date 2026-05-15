package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Cafeteria extends Facility {

    // FIELDS
    protected ArrayList<String> menu = new ArrayList<>();
    protected int staffCount;
    protected double staffSalary;
    protected double ingredientCost;
    protected String timing;
    protected static int totalCafeteria = 0;
    protected CampusRepository<String> repo = new CampusRepository<>();

    // CONSTRUCTORS
    public Cafeteria() {
        super();
    }

    public Cafeteria(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, int staffCount, double staffSalary, double ingredientCost, String timing) {
        super(entityID, entityName, location, status, maintenanceCost, usageFrequency, capacity, isOpen);
        this.staffCount = staffCount;
        this.staffSalary = staffSalary;
        this.ingredientCost = ingredientCost;
        this.timing = timing;
        totalCafeteria++;
    }

    // SETTERS
    public void setMenu(ArrayList<String> menu) { this.menu = menu; }
    public void setStaffCount(int staffCount) { this.staffCount = staffCount; }
    public void setStaffSalary(double staffSalary) { this.staffSalary = staffSalary; }
    public void setIngredientCost(double ingredientCost) { this.ingredientCost = ingredientCost; }
    public void setTiming(String timing) { this.timing = timing; }
    public static void setTotalCafeteria(int totalCafeteria) { Cafeteria.totalCafeteria = totalCafeteria; }

    // GETTERS
    public ArrayList<String> getMenu() { return menu; }
    public int getStaffCount() { return staffCount; }
    public double getStaffSalary() { return staffSalary; }
    public double getIngredientCost() { return ingredientCost; }
    public String getTiming() { return timing; }
    public static int getTotalCafeteria() { return totalCafeteria; }

    // OTHER METHODS

    // Increases staff count by the given number
    public void addStaff(int count) {
        if (count > 0) {
            this.staffCount += count;
            System.out.println("Staff added. Total staff: " + staffCount);
        } else {
            System.out.println("Invalid staff count entered");
        }
    }

    // Decreases staff count by the given number if enough staff exist
    public void removeStaff(int count) {
        if (count > 0 && count <= staffCount) {
            this.staffCount -= count;
            System.out.println("Staff removed. Total staff: " + staffCount);
        } else {
            System.out.println("Invalid staff count entered");
        }
    }

    // Adds a menu item to the cafeteria's menu repository
    public void addMenu(String menuItem) {
        repo.add(menuItem);
    }

    // Removes a menu item from the cafeteria's menu repository
    public void removeMenu(String menuItem) {
        repo.remove(menuItem);
    }

    // Searches for a menu item in the cafeteria's menu repository
    public void searchMenu(String menuItem) {
        repo.search(menuItem);
    }

    // Calculates operational cost by adding parent cost with staff salary and ingredient expenses
    @Override
    public double calculateOperationalCost() {
        return super.calculateOperationalCost() + (staffCount * staffSalary) + ingredientCost;
    }

    // TO-STRING
    @Override
    public String toString() {
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
                menu.size(),
                getStatus()
        );
    }
}