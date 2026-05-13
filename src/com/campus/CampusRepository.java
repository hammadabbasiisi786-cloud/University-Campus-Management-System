package com.campus;

import java.util.ArrayList;

interface Identifiable{
    public String getID();
}
public class CampusRepository <T extends Identifiable>{
    protected ArrayList<T> items;
    public String repositoryName;

    public CampusRepository(String repositoryName){
        this.repositoryName = repositoryName;
        this.items = new ArrayList<>();
    }
    public CampusRepository(){
        this.repositoryName = "CampusRepository";
        this.items = new ArrayList<>();
    }
    public boolean contains(String id) {
        for (T item : items) {
            if (item.getID().equals(id)){ return true;}
        }
        return false;
    }
    public boolean add(T item){

        if(item == null){
            System.out.println("Item is null, Cannot add a Null value");
            return false;
        }
        if(contains(item.getID())){
            System.out.println("Item"+ item.getID()+ "already exists in " +  repositoryName);
            return false;
        }
        System.out.println(item.getID() + " added to " + repositoryName);
        return true;
    }

    public void remove(String id){
        for(T item : items){
            if(item.getID().equals(id)){}
        }
    }
}
