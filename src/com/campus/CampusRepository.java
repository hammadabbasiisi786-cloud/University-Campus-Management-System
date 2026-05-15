package com.campus;

import java.io.Serializable;
import java.util.ArrayList;

public class CampusRepository <T extends Serializable> implements Serializable {
    // FIELDS
    protected ArrayList<T> items;

    // CONSTRUCTORS
    public CampusRepository() {
        this.items = new ArrayList<>();
    }

    // SETTERS
    public void setItems(ArrayList<T> items) { this.items = items; }

    // GETTERS
    public ArrayList<T> getItems() { return items; }

    // Returns the number of items currently stored in the repository
    public int getSize() { return items.size(); }

    // OTHER METHODS

    // Returns true if the given item exists in the repository, false if null or not found
    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        return items.contains(item);
    }

    // Adds the given item to the repository if it is not null and not already present
    public boolean add(T item) {
        if (item == null) {
            System.out.println("Invalid Input.");
            return false;
        } else if (items.contains(item)) {
            System.out.println("Already exists.");
            return false;
        }
        items.add(item);
        System.out.println("Added.");
        return true;
    }

    // Removes the given item from the repository if it exists
    public boolean remove(T item) {
        if (item == null) {
            System.out.println("Invalid Input.");
            return false;
        } else if (!items.contains(item)) {
            System.out.println("Not Found.");
            return false;
        }
        items.remove(item);
        System.out.println("Removed.");
        return true;
    }

    // Searches for the given item in the repository and prints whether it was found
    public boolean search(T item) {
        if (item == null) {
            System.out.println("Invalid Input.");
            return false;
        } else if (!items.contains(item)) {
            System.out.println("Not Found.");
            return false;
        }
        System.out.println("Found.");
        return true;
    }

    // Prints the string representation of every item in the repository
    public void displayAll() {
        for (T item : items) {
            System.out.println(item.toString());
        }
    }

    //return String
    public ArrayList<T> getAll(){
        return items;
    }


    public void clear() {
        items.clear();
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Campus Repository ===\n" +
                        "  Total Items : %d",
                items.size()
        );
    }

    public int size() {
        return items.size();
    }
}