package com.campus.frontend;

import com.campus.backend.fileio.DataManager;
import com.campus.backend.person.Teacher;
import com.campus.backend.academicunit.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TeacherPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField nameField;
    private JTextField salaryField;
    private JTextField qualificationField;
    private JButton addButton;
    private JButton removeButton;
    private JButton searchButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton refreshButton;
    private JButton myCoursesButton;
    private JTable teacherTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public TeacherPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        inputPanel.add(new JLabel("Teacher Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        inputPanel.add(salaryField);

        inputPanel.add(new JLabel("Qualification:"));
        qualificationField = new JTextField();
        inputPanel.add(qualificationField);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table)
        String[] columns = { "Teacher ID", "Name", "Salary", "Qualification" };
        tableModel = new DefaultTableModel(columns, 0);
        teacherTable = new JTable(tableModel);
        add(new JScrollPane(teacherTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add Teacher");
        removeButton = new JButton("Remove Selected");
        searchButton = new JButton("Search by ID");
        editButton = new JButton("Edit Selected");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh List");
        myCoursesButton = new JButton("My Courses");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(myCoursesButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 4. Connect Buttons to Methods
        addButton.addActionListener(e -> addTeacher());
        removeButton.addActionListener(e -> removeTeacher());
        searchButton.addActionListener(e -> searchTeacher());
        editButton.addActionListener(e -> editTeacher());
        viewButton.addActionListener(e -> viewTeacher());
        refreshButton.addActionListener(e -> refreshTable());
        myCoursesButton.addActionListener(e -> viewMyCourses());

        // 5. Initial Load
        refreshTable();
    }

    // OTHER METHODS

    // Creates a Teacher, saves to repo, and automatically registers their login
    private void addTeacher() {
        String name = nameField.getText();
        String salaryStr = salaryField.getText();
        String qual = qualificationField.getText();

        if (name.isEmpty() || salaryStr.isEmpty() || qual.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.");
            return;
        }

        Teacher newTeacher = new Teacher(name, salary, qual);
        dm.getRepoTeachers().add(newTeacher);

        // Auto-Register in LoginManager: Username = name, Password = teacherID
        dm.getLoginManager().registerUser(name, newTeacher.getTeacherID(), newTeacher);

        JOptionPane.showMessageDialog(this,
                "Teacher Added!\nLogin -> Username: " + name + "\nPassword: " + newTeacher.getTeacherID());

        nameField.setText("");
        salaryField.setText("");
        qualificationField.setText("");

        refreshTable();
    }

    // Removes the teacher selected in the table
    private void removeTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow >= 0) {
            String teacherId = (String) tableModel.getValueAt(selectedRow, 0);

            for (Teacher t : dm.getRepoTeachers().getAll()) {
                if (t.getTeacherID().equals(teacherId)) {
                    dm.getRepoTeachers().remove(t);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please click on a teacher in the table to remove them.");
        }
    }

    // Asks for an ID and highlights that row if found
    private void searchTeacher() {
        String searchId = JOptionPane.showInputDialog(this, "Enter exact Teacher ID:");

        if (searchId != null && !searchId.isEmpty()) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(searchId)) {
                    teacherTable.setRowSelectionInterval(i, i);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Teacher not found.");
        }
    }

    // Edits the salary and qualification of the selected teacher using setters
    private void editTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a teacher first.");
            return;
        }

        String teacherId = (String) tableModel.getValueAt(selectedRow, 0);
        Teacher target = null;
        for (Teacher t : dm.getRepoTeachers().getAll()) {
            if (t.getTeacherID().equals(teacherId)) {
                target = t;
                break;
            }
        }
        if (target == null)
            return;

        String newName = JOptionPane.showInputDialog(this, "Enter new name (leave blank to keep):", target.getName());
        if (newName != null && !newName.isEmpty()) {
            target.setName(newName);
        }

        String salaryStr = JOptionPane.showInputDialog(this, "Enter new salary (leave blank to keep):",
                target.getSalary());
        if (salaryStr != null && !salaryStr.isEmpty()) {
            try {
                target.setSalary(Double.parseDouble(salaryStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary number.");
            }
        }

        String newQual = JOptionPane.showInputDialog(this, "Enter new qualification (leave blank to keep):",
                target.getQualification());
        if (newQual != null && !newQual.isEmpty()) {
            target.setQualification(newQual);
        }

        JOptionPane.showMessageDialog(this, "Teacher updated!");
        refreshTable();
    }

    // Shows the full toString() of the selected teacher in a popup
    private void viewTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow >= 0) {
            String teacherId = (String) tableModel.getValueAt(selectedRow, 0);
            for (Teacher t : dm.getRepoTeachers().getAll()) {
                if (t.getTeacherID().equals(teacherId)) {
                    JOptionPane.showMessageDialog(this, t.toString(), "Teacher Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a teacher first.");
        }
    }

    // Clears the table and reloads all teachers from the DataManager
    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Teacher t : dm.getRepoTeachers().getAll()) {
            tableModel.addRow(new Object[] {
                    t.getTeacherID(),
                    t.getName(),
                    t.getSalary(),
                    t.getQualification()
            });
        }
    }

    // Shows all courses taught by the selected teacher in a scrollable popup dialog
    private void viewMyCourses() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a teacher first.");
            return;
        }

        String teacherId = (String) tableModel.getValueAt(selectedRow, 0);
        Teacher target = null;
        for (Teacher t : dm.getRepoTeachers().getAll()) {
            if (t.getTeacherID().equals(teacherId)) {
                target = t;
                break;
            }
        }
        if (target == null)
            return;

        // Scan all courses and collect those assigned to this teacher
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getTeacher() != null && c.getTeacher().equals(target)) {
                sb.append(String.format(
                        "• %s  [%s]\n  Day: %s  |  Time: %s  |  Students: %d/%d\n\n",
                        c.getCourseName(),
                        c.getCourseId(),
                        c.getDay(),
                        c.getTime(),
                        c.getStudents().size(),
                        c.getMaxCapacity()));
                count++;
            }
        }

        if (count == 0) {
            JOptionPane.showMessageDialog(this,
                    target.getName() + " is not assigned to any courses yet.",
                    "My Courses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new java.awt.Dimension(450, 300));
            JOptionPane.showMessageDialog(this, scroll,
                    target.getName() + "'s Courses (" + count + ")",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
