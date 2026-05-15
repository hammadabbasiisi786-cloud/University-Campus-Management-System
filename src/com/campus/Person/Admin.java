package com.campus.Person;

public class Admin {

    // FIELDS
    private String name;

    // CONSTRUCTORS
    public Admin() {}

    public Admin(String name) {
        setName(name);
    }

    // SETTERS

    // Name must not be null or empty
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name entered");
        } else {
            this.name = name;
        }
    }

    // GETTERS
    public String getName() { return name; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Admin ===\n" +
                        "  Name : %s",
                name
        );
    }
}