package com.campus;

import com.campus.Person.*;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginManager implements Serializable {

    // FIELDS
    private ArrayList<String> credentials = new ArrayList<>();  // stores "username:password"
    private ArrayList<Object> users = new ArrayList<>();        // stores the person object
    private Object loggedInUser = null;

    // CONSTRUCTORS
    public LoginManager() {}

    // SETTERS
    public void setLoggedInUser(Object loggedInUser) { this.loggedInUser = loggedInUser; }

    // GETTERS
    public Object getLoggedInUser() { return loggedInUser; }

    // OTHER METHODS

    // Registers a user by storing their credentials and person object at the same index
    public void registerUser(String username, String password, Object person) {
        credentials.add(username + ":" + password);
        users.add(person);
    }

    // Finds matching credentials and returns the person object, null if not found
    public Object login(String username, String password) {
        String key = username + ":" + password;

        for (int i = 0; i < credentials.size(); i++) {
            if (credentials.get(i).equals(key)) {
                loggedInUser = users.get(i);
                return loggedInUser;
            }
        }

        System.out.println("Invalid credentials");
        return null;
    }

    // Returns the role string of whoever is currently logged in
    public String getLoggedInRole() {
        if (loggedInUser instanceof Admin)   return "ADMIN";
        if (loggedInUser instanceof Teacher) return "TEACHER";
        if (loggedInUser instanceof Student) return "STUDENT";
        return null;
    }

    // Logs out the current user
    public void logout() { loggedInUser = null; }
}