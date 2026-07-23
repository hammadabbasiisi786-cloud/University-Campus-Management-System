package com.campus.frontend;

import com.campus.backend.fileio.DataManager;
import com.campus.backend.academicunit.Classroom;
import com.campus.backend.academicunit.Course;
import com.campus.backend.academicunit.Department;
import com.campus.backend.academicunit.Lab;
import com.campus.backend.academicunit.Equipment;
import com.campus.backend.person.Student;
import com.campus.backend.person.Teacher;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DepartmentPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField deptNameField;
    private JTextField locationField;
    private JComboBox<String> hodBox;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton addTeacherButton;
    private JButton addCourseButton;
    private JButton addClassroomButton;
    private JButton addLabButton;
    private JButton reportButton;
    private JButton viewButton;
    private JButton refreshButton;
    private JButton addEquipmentButton;
    private JButton markUnavailableButton;
    private JTable deptTable;
    private DefaultTableModel tableModel;
    private JTextArea reportArea;

    // CONSTRUCTOR
    public DepartmentPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        inputPanel.add(new JLabel("Department Name:"));
        deptNameField = new JTextField();
        inputPanel.add(deptNameField);

        inputPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        inputPanel.add(locationField);

        inputPanel.add(new JLabel("Head of Department:"));
        hodBox = new JComboBox<>();
        inputPanel.add(hodBox);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table & Report Area)
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        String[] columns = { "Name", "ID", "Location", "Status", "HOD", "Teachers", "Courses", "Classrooms", "Labs",
                "Students", "Equipment" };
        tableModel = new DefaultTableModel(columns, 0);
        deptTable = new JTable(tableModel);
        centerPanel.add(new JScrollPane(deptTable));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setText("Select a department and click 'Generate Report' to view details here.");
        centerPanel.add(new JScrollPane(reportArea));

        add(centerPanel, BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons - two rows)
        JPanel buttonWrapper = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        addButton = new JButton("Add Dept");
        editButton = new JButton("Edit Dept");
        removeButton = new JButton("Remove Dept");
        addTeacherButton = new JButton("Add Teacher");
        addCourseButton = new JButton("Add Course");
        addClassroomButton = new JButton("Add Classroom");
        addLabButton = new JButton("Add Lab");
        reportButton = new JButton("Generate Report");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh");

        row1.add(addButton);
        row1.add(editButton);
        row1.add(removeButton);
        row1.add(addTeacherButton);
        row1.add(addCourseButton);
        row1.add(addClassroomButton);

        addEquipmentButton = new JButton("Add Equipment");
        row2.add(addLabButton);
        row2.add(addEquipmentButton);
        row2.add(reportButton);
        row2.add(viewButton);
        row2.add(refreshButton);

        markUnavailableButton = new JButton("Mark Classroom Unavailable");
        row2.add(markUnavailableButton);

        buttonWrapper.add(row1);
        buttonWrapper.add(row2);
        add(buttonWrapper, BorderLayout.SOUTH);

        // 4. Connect Buttons
        addButton.addActionListener(e -> addDepartment());
        editButton.addActionListener(e -> editDepartment());
        removeButton.addActionListener(e -> removeDepartment());
        addTeacherButton.addActionListener(e -> addTeacherToDept());
        addCourseButton.addActionListener(e -> addCourseToDept());
        addClassroomButton.addActionListener(e -> addClassroomToDept());
        addLabButton.addActionListener(e -> addLabToDept());
        addEquipmentButton.addActionListener(e -> addEquipmentToDept());
        reportButton.addActionListener(e -> generateReport());
        viewButton.addActionListener(e -> viewDepartment());
        refreshButton.addActionListener(e -> {
            refreshComboBox();
            refreshTable();
        });
        markUnavailableButton.addActionListener(e -> markClassroomUnavailable());

        // 5. Initial Load
        refreshComboBox();
        refreshTable();
    }

    // OTHER METHODS

    private void addDepartment() {
        String name = deptNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a department name.");
            return;
        }

        // Find HOD
        Teacher selectedHod = null;
        String selection = (String) hodBox.getSelectedItem();
        if (selection != null && !selection.equals("None")) {
            String tID = selection.substring(selection.indexOf("(") + 1, selection.indexOf(")"));
            for (Teacher t : dm.getRepoTeachers().getAll()) {
                if (t.getTeacherID().equals(tID)) {
                    selectedHod = t;
                    break;
                }
            }
        }

        String location = locationField.getText().trim();
        String autoId = "DEPT-" + (dm.getRepoDepartments().getAll().size() + 1);
        Department newDept = new Department(autoId, name, location.isEmpty() ? "Main Campus" : location, selectedHod);
        dm.getRepoDepartments().add(newDept);

        JOptionPane.showMessageDialog(this, "Department Added Successfully!");
        deptNameField.setText("");
        locationField.setText("");
        refreshTable();
    }

    private void removeDepartment() {
        int selectedRow = deptTable.getSelectedRow();
        if (selectedRow >= 0) {
            String deptName = (String) tableModel.getValueAt(selectedRow, 0);
            for (Department d : dm.getRepoDepartments().getAll()) {
                if (d.getDepartmentName().equals(deptName)) {
                    dm.getRepoDepartments().remove(d);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Select a department from the table to remove.");
        }
    }

    private Department getSelectedDepartment() {
        int selectedRow = deptTable.getSelectedRow();
        if (selectedRow < 0)
            return null;

        String deptName = (String) tableModel.getValueAt(selectedRow, 0);
        for (Department d : dm.getRepoDepartments().getAll()) {
            if (d.getDepartmentName().equals(deptName))
                return d;
        }
        return null;
    }

    private void editDepartment() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String newName = JOptionPane.showInputDialog(this, "Department name (blank to keep):",
                dept.getDepartmentName());
        if (newName != null && !newName.isEmpty()) {
            dept.setDepartmentName(newName);
        }
        String newLocation = JOptionPane.showInputDialog(this, "Location (blank to keep):", dept.getLocation());
        if (newLocation != null && !newLocation.isEmpty()) {
            dept.setLocation(newLocation);
        }
        String[] statusOptions = { "Active", "Busy", "Closed" };
        String newStatus = (String) JOptionPane.showInputDialog(this,
                "Select status:", "Edit Status",
                JOptionPane.QUESTION_MESSAGE, null,
                statusOptions, dept.getStatus());
        if (newStatus != null) {
            dept.setStatus(newStatus);
        }
        // Change HOD
        if (!dm.getRepoTeachers().getAll().isEmpty()) {
            String keepLabel = "Keep current: " +
                    (dept.getHeadOfDepartment() != null ? dept.getHeadOfDepartment().getName() : "None");
            String[] hodOptions = new String[dm.getRepoTeachers().getAll().size() + 1];
            hodOptions[0] = keepLabel;
            int i = 1;
            for (Teacher t : dm.getRepoTeachers().getAll()) {
                hodOptions[i++] = t.getName() + " (" + t.getTeacherID() + ")";
            }
            String hodChoice = (String) JOptionPane.showInputDialog(this,
                    "Change Head of Department:", "Edit HOD",
                    JOptionPane.QUESTION_MESSAGE, null, hodOptions, hodOptions[0]);
            if (hodChoice != null && !hodChoice.equals(keepLabel)) {
                String tid = hodChoice.substring(hodChoice.indexOf("(") + 1, hodChoice.indexOf(")"));
                for (Teacher t : dm.getRepoTeachers().getAll()) {
                    if (t.getTeacherID().equals(tid)) {
                        dept.setHeadOfDepartment(t);
                        break;
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Department updated!");
        refreshTable();
    }

    private void addTeacherToDept() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }

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
                "Add Teacher to " + dept.getDepartmentName(), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String id = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Teacher t : dm.getRepoTeachers().getAll()) {
                if (t.getTeacherID().equals(id)) {
                    dept.addTeacher(t);
                    JOptionPane.showMessageDialog(this, "Teacher added to Department!");
                    refreshTable();
                    break;
                }
            }
        }
    }

    private void addCourseToDept() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }

        int count = dm.getRepoCourses().getAll().size();
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No courses in the system.");
            return;
        }

        String[] options = new String[count];
        int i = 0;
        for (Course c : dm.getRepoCourses().getAll()) {
            options[i++] = c.getCourseName() + " (" + c.getCourseId() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Select Course:",
                "Add Course to " + dept.getDepartmentName(), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String id = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Course c : dm.getRepoCourses().getAll()) {
                if (c.getCourseId().equals(id)) {
                    dept.addCourse(c);
                    JOptionPane.showMessageDialog(this, "Course added to Department!");
                    refreshTable();
                    break;
                }
            }
        }
    }

    private void addClassroomToDept() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }

        int count = dm.getRepoClassrooms().getAll().size();
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No classrooms in the system.");
            return;
        }

        String[] options = new String[count];
        int i = 0;
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            options[i++] = c.getEntityName() + " (" + c.getClassNumber() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Select Classroom:",
                "Add Classroom to " + dept.getDepartmentName(), JOptionPane.QUESTION_MESSAGE, null, options,
                options[0]);

        if (choice != null) {
            String classNum = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Classroom c : dm.getRepoClassrooms().getAll()) {
                if (c.getClassNumber().equals(classNum)) {
                    // Duplicate check
                    for (Classroom existing : dept.getDepartmentalClassrooms()) {
                        if (existing.getClassNumber().equals(classNum)) {
                            JOptionPane.showMessageDialog(this,
                                    "Classroom " + classNum + " is already in this department.");
                            return;
                        }
                    }
                    dept.addClassroom(c);
                    JOptionPane.showMessageDialog(this, "Classroom added to Department!");
                    refreshTable();
                    break;
                }
            }
        }
    }

    private void addLabToDept() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }

        int count = dm.getRepoLabs().getAll().size();
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No labs in the system.");
            return;
        }

        String[] options = new String[count];
        int i = 0;
        for (Lab l : dm.getRepoLabs().getAll()) {
            options[i++] = l.getEntityName() + " (" + l.getLabNumber() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Select Lab:",
                "Add Lab to " + dept.getDepartmentName(), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String labNum = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Lab l : dm.getRepoLabs().getAll()) {
                if (l.getLabNumber().equals(labNum)) {
                    // Duplicate check
                    for (Lab existing : dept.getDepartmentalLabs()) {
                        if (existing.getLabNumber().equals(labNum)) {
                            JOptionPane.showMessageDialog(this, "Lab " + labNum + " is already in this department.");
                            return;
                        }
                    }
                    dept.addLab(l);
                    JOptionPane.showMessageDialog(this, "Lab added to Department!");
                    refreshTable();
                    break;
                }
            }
        }
    }

    private void viewDepartment() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        JOptionPane.showMessageDialog(this, dept.toString(), "Department Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateReport() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department to view its report.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("            DEPARTMENTAL ACADEMIC REPORT          \n");
        sb.append("==================================================\n");
        sb.append("Department Name : ").append(dept.getDepartmentName()).append("\n");
        sb.append("Head of Dept    : ")
                .append(dept.getHeadOfDepartment() != null ? dept.getHeadOfDepartment().getName() : "None")
                .append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("STATISTICS OVERVIEW:\n");
        sb.append(" - Total Faculty    : ").append(dept.getTeachers().size()).append("\n");
        sb.append(" - Total Courses    : ").append(dept.getCourses().size()).append("\n");
        sb.append(" - Total Classrooms : ").append(dept.getDepartmentalClassrooms().size()).append("\n");
        sb.append(" - Total Labs       : ").append(dept.getDepartmentalLabs().size()).append("\n");
        sb.append(" - Total Students   : ").append(dept.getNumberOfStudents()).append("\n");
        sb.append("--------------------------------------------------\n");

        // List teachers
        sb.append("FACULTY:\n");
        if (dept.getTeachers().isEmpty()) {
            sb.append(" [No teachers assigned]\n");
        } else {
            for (Teacher t : dept.getTeachers()) {
                sb.append(" • ").append(t.getName()).append(" (").append(t.getTeacherID()).append(") — ")
                        .append(t.getQualification()).append("\n");
            }
        }
        sb.append("--------------------------------------------------\n");

        // List courses with enrolled students
        sb.append("COURSES:\n");
        if (dept.getCourses().isEmpty()) {
            sb.append(" [No courses assigned]\n");
        } else {
            for (Course c : dm.getRepoCourses().getAll()) {
                sb.append(" ▸ ").append(c.getCourseName()).append(" (").append(c.getCourseId()).append(")");
                sb.append(" — ").append(c.getDay()).append(" ").append(c.getTime());
                sb.append(" — Enrolled: ").append(c.getStudents().size()).append("/").append(c.getMaxCapacity());
                sb.append("\n");
                if (!c.getStudents().isEmpty()) {
                    for (Student s : c.getStudents()) {
                        sb.append("     └ ").append(s.getName()).append(" (").append(s.getStudentID()).append(")\n");
                    }
                }
            }
        }
        sb.append("--------------------------------------------------\n");
        sb.append("TOTAL OPERATIONAL COST: $").append(String.format("%.2f", dept.calculateOperationalCost()))
                .append("\n");
        sb.append("==================================================\n");

        reportArea.setText(sb.toString());
    }

    private void markClassroomUnavailable() {
        Department dept = getSelectedDepartment();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        if (dept.getDepartmentalClassrooms().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This department has no classrooms assigned.");
            return;
        }

        // Build dropdown — only show available classrooms as candidates to mark
        // unavailable
        java.util.List<Classroom> rooms = dept.getDepartmentalClassrooms();
        java.util.List<Classroom> availableRooms = new java.util.ArrayList<>();
        for (Classroom c : rooms) {
            if (c.isAvailable())
                availableRooms.add(c);
        }
        if (availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All classrooms in this department are already unavailable.");
            return;
        }

        String[] options = new String[availableRooms.size()];
        for (int i = 0; i < availableRooms.size(); i++) {
            Classroom c = availableRooms.get(i);
            options[i] = c.getEntityName() + " (" + c.getClassNumber() + ") — Booked Slots: "
                    + c.getOccupiedSlots().size();
        }

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select classroom to mark unavailable in " + dept.getDepartmentName() + ":",
                "Mark Classroom Unavailable",
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (choice == null)
            return;

        // Resolve target classroom
        String classNum = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
        Classroom target = null;
        for (Classroom c : availableRooms) {
            if (c.getClassNumber().equals(classNum)) {
                target = c;
                break;
            }
        }
        if (target == null)
            return;

        // Capture the slots that were booked on the target before clearing
        java.util.List<String> affectedSlots = new java.util.ArrayList<>(target.getOccupiedSlots());

        // Mark target as closed and clear its slots
        target.markUnavailable();
        target.setStatus("Closed");

        // Find the next available classroom in the same department (not the target)
        Classroom replacement = null;
        for (Classroom c : rooms) {
            if (!c.getClassNumber().equals(target.getClassNumber()) && c.isAvailable()) {
                replacement = c;
                break;
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("=== Classroom Unavailability Report ===\n\n");
        report.append("Classroom Closed : ").append(target.getClassNumber())
                .append(" (").append(target.getEntityName()).append(")\n");
        report.append("Affected Slots   : ").append(affectedSlots.isEmpty() ? "None" : affectedSlots.toString())
                .append("\n\n");

        if (replacement == null) {
            report.append("No replacement classroom found in this department.\n");
            report.append("Courses using ").append(target.getClassNumber())
                    .append(" have had their classroom cleared.\n");
            // Still clear classroom from affected courses
            for (Course c : dept.getCourses()) {
                if (c.getClassroom() != null && c.getClassroom().getClassNumber().equals(target.getClassNumber())) {
                    c.setClassroom(null);
                    report.append(" - Course '").append(c.getCourseName()).append("' classroom cleared.\n");
                }
            }
        } else {
            // Book the same slots on the replacement classroom
            for (String slot : affectedSlots) {
                // slot format is "Day-Time" e.g. "Monday-9-12"
                int dashIdx = slot.indexOf("-");
                if (dashIdx >= 0) {
                    String day = slot.substring(0, dashIdx);
                    String time = slot.substring(dashIdx + 1);
                    if (replacement.isSlotAvailable(day, time)) {
                        replacement.bookSlot(day, time);
                    }
                }
            }
            // Reassign all courses in this dept from old classroom to replacement
            for (Course c : dept.getCourses()) {
                if (c.getClassroom() != null && c.getClassroom().getClassNumber().equals(target.getClassNumber())) {
                    c.setClassroom(replacement);
                    report.append(" - Course '").append(c.getCourseName())
                            .append("' reassigned from ").append(target.getClassNumber())
                            .append(" → ").append(replacement.getClassNumber())
                            .append(" (").append(replacement.getEntityName()).append(")\n");
                }
            }
            report.append("\nReplacement Room : ").append(replacement.getClassNumber())
                    .append(" (").append(replacement.getEntityName()).append(")\n");
            report.append("Slots transferred: ").append(affectedSlots.size()).append("\n");
            report.append("New booked slots on replacement: ").append(replacement.getOccupiedSlots().size())
                    .append("\n");
        }

        report.append("\n[Refresh the Courses tab to see updated classroom assignments]");
        reportArea.setText(report.toString());
        refreshTable();

        JOptionPane.showMessageDialog(this,
                "Done! " + target.getClassNumber() + " is now Closed.\n\n"
                        + (replacement != null
                                ? "Slots and courses transferred to " + replacement.getClassNumber() + "."
                                : "No replacement found. Course classrooms cleared.")
                        + "\n\nClick Refresh in the Courses tab to see updated assignments.",
                "Rescheduling Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addEquipmentToDept() {
        String type = JOptionPane.showInputDialog(this, "Equipment Type (e.g. Projector, Computer):");
        if (type == null || type.isEmpty())
            return;

        String name = JOptionPane.showInputDialog(this, "Equipment Name:");
        if (name == null || name.isEmpty())
            return;

        String costStr = JOptionPane.showInputDialog(this, "Operational Cost per hour:");
        if (costStr == null || costStr.isEmpty())
            return;

        try {
            Department dept = getSelectedDepartment();
            if (dept == null) {
                JOptionPane.showMessageDialog(this, "Select a department first.");
                return;
            }
            Equipment eq = new Equipment(type, name, Integer.parseInt(costStr));
            dept.addEquipment(eq);
            JOptionPane.showMessageDialog(this, "Equipment added to " + dept.getDepartmentName() + "!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cost value.");
        }
    }

    private void refreshComboBox() {
        hodBox.removeAllItems();
        hodBox.addItem("None");
        for (Teacher t : dm.getRepoTeachers().getAll()) {
            hodBox.addItem(t.getName() + " (" + t.getTeacherID() + ")");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Department d : dm.getRepoDepartments().getAll()) {
            tableModel.addRow(new Object[] {
                    d.getDepartmentName(),
                    d.getEntityID() != null ? d.getEntityID() : "N/A",
                    d.getLocation() != null ? d.getLocation() : "N/A",
                    d.getStatus() != null ? d.getStatus() : "Active",
                    (d.getHeadOfDepartment() != null ? d.getHeadOfDepartment().getName() : "None"),
                    d.getTeachers().size(),
                    d.getCourses().size(),
                    d.getDepartmentalClassrooms().size(),
                    d.getDepartmentalLabs().size(),
                    d.getNumberOfStudents(),
                    d.getEquipments().size()
            });
        }
    }
}
