package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.academicunit.Assignment;
import com.campus.academicunit.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AssignmentPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JComboBox<String> courseBox;
    private JTextField numberField;
    private JTextField titleField;
    private JTextField issueDateField;
    private JTextField dueDateField;
    private JTextField totalMarksField;
    private JTextField obtainedMarksField;
    
    private JButton addButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton refreshButton;
    
    private JTable assignmentTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public AssignmentPanel(DataManager dm) {
        this.dm = dm;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        
        inputPanel.add(new JLabel("Select Course:"));
        courseBox = new JComboBox<>();
        inputPanel.add(courseBox);
        
        inputPanel.add(new JLabel("Assignment Number:"));
        numberField = new JTextField();
        inputPanel.add(numberField);
        
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);
        
        inputPanel.add(new JLabel("Issue Date:"));
        issueDateField = new JTextField();
        inputPanel.add(issueDateField);
        
        inputPanel.add(new JLabel("Due Date:"));
        dueDateField = new JTextField();
        inputPanel.add(dueDateField);
        
        inputPanel.add(new JLabel("Total Marks:"));
        totalMarksField = new JTextField();
        inputPanel.add(totalMarksField);
        
        inputPanel.add(new JLabel("Obtained Marks:"));
        obtainedMarksField = new JTextField("0.0"); // Default
        inputPanel.add(obtainedMarksField);
        
        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table)
        String[] columns = {"Course", "Number", "Title", "Issue Date", "Due Date", "Total Marks", "Obtained Marks"};
        tableModel = new DefaultTableModel(columns, 0);
        assignmentTable = new JTable(tableModel);
        add(new JScrollPane(assignmentTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        addButton = new JButton("Add Assignment");
        removeButton = new JButton("Remove Selected");
        editButton = new JButton("Edit Selected");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh List");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 4. Connect Buttons to Methods
        addButton.addActionListener(e -> addAssignment());
        removeButton.addActionListener(e -> removeAssignment());
        editButton.addActionListener(e -> editAssignment());
        viewButton.addActionListener(e -> viewAssignment());
        refreshButton.addActionListener(e -> {
            refreshComboBox();
            refreshTable();
        });
        
        // Add listener to combobox to filter table when course changes
        courseBox.addActionListener(e -> refreshTable());

        // 5. Initial Load
        refreshComboBox();
        refreshTable();
    }

    // OTHER METHODS
    
    private void addAssignment() {
        String cSelection = (String) courseBox.getSelectedItem();
        if (cSelection == null || cSelection.equals("None")) {
            JOptionPane.showMessageDialog(this, "Please select a course first.");
            return;
        }
        
        try {
            int number = Integer.parseInt(numberField.getText());
            String title = titleField.getText();
            String issueDate = issueDateField.getText();
            String dueDate = dueDateField.getText();
            int totalMarks = Integer.parseInt(totalMarksField.getText());
            double obtainedMarks = Double.parseDouble(obtainedMarksField.getText());
            
            if (title.isEmpty() || issueDate.isEmpty() || dueDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all text fields.");
                return;
            }
            
            Course selectedCourse = getSelectedCourse();
            if (selectedCourse != null) {
                // Check if assignment number already exists in this course
                for (Assignment a : selectedCourse.getAssignments()) {
                    if (a.getAssignmentNumber() == number) {
                        JOptionPane.showMessageDialog(this, "Assignment number " + number + " already exists for this course!");
                        return;
                    }
                }
                
                Assignment newAssignment = new Assignment(number, title, issueDate, dueDate, totalMarks, obtainedMarks);
                selectedCourse.addAssignment(newAssignment);
                
                JOptionPane.showMessageDialog(this, "Assignment Added Successfully!");
                
                // Clear fields except course
                numberField.setText("");
                titleField.setText("");
                issueDateField.setText("");
                dueDateField.setText("");
                totalMarksField.setText("");
                obtainedMarksField.setText("0.0");
                
                refreshTable();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Number, Total Marks, and Obtained Marks must be valid numbers.");
        }
    }

    private void removeAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            Course course = getSelectedCourse();
            if (course == null) return;
            
            int assignmentNum = (int) tableModel.getValueAt(selectedRow, 1);
            
            for (Assignment a : course.getAssignments()) {
                if (a.getAssignmentNumber() == assignmentNum) {
                    course.removeAssignment(a);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please click on an assignment in the table to remove it.");
        }
    }
    
    private void editAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select an assignment first.");
            return;
        }

        Course course = getSelectedCourse();
        if (course == null) return;
        
        int assignmentNum = (int) tableModel.getValueAt(selectedRow, 1);
        Assignment target = null;
        
        for (Assignment a : course.getAssignments()) {
            if (a.getAssignmentNumber() == assignmentNum) {
                target = a;
                break;
            }
        }
        
        if (target == null) return;

        String newTitle = JOptionPane.showInputDialog(this, "Enter new title (leave blank to keep):", target.getTitle());
        if (newTitle != null && !newTitle.isEmpty()) {
            target.setTitle(newTitle);
        }

        String newDueDate = JOptionPane.showInputDialog(this, "Enter new due date (leave blank to keep):", target.getDueDate());
        if (newDueDate != null && !newDueDate.isEmpty()) {
            target.setDueDate(newDueDate);
        }
        
        String marksStr = JOptionPane.showInputDialog(this, "Enter obtained marks (leave blank to keep):", target.getObtainedMarks());
        if (marksStr != null && !marksStr.isEmpty()) {
            try {
                target.setObtainedMarks(Double.parseDouble(marksStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid marks number.");
            }
        }

        JOptionPane.showMessageDialog(this, "Assignment updated!");
        refreshTable();
    }

    private void viewAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            Course course = getSelectedCourse();
            if (course == null) return;
            
            int assignmentNum = (int) tableModel.getValueAt(selectedRow, 1);
            
            for (Assignment a : course.getAssignments()) {
                if (a.getAssignmentNumber() == assignmentNum) {
                    JOptionPane.showMessageDialog(this, a.toString(), "Assignment Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an assignment first.");
        }
    }
    
    // Helper to get the currently selected course from the dropdown
    private Course getSelectedCourse() {
        String cSelection = (String) courseBox.getSelectedItem();
        if (cSelection != null && !cSelection.equals("None")) {
            String courseId = cSelection.substring(cSelection.indexOf("(") + 1, cSelection.indexOf(")"));
            for (Course c : dm.getRepoCourses().getAll()) {
                if (c.getCourseId().equals(courseId)) {
                    return c;
                }
            }
        }
        return null;
    }

    private void refreshComboBox() {
        // Suppress action events during reload
        courseBox.removeActionListener(courseBox.getActionListeners()[0]);
        
        courseBox.removeAllItems();
        courseBox.addItem("None");
        for (Course c : dm.getRepoCourses().getAll()) {
            courseBox.addItem(c.getCourseName() + " (" + c.getCourseId() + ")");
        }
        
        // Re-add listener
        courseBox.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        
        Course selectedCourse = getSelectedCourse();
        if (selectedCourse != null) {
            for (Assignment a : selectedCourse.getAssignments()) {
                tableModel.addRow(new Object[]{
                        selectedCourse.getCourseName(),
                        a.getAssignmentNumber(),
                        a.getTitle(),
                        a.getIssueDate(),
                        a.getDueDate(),
                        a.getTotalMarks(),
                        a.getObtainedMarks()
                });
            }
        } else {
            // Show all assignments if "None" is selected
            for (Course c : dm.getRepoCourses().getAll()) {
                for (Assignment a : c.getAssignments()) {
                    tableModel.addRow(new Object[]{
                            c.getCourseName(),
                            a.getAssignmentNumber(),
                            a.getTitle(),
                            a.getIssueDate(),
                            a.getDueDate(),
                            a.getTotalMarks(),
                            a.getObtainedMarks()
                    });
                }
            }
        }
    }
}
