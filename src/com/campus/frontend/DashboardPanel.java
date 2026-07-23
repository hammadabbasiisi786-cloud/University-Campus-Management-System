package com.campus.frontend;

import com.campus.backend.fileio.DataManager;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JLabel totalStudentsLabel;
    private JLabel totalTeachersLabel;
    private JLabel totalCoursesLabel;
    private JLabel totalDepartmentsLabel;
    private JButton refreshButton;
    private JButton saveButton;

    // CONSTRUCTOR
    public DashboardPanel(DataManager dm) {
        this.dm = dm;

        // Setup simple layout
        setLayout(new GridLayout(7, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create Title
        JLabel title = new JLabel("Welcome to Campus Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);

        // Create Labels
        totalStudentsLabel = new JLabel();
        totalStudentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        totalTeachersLabel = new JLabel();
        totalTeachersLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        totalCoursesLabel = new JLabel();
        totalCoursesLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        totalDepartmentsLabel = new JLabel();
        totalDepartmentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add Labels
        add(totalStudentsLabel);
        add(totalTeachersLabel);
        add(totalCoursesLabel);
        add(totalDepartmentsLabel);

        // Create Refresh Button
        refreshButton = new JButton("Refresh Statistics");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> refreshStats());
        add(refreshButton);

        // Create Save Button
        saveButton = new JButton("Save Data");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> {
            try {
                dm.saveAll();
                JOptionPane.showMessageDialog(this, "Data saved successfully!", "Save",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Save Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);

        // Initial Load
        refreshStats();
    }

    // OTHER METHODS

    // Grabs the size of the lists from DataManager and updates the labels
    public void refreshStats() {
        int students = dm.getRepoStudents().getAll().size();
        int teachers = dm.getRepoTeachers().getAll().size();
        int courses = dm.getRepoCourses().getAll().size();
        int depts = dm.getRepoDepartments().getAll().size();
        int classrooms = dm.getRepoClassrooms().getAll().size();
        int labs = dm.getRepoLabs().getAll().size();

        totalStudentsLabel.setText("Total Registered Students : " + students);
        totalTeachersLabel.setText("Total Registered Teachers : " + teachers);
        totalCoursesLabel.setText("Total Active Courses      : " + courses);
        totalDepartmentsLabel
                .setText("Total Departments: " + depts + "  |  Classrooms: " + classrooms + "  |  Labs: " + labs);
    }
}
