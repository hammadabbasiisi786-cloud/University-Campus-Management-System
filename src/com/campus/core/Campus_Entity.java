package com.campus.core;

abstract class Campus_Entity {
    protected String entityID;
    protected String entityName;
    protected String location;
    protected String status;


    public abstract double calculateOperationalCost() ;

    @Override
    public String toString() {
        return "-------Campus_Entity------" +
                "entityID='" + entityID + '\'' +
                ", entityName='" + entityName + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'';
    }
}
