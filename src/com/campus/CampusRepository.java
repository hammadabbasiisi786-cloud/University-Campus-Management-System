package com.campus;

import java.io.Serializable;
import java.util.ArrayList;
public class CampusRepository <T extends Serializable> implements Serializable {
    protected ArrayList<T> items;
    protected T item;


    public CampusRepository() {

        this.items = new ArrayList<>();
    }


    public boolean contains(T item) {
        if(item == null){
            return false;
        }
        return items.contains(item);
    }

    public boolean add(T item) {
        if (item == null) {
            System.out.println("Invalid Input.");
            return false;
        }else if(items.contains(item)){
            System.out.println("Already exists." );
            return false;
        }
        items.add(item);
        System.out.println("Added." );
        return true;
    }

    public boolean remove(T item) {
        if (item == null) {
            System.out.println("Invalid Input");
            return false;
        }else if(!items.contains(item)){
            System.out.println("Not Found");
            return false;
        }
        items.remove(item);
        System.out.printf("Removed ");
        return false;
    }

    public boolean search(T item) {
        if(item == null) {
            System.out.println("Invalid Input");
            return false;
        }else if(!items.contains(item)){
            System.out.println("Not Found");
        }
        System.out.println("Found");
        return true;
    }

    public void displayAll(){
        for (T item : items) {
            System.out.println(item.toString());
        }
    }
}
