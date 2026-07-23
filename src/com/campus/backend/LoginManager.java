package com.campus.backend;

import com.campus.backend.person.Admin;
import com.campus.backend.person.Student;
import com.campus.backend.person.Teacher;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginManager implements Serializable {

    // FIELDS
    private ArrayList<String> credentials = new ArrayList<>();  // stores "username:password"
    private ArrayList<Object> users = new ArrayList<>();        // stores the person object
    private Object loggedInUser = null;

    // CONSTRUCTORS
    public LoginManager() {
    }

    // GETTERS
    public Object getLoggedInUser() {
        return loggedInUser;
    }

    // SETTERS
    public void setLoggedInUser(Object loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // OTHER METHODS

    public void registerUser(String username, String password, Object person) {
        credentials.add(username + ":" + password);
        users.add(person);
    }

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

    public String getLoggedInRole() {
        if (loggedInUser instanceof Admin) return "ADMIN";
        if (loggedInUser instanceof Teacher) return "TEACHER";
        if (loggedInUser instanceof Student) return "STUDENT";
        return null;
    }

    public void logout() {
        loggedInUser = null;
    }
}
