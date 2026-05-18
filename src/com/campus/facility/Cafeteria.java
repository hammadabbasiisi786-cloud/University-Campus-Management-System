package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Cafeteria extends Facility {

    // FIELDS
    private int cafeteriaUsage;
    private int noOfStaff;
    private String timing;
    private CampusRepository<String> repoMenu = new CampusRepository<>();

    // CONSTRUCTORS
    public Cafeteria() {
        super();
    }

    public Cafeteria(String entityID, String entityName, String location, String status, double maintenanceCost, int capacity, int noOfStaff, String timing) {
        super(entityID, entityName, location, status, maintenanceCost, capacity);
        setNoOfStaff(noOfStaff);
        setTiming(timing);
    }

    // SETTERS
    public void setNoOfStaff(int noOfStaff) {
        if (noOfStaff < 0) {
            System.out.println("Staff count cannot be negative");
        } else {
            this.noOfStaff = noOfStaff;
        }
    }

    public void setTiming(String timing) {
        if (timing == null || timing.isEmpty()) {
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
    public ArrayList<String> getMenu()              { return repoMenu.getAll(); }
    public int getNoOfStaff()                       { return noOfStaff; }
    public String getTiming()                       { return timing; }
    public int getCafeteriaUsage()                  { return cafeteriaUsage; }

    // Increases staff count by the given number
    public void addStaff(int count) {
        if (count > 0) {
            setNoOfStaff(noOfStaff + count);
            System.out.println("Staff added. Total staff: " + noOfStaff);
        } else {
            System.out.println("Invalid staff count entered");
        }
    }

    // Decreases staff count by the given number if enough staff exist
    public void removeStaff(int count) {
        if (count > 0 && count <= noOfStaff) {
            setNoOfStaff(noOfStaff - count);
            System.out.println("Staff removed. Total staff: " + noOfStaff);
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

    // Serves an order — increments this cafeteria's usage and the system-wide facility usage
    public void serveOrder() {
        cafeteriaUsage++;
        incrementFacilityUsage();
        System.out.println("Order served. Cafeteria Usage: " + cafeteriaUsage +
                " | Total Facility Usage: " + getTotalFacilityUsage());
    }

    // Returns maintenanceCost (from Facility) as the base operational cost
    @Override
    public double calculateOperationalCost() {
        return maintenanceCost * cafeteriaUsage;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  No Of Staff      : %d\n" +
                        "  Timing           : %s\n" +
                        "  Menu Items       : %d\n" +
                        "  Cafeteria Usage  : %d",
                super.toString(),
                noOfStaff,
                timing,
                repoMenu.getAll().size(),
                cafeteriaUsage
        );
    }
}