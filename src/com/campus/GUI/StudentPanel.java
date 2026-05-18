package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.Person.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField nameField;
    private JComboBox<Integer> semesterBox;
    private JButton addButton;
    private JButton removeButton;
    private JButton searchButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton refreshButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public StudentPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Semester (1-8):"));
        Integer[] semesters = {1, 2, 3, 4, 5, 6, 7, 8};
        semesterBox = new JComboBox<>(semesters);
        inputPanel.add(semesterBox);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table)
        String[] columns = {"Student ID", "Name", "Semester", "Enrolled Courses"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add Student");
        removeButton = new JButton("Remove Selected");
        searchButton = new JButton("Search by ID");
        editButton = new JButton("Edit Selected");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh List");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Role-based UI Restrictions
        String role = dm.getLoginManager().getLoggedInRole();
        if ("TEACHER".equals(role) || "STUDENT".equals(role)) {
            inputPanel.setVisible(false);
            addButton.setVisible(false);
            removeButton.setVisible(false);
            editButton.setVisible(false);
            
            if ("STUDENT".equals(role)) {
                searchButton.setVisible(false);
            }
        }

        // 4. Connect Buttons to Methods
        addButton.addActionListener(e -> addStudent());
        removeButton.addActionListener(e -> removeStudent());
        searchButton.addActionListener(e -> searchStudent());
        editButton.addActionListener(e -> editStudent());
        viewButton.addActionListener(e -> viewStudent());
        refreshButton.addActionListener(e -> refreshTable());

        // 5. Initial Load
        refreshTable();
    }

    // OTHER METHODS

    // Creates a Student, saves to repo, and automatically registers their login
    private void addStudent() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name.");
            return;
        }

        int sem = (int) semesterBox.getSelectedItem();

        Student newStudent = new Student(name, sem);
        dm.getRepoStudents().add(newStudent);

        // Auto-Register in LoginManager: Username = name, Password = studentID
        dm.getLoginManager().registerUser(name, newStudent.getStudentID(), newStudent);

        JOptionPane.showMessageDialog(this,
                "Student Added!\nLogin -> Username: " + name + "\nPassword: " + newStudent.getStudentID());

        nameField.setText("");
        refreshTable();
    }

    // Removes the student selected in the table
    private void removeStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentId = (String) tableModel.getValueAt(selectedRow, 0);

            for (Student s : dm.getRepoStudents().getAll()) {
                if (s.getStudentID().equals(studentId)) {
                    dm.getRepoStudents().remove(s);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please click on a student in the table to remove them.");
        }
    }

    // Asks for an ID and highlights that row if found
    private void searchStudent() {
        String searchId = JOptionPane.showInputDialog(this, "Enter exact Student ID:");

        if (searchId != null && !searchId.isEmpty()) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(searchId)) {
                    studentTable.setRowSelectionInterval(i, i);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found.");
        }
    }

    // Edits the name and semester of the selected student using setters
    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a student first.");
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student target = null;
        for (Student s : dm.getRepoStudents().getAll()) {
            if (s.getStudentID().equals(studentId)) {
                target = s;
                break;
            }
        }
        if (target == null) return;

        String newName = JOptionPane.showInputDialog(this, "Enter new name (leave blank to keep):", target.getName());
        if (newName != null && !newName.isEmpty()) {
            target.setName(newName);
        }

        String semStr = JOptionPane.showInputDialog(this, "Enter new semester (1-8, leave blank to keep):", target.getSemester());
        if (semStr != null && !semStr.isEmpty()) {
            try {
                target.setSemester(Integer.parseInt(semStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid semester number.");
            }
        }

        JOptionPane.showMessageDialog(this, "Student updated!");
        refreshTable();
    }

    // Shows the full toString() of the selected student in a popup
    private void viewStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentId = (String) tableModel.getValueAt(selectedRow, 0);
            for (Student s : dm.getRepoStudents().getAll()) {
                if (s.getStudentID().equals(studentId)) {
                    JOptionPane.showMessageDialog(this, s.toString(), "Student Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a student first.");
        }
    }

    // Clears the table and reloads all students from the DataManager
    private void refreshTable() {
        tableModel.setRowCount(0);

        String role = dm.getLoginManager().getLoggedInRole();
        Student loggedInStudent = null;
        if ("STUDENT".equals(role)) {
            loggedInStudent = (Student) dm.getLoginManager().getLoggedInUser();
        }

        for (Student s : dm.getRepoStudents().getAll()) {
            if (loggedInStudent != null && !s.getStudentID().equals(loggedInStudent.getStudentID())) {
                continue; // Skip other students
            }

            tableModel.addRow(new Object[]{
                    s.getStudentID(),
                    s.getName(),
                    s.getSemester(),
                    s.getEnrolledCourses().size()
            });
        }
    }
}
