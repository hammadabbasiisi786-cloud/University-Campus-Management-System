package com.campus.core;

public abstract class CampusEntity {
    protected String entityID;
    protected String name;
    protected String location;

    public abstract double calculateOperationalCost();
}


