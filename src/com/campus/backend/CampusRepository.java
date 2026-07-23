package com.campus.backend;

import java.io.Serializable;
import java.util.ArrayList;

public class CampusRepository<T extends Serializable> implements Serializable {
    // FIELDS
    protected ArrayList<T> items;

    // CONSTRUCTORS
    public CampusRepository() {
        this.items = new ArrayList<>();
    }

    // SETTERS
    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    // GETTERS
    public int getSize() {
        return items.size();
    }

    // OTHER METHODS

    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        return items.contains(item);
    }

    public ArrayList<T> getAll() {
        return items;
    }

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

    public void displayAll() {
        for (T item : items) {
            System.out.println(item.toString());
        }
    }


    public void clear() {
        items.clear();
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Campus Repository ===\n" + "  Total Items : %d", items.size());
    }

    public int size() {
        return items.size();
    }
}
