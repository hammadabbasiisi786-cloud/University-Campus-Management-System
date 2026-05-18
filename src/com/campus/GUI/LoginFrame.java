package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.Person.Admin;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    // FIELDS
    private DataManager dm;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // CONSTRUCTOR
    public LoginFrame(DataManager dm) {
        this.dm = dm;

        // Setup frame
        setTitle("Campus Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new GridLayout(3, 2, 10, 10)); // Simple grid

        // Create and add components
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);

        registerButton = new JButton("Register Admin");
        add(registerButton);

        // Add action listeners
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        // Make visible
        setVisible(true);
    }

    // OTHER METHODS

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Object user = dm.getLoginManager().login(username, password);

        if (user != null) {
            String role = dm.getLoginManager().getLoggedInRole();
            JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role);
            new MainFrame(dm, role);
            this.dispose(); // Close login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password to register.");
            return;
        }

        // Simplest approach: Register as Admin so you can access the full system
        // initially
        Admin newAdmin = new Admin(username);
        dm.getLoginManager().registerUser(username, password, newAdmin);
        dm.saveAll(); // Save the new user to file

        JOptionPane.showMessageDialog(this, "Admin registered successfully! You can now login.");
    }
}
