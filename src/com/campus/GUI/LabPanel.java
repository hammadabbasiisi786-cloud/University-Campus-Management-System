package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.Person.Student;
import com.campus.Person.Teacher;
import com.campus.academicunit.Equipment;
import com.campus.academicunit.Lab;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LabPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField nameField;
    private JTextField capacityField;
    private JTextField locationField; // Change 1
    private JButton addButton;
    private JButton removeButton;
    private JButton assignTeacherButton;
    private JButton addStudentButton;
    private JButton addEquipButton;
    private JButton editButton;
    private JButton costButton;
    private JButton viewButton;
    private JButton refreshButton;
    private JTable labTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public LabPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs) — Change 2
        JPanel inputPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        nameField = new JTextField();
        capacityField = new JTextField();
        locationField = new JTextField();

        inputPanel.add(new JLabel("Lab Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capacityField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table) — Change 1 (Updated with Status column)
        String[] cols = { "Lab ID", "Name", "Capacity", "Location", "Status", "Available", "Students", "Equipment",
                "Lab Assistant" };
        tableModel = new DefaultTableModel(cols, 0);
        labTable = new JTable(tableModel);
        add(new JScrollPane(labTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons - two rows)
        JPanel btnPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        addButton = new JButton("Add Lab");
        removeButton = new JButton("Remove");
        assignTeacherButton = new JButton("Assign Teacher");
        addStudentButton = new JButton("Add Student");
        addEquipButton = new JButton("Add Equipment");
        editButton = new JButton("Edit Lab");
        costButton = new JButton("Calc Op. Cost");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh");

        row1.add(addButton);
        row1.add(removeButton);
        row1.add(assignTeacherButton);
        row1.add(addStudentButton);
        row1.add(addEquipButton);
        row2.add(editButton);
        row2.add(costButton);
        row2.add(viewButton);
        row2.add(refreshButton);

        btnPanel.add(row1);
        btnPanel.add(row2);
        add(btnPanel, BorderLayout.SOUTH);

        // 4. Connect Buttons
        addButton.addActionListener(e -> addLab());
        removeButton.addActionListener(e -> removeLab());
        assignTeacherButton.addActionListener(e -> assignTeacher());
        addStudentButton.addActionListener(e -> addStudent());
        addEquipButton.addActionListener(e -> addEquipment());
        editButton.addActionListener(e -> editLab());
        costButton.addActionListener(e -> calcOperationalCost());
        viewButton.addActionListener(e -> viewLab());
        refreshButton.addActionListener(e -> refreshTable());

        // 5. Initial Load
        refreshTable();
    }

    // OTHER METHODS

    private void addLab() {
        try {
            String labName = nameField.getText().trim();
            String location = locationField.getText().trim();
            int capacity = Integer.parseInt(capacityField.getText().trim());

            if (labName.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
                return;
            }

            // Block duplicate lab names
            for (Lab existing : dm.getRepoLabs().getAll()) {
                if (existing.getEntityName().equalsIgnoreCase(labName)) {
                    JOptionPane.showMessageDialog(this,
                            "A lab named \"" + labName + "\" already exists. Please use a unique name.",
                            "Duplicate Lab", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            Lab l = new Lab("LB-" + System.currentTimeMillis(), labName, location,
                    true, capacity, null);
            dm.getRepoLabs().add(l);
            nameField.setText("");
            capacityField.setText("");
            locationField.setText("");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number.");
        }
    }

    private void removeLab() {
        int row = labTable.getSelectedRow();
        if (row >= 0) {
            String labNum = (String) tableModel.getValueAt(row, 0);
            dm.getRepoLabs().getAll().removeIf(l -> l.getLabNumber().equals(labNum));
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
        }
    }

    private void assignTeacher() {
        int row = labTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
            return;
        }

        String labNum = (String) tableModel.getValueAt(row, 0);
        int count = dm.getRepoTeachers().getAll().size();
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No teachers in the system.");
            return;
        }

        String[] options = new String[count];
        int i = 0;
        for (Teacher t : dm.getRepoTeachers().getAll()) {
            options[i++] = t.getName() + " (" + t.getTeacherID() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Select Teacher:",
                "Assign Lab Assistant", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String tid = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Lab l : dm.getRepoLabs().getAll()) {
                if (l.getLabNumber().equals(labNum)) {
                    for (Teacher t : dm.getRepoTeachers().getAll()) {
                        if (t.getTeacherID().equals(tid)) {
                            l.setLabAssistant(t);
                            JOptionPane.showMessageDialog(this, "Teacher assigned!");
                            break;
                        }
                    }
                    break;
                }
            }
            refreshTable();
        }
    }

    private void addStudent() {
        int row = labTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
            return;
        }

        String labNum = (String) tableModel.getValueAt(row, 0);
        Lab targetLab = null;
        for (Lab l : dm.getRepoLabs().getAll()) {
            if (l.getLabNumber().equals(labNum)) {
                targetLab = l;
                break;
            }
        }
        if (targetLab == null)
            return;

        int count = dm.getRepoStudents().getAll().size();
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No students in the system.");
            return;
        }

        String[] options = new String[count];
        int i = 0;
        for (Student s : dm.getRepoStudents().getAll()) {
            options[i++] = s.getName() + " (" + s.getStudentID() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Select Student:",
                "Add Student to Lab", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String sid = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Student s : dm.getRepoStudents().getAll()) {
                if (s.getStudentID().equals(sid)) {
                    targetLab.addStudent(s);
                    JOptionPane.showMessageDialog(this, "Student added to lab!");
                    break;
                }
            }
            refreshTable();
        }
    }

    private void addEquipment() {
        int row = labTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
            return;
        }
        String labNum = (String) tableModel.getValueAt(row, 0);
        Lab target = null;
        for (Lab l : dm.getRepoLabs().getAll()) {
            if (l.getLabNumber().equals(labNum)) {
                target = l;
                break;
            }
        }
        if (target == null)
            return;

        String type = JOptionPane.showInputDialog(this, "Equipment Type (e.g. Computer, Projector):");
        if (type == null || type.isEmpty())
            return;

        String eName = JOptionPane.showInputDialog(this, "Equipment Name:");
        if (eName == null || eName.isEmpty())
            return;

        String costStr = JOptionPane.showInputDialog(this, "Operational Cost per hour:");
        if (costStr == null || costStr.isEmpty())
            return;

        try {
            Equipment eq = new Equipment(type, eName, Integer.parseInt(costStr));
            target.addEquipment(eq);
            JOptionPane.showMessageDialog(this, "Equipment added: " + eName);
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cost value.");
        }
    }

    // Change 3 — Updated editLab() to support editing of Lab Status via dropdown
    // select
    private void editLab() {
        int row = labTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
            return;
        }
        String labNum = (String) tableModel.getValueAt(row, 0);
        Lab target = null;
        for (Lab l : dm.getRepoLabs().getAll()) {
            if (l.getLabNumber().equals(labNum)) {
                target = l;
                break;
            }
        }
        if (target == null)
            return;

        String newName = JOptionPane.showInputDialog(this, "New name (blank to keep):", target.getEntityName());
        if (newName != null && !newName.isEmpty()) {
            target.setEntityName(newName);
        }

        String newLocation = JOptionPane.showInputDialog(this, "New location (blank to keep):", target.getLocation());
        if (newLocation != null && !newLocation.isEmpty()) {
            target.setLocation(newLocation);
        }

        String capStr = JOptionPane.showInputDialog(this, "New capacity (blank to keep):", target.getCapacity());
        if (capStr != null && !capStr.isEmpty()) {
            try {
                target.setCapacity(Integer.parseInt(capStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        }

        String[] statusOptions = { "Active", "Inactive", "Under Maintenance", "Closed" };
        String newStatus = (String) JOptionPane.showInputDialog(this,
                "Select status:",
                "Edit Status",
                JOptionPane.QUESTION_MESSAGE,
                null, statusOptions, target.getStatus());
        if (newStatus != null) {
            target.setStatus(newStatus);
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Toggle availability? Current: " + (target.isAvailable() ? "Available" : "Unavailable"),
                "Toggle", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            target.setAvailability(!target.isAvailable());
        }

        JOptionPane.showMessageDialog(this, "Lab updated!");
        refreshTable();
    }

    private void calcOperationalCost() {
        int row = labTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
            return;
        }
        String labNum = (String) tableModel.getValueAt(row, 0);
        for (Lab l : dm.getRepoLabs().getAll()) {
            if (l.getLabNumber().equals(labNum)) {
                double cost = l.calculateOperationalCost();
                JOptionPane.showMessageDialog(this,
                        "Operational Cost for " + l.getEntityName() + ": $" + String.format("%.2f", cost),
                        "Operational Cost", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void viewLab() {
        int row = labTable.getSelectedRow();
        if (row >= 0) {
            String labNum = (String) tableModel.getValueAt(row, 0);
            for (Lab l : dm.getRepoLabs().getAll()) {
                if (l.getLabNumber().equals(labNum)) {
                    JOptionPane.showMessageDialog(this, l.toString(), "Lab Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a lab first.");
        }
    }

    // Change 2 — Table row population replaced to reflect Status column mappings
    // safely
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Lab l : dm.getRepoLabs().getAll()) {
            tableModel.addRow(new Object[] {
                    l.getLabNumber(),
                    l.getEntityName(),
                    l.getCapacity(),
                    l.getLocation() != null ? l.getLocation() : "N/A",
                    l.getStatus(),
                    l.isAvailable() ? "Yes" : "No",
                    l.getStudents().size(),
                    l.getEquipments().size(),
                    (l.getTeacher() != null ? l.getTeacher().getName() : "None")
            });
        }
    }
}