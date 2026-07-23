package com.campus.frontend;

import com.campus.backend.fileio.DataManager;
import com.campus.backend.academicunit.Classroom;
import com.campus.backend.academicunit.Equipment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClassroomPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField nameField;
    private JTextField locationField; // Change 4
    private JTextField capacityField;
    private JButton addButton;
    private JButton editButton; // Change 5a
    private JButton removeButton;
    private JButton addEquipButton;
    private JButton costButton;
    private JButton viewButton;
    private JButton refreshButton;
    private JTable classroomTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public ClassroomPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs) — Change 1
        JPanel inputPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        nameField = new JTextField();
        locationField = new JTextField();
        capacityField = new JTextField();

        inputPanel.add(new JLabel("Classroom Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capacityField);
        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table) — Change 2
        String[] cols = { "Class ID", "Name", "Location", "Capacity", "Available", "Booked Slots", "Equipment",
                "Department", "Status" };
        tableModel = new DefaultTableModel(cols, 0);
        classroomTable = new JTable(tableModel);
        add(new JScrollPane(classroomTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel btnWrapper = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        addButton = new JButton("Add Classroom");
        editButton = new JButton("Edit"); // Change 5b
        refreshButton = new JButton("Refresh");
        removeButton = new JButton("Remove");
        addEquipButton = new JButton("Add Equipment");
        costButton = new JButton("Calc Op. Cost");
        viewButton = new JButton("View Info");

        row1.add(addButton);
        row1.add(editButton); // Change 5b
        row1.add(refreshButton);
        row2.add(removeButton);
        row2.add(addEquipButton);
        row2.add(costButton);
        row2.add(viewButton);

        btnWrapper.add(row1);
        btnWrapper.add(row2);
        add(btnWrapper, BorderLayout.SOUTH);

        // 4. Connect Buttons
        addButton.addActionListener(e -> addClassroom());
        editButton.addActionListener(e -> editClassroom()); // Change 5b
        refreshButton.addActionListener(e -> refreshTable());
        removeButton.addActionListener(e -> removeClassroom());
        addEquipButton.addActionListener(e -> addEquipment());
        costButton.addActionListener(e -> calcOperationalCost());
        viewButton.addActionListener(e -> viewClassroom());

        // 5. Initial Load
        refreshTable();
    }

    // OTHER METHODS

    // Change 4 — Updated method to remove idField reference and use auto-generated
    // ID
    private void addClassroom() {
        String name = nameField.getText();
        String location = locationField.getText();
        String cap = capacityField.getText();

        if (name.isEmpty() || location.isEmpty() || cap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Classroom c = new Classroom("CR-" + System.currentTimeMillis(), name, location,
                    Integer.parseInt(cap), true);
            dm.getRepoClassrooms().add(c);
            nameField.setText("");
            locationField.setText("");
            capacityField.setText("");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Classroom added!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number.");
        }
    }

    // Change 5c — editClassroom() stays mapped to column 0
    private void editClassroom() {
        int row = classroomTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a classroom first.");
            return;
        }
        String classNum = (String) tableModel.getValueAt(row, 0);
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            if (c.getClassNumber().equals(classNum)) {
                String name = JOptionPane.showInputDialog(this, "Name (blank to keep):", c.getEntityName());
                if (name != null && !name.isEmpty())
                    c.setEntityName(name);
                String location = JOptionPane.showInputDialog(this, "Location (blank to keep):", c.getLocation());
                if (location != null && !location.isEmpty())
                    c.setLocation(location);
                String cap = JOptionPane.showInputDialog(this, "Capacity (blank to keep):", c.getCapacity());
                if (cap != null && !cap.isEmpty()) {
                    try {
                        c.setCapacity(Integer.parseInt(cap));
                    } catch (NumberFormatException ignored) {
                    }
                }
                String[] statusOptions = { "Active", "Busy", "Closed" };
                String status = (String) JOptionPane.showInputDialog(this, "Set Status:",
                        "Status", JOptionPane.QUESTION_MESSAGE, null,
                        statusOptions, c.getStatus());
                if (status != null)
                    c.setStatus(status);
                JOptionPane.showMessageDialog(this, "Classroom updated!");
                refreshTable();
                break;
            }
        }
    }

    private void removeClassroom() {
        int row = classroomTable.getSelectedRow();
        if (row >= 0) {
            String classNum = (String) tableModel.getValueAt(row, 0);
            dm.getRepoClassrooms().getAll().removeIf(c -> c.getClassNumber().equals(classNum));
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Select a classroom first.");
        }
    }

    private void addEquipment() {
        int row = classroomTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a classroom first.");
            return;
        }
        String classNum = (String) tableModel.getValueAt(row, 0);
        Classroom target = null;
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            if (c.getClassNumber().equals(classNum)) {
                target = c;
                break;
            }
        }
        if (target == null)
            return;

        String type = JOptionPane.showInputDialog(this, "Equipment Type (e.g. Projector, Computer, Fan):");
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

    private void calcOperationalCost() {
        int row = classroomTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a classroom first.");
            return;
        }
        String classNum = (String) tableModel.getValueAt(row, 0);
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            if (c.getClassNumber().equals(classNum)) {
                double cost = c.calculateOperationalCost();
                JOptionPane.showMessageDialog(this,
                        "Operational Cost for " + c.getEntityName() + ": $" + String.format("%.2f", cost),
                        "Operational Cost", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void viewClassroom() {
        int row = classroomTable.getSelectedRow();
        if (row >= 0) {
            String classNum = (String) tableModel.getValueAt(row, 0);
            for (Classroom c : dm.getRepoClassrooms().getAll()) {
                if (c.getClassNumber().equals(classNum)) {
                    JOptionPane.showMessageDialog(this, c.toString(), "Classroom Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a classroom first.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            // Classroom is "Unavailable" only when all 3 time slots are occupied
            boolean t1 = false, t2 = false, t3 = false;
            for (String slot : c.getOccupiedSlots()) {
                if (slot.contains("9-12"))
                    t1 = true;
                if (slot.contains("12-2"))
                    t2 = true;
                if (slot.contains("2-4"))
                    t3 = true;
            }
            String availStr = (t1 && t2 && t3) ? "No" : "Yes";

            // Change 3 — tableModel.addRow block fully replaced
            tableModel.addRow(new Object[] {
                    c.getClassNumber(),
                    c.getEntityName(),
                    c.getLocation(),
                    c.getCapacity(),
                    availStr,
                    c.getOccupiedSlots().size(),
                    c.getEquipments().size(),
                    (c.getDepartment() != null ? c.getDepartment().getDepartmentName() : "Unassigned"),
                    c.getStatus()
            });
        }
    }
}
