package com.campus.Person;

public class Admin {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Admin [Name: " + name + "]";
    }
}