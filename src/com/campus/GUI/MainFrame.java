package com.campus.GUI;

import com.campus.FileIO.DataManager;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // FIELDS
    private DataManager dm;
    private String role;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // CONSTRUCTOR
    public MainFrame(DataManager dm, String role) {
        this.dm = dm;
        this.role = role;

        // Setup frame
        setTitle("Smart University Campus Management - Role: " + role);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Save data when the window is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dm.saveAll();
                System.exit(0);
            }
        });

        // Initialize Layout Panels
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(150, 0));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Setup the panels and buttons
        setupPanelsAndButtons();

        // Add to main frame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // OTHER METHODS

    private void setupPanelsAndButtons() {
        // Create actual panels
        DashboardPanel dashboard = new DashboardPanel(dm);
        StudentPanel studentPanel = new StudentPanel(dm);
        TeacherPanel teacherPanel = new TeacherPanel(dm);
        CoursePanel coursePanel = new CoursePanel(dm);
        DepartmentPanel deptPanel = new DepartmentPanel(dm);
        FacilityPanel facilityPanel = new FacilityPanel(dm);
        ServicePanel servicePanel = new ServicePanel(dm);
        ClassroomPanel classroomPanel = new ClassroomPanel(dm);
        LabPanel labPanel = new LabPanel(dm);
        CampusMapPanel mapPanel = new CampusMapPanel(dm);
        // AssignmentPanel assignmentPanel = new AssignmentPanel(dm);
        BookPanel bookPanel = new BookPanel(dm);
        AdminPanel adminPanel = new AdminPanel(dm);
        CampusZonePanel zonePanel = new CampusZonePanel(dm);

        // Add panels to CardLayout with string identifiers
        contentPanel.add(dashboard, "Dashboard");
        contentPanel.add(studentPanel, "Students");
        contentPanel.add(teacherPanel, "Teachers");
        contentPanel.add(coursePanel, "Courses");
        contentPanel.add(deptPanel, "Departments");
        contentPanel.add(facilityPanel, "Facilities");
        contentPanel.add(servicePanel, "Services");
        contentPanel.add(classroomPanel, "Classrooms");
        contentPanel.add(labPanel, "Labs");
        contentPanel.add(mapPanel, "Map");
        // contentPanel.add(assignmentPanel, "Assignments");
        contentPanel.add(bookPanel, "Books");
        contentPanel.add(adminPanel, "Admin");
        contentPanel.add(zonePanel, "Zones");

        // Simple Role-Based Access logic
        if (role.equals("TEACHER") || role.equals("STUDENT")) {
            addButton("Courses");
            addButton("Students");
            cardLayout.show(contentPanel, "Courses"); // Set default view
        } else {
            // ADMIN
            addButton("Admin");
            addButton("Dashboard");
            addButton("Students");
            addButton("Teachers");
            addButton("Courses");
            // addButton("Assignments"); // Removed as per user request
            addButton("Departments");
            addButton("Books");
            addButton("Zones");
            // addButton("Menus"); // Removed as per user request
            addButton("Facilities");
            addButton("Services");
            addButton("Classrooms");
            addButton("Labs");
            addButton("Map");
        }

        // Add a Logout Button at the bottom
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(150, 40));
        logoutBtn.addActionListener(e -> {
            dm.getLoginManager().logout();
            new LoginFrame(dm);
            this.dispose(); // close current window
        });
        sidebarPanel.add(Box.createVerticalGlue()); // Pushes logout to the bottom
        sidebarPanel.add(logoutBtn);
    }

    // Helper method to create a button and add its switching action
    private void addButton(String panelName) {
        JButton btn = new JButton(panelName);
        btn.setMaximumSize(new Dimension(150, 40));
        btn.addActionListener(e -> cardLayout.show(contentPanel, panelName));
        sidebarPanel.add(btn);
    }
}
