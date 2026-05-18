package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.academicunit.Course;
import com.campus.Person.Teacher;
import com.campus.Person.Student;
import com.campus.academicunit.Classroom;
import com.campus.academicunit.Assignment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField courseNameField;
    private JComboBox<Integer> creditHourBox;
    private JComboBox<String> teacherBox;
    private JComboBox<String> classroomBox;
    private JComboBox<String> dayBox;
    private JComboBox<String> timeBox;
    private JButton addButton;
    private JButton removeButton;
    private JButton enrollStudentButton;
    private JButton viewStudentsButton;
    private JButton viewAssignmentsButton;
    private JButton addAssignmentButton;
    private JButton assignClassroomButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton refreshButton;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public CoursePanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        inputPanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField();
        inputPanel.add(courseNameField);

        inputPanel.add(new JLabel("Credit Hours:"));
        Integer[] credits = { 1, 2, 3, 4 };
        creditHourBox = new JComboBox<>(credits);
        inputPanel.add(creditHourBox);

        inputPanel.add(new JLabel("Teacher:"));
        teacherBox = new JComboBox<>();
        inputPanel.add(teacherBox);

        inputPanel.add(new JLabel("Classroom:"));
        classroomBox = new JComboBox<>();
        inputPanel.add(classroomBox);

        inputPanel.add(new JLabel("Day:"));
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        dayBox = new JComboBox<>(days);
        inputPanel.add(dayBox);

        inputPanel.add(new JLabel("Time:"));
        String[] times = { "9-12", "12-2", "2-4" };
        timeBox = new JComboBox<>(times);
        inputPanel.add(timeBox);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table - also acts as the Timetable)
        String[] columns = { "Course ID", "Name", "Credits", "Teacher", "Classroom", "Day", "Time", "Enrolled" };
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        add(new JScrollPane(courseTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add Course");
        removeButton = new JButton("Remove");
        enrollStudentButton = new JButton("Enroll Student");
        viewStudentsButton = new JButton("View Students");
        viewAssignmentsButton = new JButton("View Assignments");
        addAssignmentButton = new JButton("Add Assignment");
        assignClassroomButton = new JButton("Assign Classroom");
        editButton = new JButton("Edit Course");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh List");

        // Use two rows for cleaner layout
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());
        JPanel buttonWrapper = new JPanel(new GridLayout(2, 1));

        row1.add(addButton);
        row1.add(removeButton);
        row1.add(enrollStudentButton);
        row1.add(viewStudentsButton);
        row1.add(viewAssignmentsButton);
        row2.add(addAssignmentButton);
        row2.add(assignClassroomButton);
        row2.add(editButton);
        row2.add(viewButton);
        row2.add(refreshButton);

        buttonWrapper.add(row1);
        buttonWrapper.add(row2);
        buttonPanel.add(buttonWrapper);

        add(buttonWrapper, BorderLayout.SOUTH);

        // Role-based UI Restrictions
        String role = dm.getLoginManager().getLoggedInRole();
        if ("TEACHER".equals(role) || "STUDENT".equals(role)) {
            inputPanel.setVisible(false);
            addButton.setVisible(false);
            removeButton.setVisible(false);
            enrollStudentButton.setVisible(false);
            assignClassroomButton.setVisible(false);
            editButton.setVisible(false);

            if ("STUDENT".equals(role)) {
                viewStudentsButton.setVisible(false);
                addAssignmentButton.setVisible(false);
            }
        }

        // 4. Connect Buttons to Methods
        addButton.addActionListener(e -> addCourse());
        removeButton.addActionListener(e -> removeCourse());
        enrollStudentButton.addActionListener(e -> enrollStudent());
        viewStudentsButton.addActionListener(e -> viewEnrolledStudents());
        viewAssignmentsButton.addActionListener(e -> viewAssignments());
        addAssignmentButton.addActionListener(e -> addAssignmentToCourse());
        assignClassroomButton.addActionListener(e -> assignClassroom());
        editButton.addActionListener(e -> editCourse());
        viewButton.addActionListener(e -> viewCourse());
        refreshButton.addActionListener(e -> {
            refreshComboBoxes();
            refreshTable();
        });

        // 5. Initial Load
        refreshComboBoxes();
        refreshTable();
    }

    // OTHER METHODS

    private void addCourse() {
        String name = courseNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a course name.");
            return;
        }

        int credits = (int) creditHourBox.getSelectedItem();
        String day = (String) dayBox.getSelectedItem();
        String time = (String) timeBox.getSelectedItem();

        // Find selected Teacher object
        Teacher selectedTeacher = null;
        String tSelection = (String) teacherBox.getSelectedItem();
        if (tSelection != null && !tSelection.equals("None")) {
            String tID = tSelection.substring(tSelection.indexOf("(") + 1, tSelection.indexOf(")"));
            for (Teacher t : dm.getRepoTeachers().getAll()) {
                if (t.getTeacherID().equals(tID)) {
                    selectedTeacher = t;
                    break;
                }
            }
        }

        // Find selected Classroom object
        Classroom selectedClassroom = null;
        String cSelection = (String) classroomBox.getSelectedItem();
        if (cSelection != null && !cSelection.equals("None")) {
            for (Classroom c : dm.getRepoClassrooms().getAll()) {
                if (c.getClassNumber().equals(cSelection)) {
                    selectedClassroom = c;
                    break;
                }
            }
        }

        // Check for slot conflict before creating the course
        if (selectedClassroom != null) {
            if (!selectedClassroom.isSlotAvailable(day, time)) {
                // Find next available slot for this classroom
                String[] allDays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
                String[] allTimes = { "9-12", "12-2", "2-4" };
                String nextDay = null;
                String nextTime = null;
                for (String d : allDays) {
                    for (String t : allTimes) {
                        if (selectedClassroom.isSlotAvailable(d, t)) {
                            nextDay = d;
                            nextTime = t;
                            break;
                        }
                    }
                    if (nextDay != null)
                        break;
                }

                if (nextDay != null) {
                    int opt = JOptionPane.showConfirmDialog(this,
                            "Slot " + day + " " + time + " is already booked for " + selectedClassroom.getClassNumber()
                                    + "!\n\n"
                                    + "Next available slot: " + nextDay + " " + nextTime + "\n\n"
                                    + "Would you like to assign this slot instead?",
                            "Slot Conflict — Auto-Assign?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (opt == JOptionPane.YES_OPTION) {
                        day = nextDay;
                        time = nextTime;
                        // Fall through to course creation below with the new day/time
                    } else {
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Slot " + day + " " + time + " is already booked and no other slots are available in "
                                    + selectedClassroom.getClassNumber() + ".",
                            "Slot Conflict", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            // NOTE: Do NOT bookSlot here — Course.setClassroom() handles booking internally
        }

        // Change A — Block course creation if no classroom is selected
        if (selectedClassroom == null) {
            JOptionPane.showMessageDialog(this,
                    "A classroom must be selected before adding a course.\nPlease add classrooms first or pick one from the dropdown.",
                    "Classroom Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create and add
        Course newCourse = new Course(name, credits, selectedTeacher, selectedClassroom, day, time);
        dm.getRepoCourses().add(newCourse);

        JOptionPane.showMessageDialog(this, "Course Added Successfully!\n\n"
                + "Room: " + (selectedClassroom != null ? selectedClassroom.getClassNumber() : "None")
                + " | Day: " + day + " | Time: " + time);
        courseNameField.setText("");
        refreshTable();
    }

    private void removeCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            String courseId = (String) tableModel.getValueAt(selectedRow, 0);

            for (Course c : dm.getRepoCourses().getAll()) {
                if (c.getCourseId().equals(courseId)) {
                    dm.getRepoCourses().remove(c);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please click on a course in the table to remove it.");
        }
    }

    private void enrollStudent() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a Course from the table first.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        Course selectedCourse = null;
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getCourseId().equals(courseId)) {
                selectedCourse = c;
                break;
            }
        }

        if (selectedCourse == null)
            return;

        int studentCount = dm.getRepoStudents().getAll().size();
        if (studentCount == 0) {
            JOptionPane.showMessageDialog(this, "No students registered in the system yet!");
            return;
        }

        String[] studentOptions = new String[studentCount];
        int i = 0;
        for (Student s : dm.getRepoStudents().getAll()) {
            studentOptions[i++] = s.getName() + " (" + s.getStudentID() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(this, "Select a student to enroll:",
                "Enroll Student", JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);

        if (choice != null) {
            String studentId = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
            for (Student s : dm.getRepoStudents().getAll()) {
                if (s.getStudentID().equals(studentId)) {
                    selectedCourse.addStudent(s);
                    JOptionPane.showMessageDialog(this, s.getName() + " enrolled in " + selectedCourse.getCourseName());
                    refreshTable();
                    break;
                }
            }
        }
    }

    // Shows all enrolled students for the selected course
    private void viewEnrolledStudents() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getCourseId().equals(courseId)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Enrolled Students in ").append(c.getCourseName()).append(":\n");
                sb.append("--------------------------------------------------\n");
                if (c.getStudents().isEmpty()) {
                    sb.append("No students enrolled yet.");
                } else {
                    int num = 1;
                    for (Student s : c.getStudents()) {
                        sb.append(num++).append(". ").append(s.getName())
                                .append(" (").append(s.getStudentID()).append(") - Sem ")
                                .append(s.getSemester()).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Enrolled Students",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    // Shows all assignments for the selected course
    private void viewAssignments() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getCourseId().equals(courseId)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Assignments for ").append(c.getCourseName()).append(":\n");
                sb.append("--------------------------------------------------\n");
                if (c.getAssignments().isEmpty()) {
                    sb.append("No assignments yet.");
                } else {
                    for (Assignment a : c.getAssignments()) {
                        sb.append("Num: ").append(a.getAssignmentNumber())
                                .append(" | Title: ").append(a.getTitle())
                                .append(" | Due: ").append(a.getDueDate())
                                .append("\n");
                    }
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Assignments",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    // Change B — Replace the entire editCourse() method
    private void editCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        Course target = null;
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getCourseId().equals(courseId)) {
                target = c;
                break;
            }
        }
        if (target == null)
            return;

        // --- Edit Name ---
        String newName = JOptionPane.showInputDialog(this, "Course name (blank to keep):", target.getCourseName());
        if (newName != null && !newName.isEmpty()) {
            target.setCourseName(newName);
        }

        // --- Edit Classroom ---
        java.util.List<Classroom> allRooms = dm.getRepoClassrooms().getAll();
        if (!allRooms.isEmpty()) {
            // Build options: first entry = keep current
            String keepLabel = "Keep current: "
                    + (target.getClassroom() != null ? target.getClassroom().getClassNumber() : "None");
            String[] roomOptions = new String[allRooms.size() + 1];
            roomOptions[0] = keepLabel;
            for (int i = 0; i < allRooms.size(); i++) {
                Classroom r = allRooms.get(i);
                String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
                String[] times = { "9-12", "12-2", "2-4" };
                int free = 0;
                for (String d : days)
                    for (String t : times)
                        if (r.isSlotAvailable(d, t))
                            free++;
                roomOptions[i + 1] = r.getClassNumber() + " | Cap: " + r.getCapacity()
                        + " | Free Slots: " + free;
            }

            String roomChoice = (String) JOptionPane.showInputDialog(this,
                    "Select classroom (or keep current):",
                    "Edit Classroom",
                    JOptionPane.QUESTION_MESSAGE,
                    null, roomOptions, roomOptions[0]);

            if (roomChoice != null && !roomChoice.equals(keepLabel)) {
                // Resolve picked classroom
                String pickedNumber = roomChoice.split("\\|")[0].trim();
                Classroom pickedRoom = null;
                for (Classroom r : allRooms) {
                    if (r.getClassNumber().equals(pickedNumber)) {
                        pickedRoom = r;
                        break;
                    }
                }

                if (pickedRoom != null) {
                    // Find free slots in the picked classroom
                    String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
                    String[] times = { "9-12", "12-2", "2-4" };
                    java.util.List<String> freeSlots = new java.util.ArrayList<>();
                    for (String d : days)
                        for (String t : times)
                            if (pickedRoom.isSlotAvailable(d, t))
                                freeSlots.add(d + " | " + t);

                    if (freeSlots.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No free slots in " + pickedRoom.getClassNumber() + ". Choose a different classroom.",
                                "No Slots Available", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String[] slotArr = freeSlots.toArray(new String[0]);
                        String slotChoice = (String) JOptionPane.showInputDialog(this,
                                "Select a free slot in " + pickedRoom.getClassNumber() + ":",
                                "Pick Slot",
                                JOptionPane.QUESTION_MESSAGE,
                                null, slotArr, slotArr[0]);

                        if (slotChoice != null) {
                            String[] parts = slotChoice.split("\\|");
                            String newDay = parts[0].trim();
                            String newTime = parts[1].trim();

                            // Unbook old slot from old classroom
                            if (target.getClassroom() != null
                                    && target.getDay() != null
                                    && target.getTime() != null) {
                                target.getClassroom().unbookSlot(target.getDay(), target.getTime());
                            }

                            // Book new slot and update course
                            target.setDay(newDay);
                            target.setTime(newTime);
                            target.setClassroom(pickedRoom); // internally books the new slot
                            JOptionPane.showMessageDialog(this,
                                    "Classroom updated!\nRoom: " + pickedRoom.getClassNumber()
                                            + " | Day: " + newDay + " | Time: " + newTime);
                        }
                    }
                }
            } else if (roomChoice != null) {
                // Kept the same classroom — still allow day/time change within it
                Classroom currentRoom = target.getClassroom();
                if (currentRoom != null) {
                    String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
                    String[] times = { "9-12", "12-2", "2-4" };
                    java.util.List<String> freeSlots = new java.util.ArrayList<>();
                    // Include current slot so user can "keep" it too
                    freeSlots.add("Keep current: " + target.getDay() + " | " + target.getTime());
                    for (String d : days)
                        for (String t : times)
                            if (currentRoom.isSlotAvailable(d, t))
                                freeSlots.add(d + " | " + t);

                    String[] slotArr = freeSlots.toArray(new String[0]);
                    String slotChoice = (String) JOptionPane.showInputDialog(this,
                            "Change slot for " + currentRoom.getClassNumber() + " (or keep current):",
                            "Edit Day/Time",
                            JOptionPane.QUESTION_MESSAGE,
                            null, slotArr, slotArr[0]);

                    if (slotChoice != null && !slotChoice.startsWith("Keep current")) {
                        String[] parts = slotChoice.split("\\|");
                        String newDay = parts[0].trim();
                        String newTime = parts[1].trim();

                        // Unbook old, book new
                        currentRoom.unbookSlot(target.getDay(), target.getTime());
                        target.setDay(newDay);
                        target.setTime(newTime);
                        currentRoom.bookSlot(newDay, newTime);
                        JOptionPane.showMessageDialog(this,
                                "Schedule updated!\nDay: " + newDay + " | Time: " + newTime);
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Course updated!");
        refreshTable();
    }

    // Shows the full toString() of the selected course in a popup
    private void viewCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            String courseId = (String) tableModel.getValueAt(selectedRow, 0);
            for (Course c : dm.getRepoCourses().getAll()) {
                if (c.getCourseId().equals(courseId)) {
                    JOptionPane.showMessageDialog(this, c.toString(), "Course Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a course first.");
        }
    }

    private void refreshComboBoxes() {
        // Reload Teachers
        teacherBox.removeAllItems();
        teacherBox.addItem("None");
        for (Teacher t : dm.getRepoTeachers().getAll()) {
            teacherBox.addItem(t.getName() + " (" + t.getTeacherID() + ")");
        }

        // Reload Classrooms — only show AVAILABLE ones
        classroomBox.removeAllItems();
        classroomBox.addItem("None");
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            String label = c.getClassNumber();
            if (!c.isAvailable()) {
                label += " [UNAVAILABLE]";
            }
            classroomBox.addItem(label);
        }
    }

    private void addAssignmentToCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        Course selectedCourse = null;
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getCourseId().equals(courseId)) {
                selectedCourse = c;
                break;
            }
        }

        if (selectedCourse == null)
            return;

        // Prompt for assignment details
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        JTextField numberField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField issueDateField = new JTextField();
        JTextField dueDateField = new JTextField();
        JTextField totalMarksField = new JTextField();
        JTextField obtainedMarksField = new JTextField("0.0");

        panel.add(new JLabel("Assignment Number:"));
        panel.add(numberField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Issue Date (e.g. 2026-05-17):"));
        panel.add(issueDateField);
        panel.add(new JLabel("Due Date (e.g. 2026-05-24):"));
        panel.add(dueDateField);
        panel.add(new JLabel("Total Marks:"));
        panel.add(totalMarksField);
        panel.add(new JLabel("Obtained Marks (Default 0.0):"));
        panel.add(obtainedMarksField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Assignment to " + selectedCourse.getCourseName(), JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
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

                // Check for duplicate assignment number
                for (Assignment a : selectedCourse.getAssignments()) {
                    if (a.getAssignmentNumber() == number) {
                        JOptionPane.showMessageDialog(this,
                                "Assignment number " + number + " already exists for this course!");
                        return;
                    }
                }

                Assignment newAssignment = new Assignment(number, title, issueDate, dueDate, totalMarks, obtainedMarks);
                selectedCourse.addAssignment(newAssignment);

                JOptionPane.showMessageDialog(this, "Assignment Added Successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Number, Total Marks, and Obtained Marks must be valid numbers.");
            }
        }
    }

    private void assignClassroom() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }

        // --- Resolve selected course ---
        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        Course selectedCourse = null;
        for (Course c : dm.getRepoCourses().getAll()) {
            if (c.getCourseId().equals(courseId)) {
                selectedCourse = c;
                break;
            }
        }
        if (selectedCourse == null)
            return;

        // --- Check classrooms exist ---
        if (dm.getRepoClassrooms().getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No classrooms in the system. Add classrooms first.");
            return;
        }

        // --- Build classroom options ---
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        String[] times = { "9-12", "12-2", "2-4" };

        java.util.List<Classroom> allRooms = dm.getRepoClassrooms().getAll();
        String[] roomOptions = new String[allRooms.size()];
        for (int i = 0; i < allRooms.size(); i++) {
            Classroom r = allRooms.get(i);
            int free = 0;
            for (String d : days)
                for (String t : times)
                    if (r.isSlotAvailable(d, t))
                        free++;
            roomOptions[i] = r.getClassNumber() + " | Cap: " + r.getCapacity()
                    + " | Free Slots: " + free + (r.isAvailable() ? "" : " [UNAVAILABLE]");
        }

        // --- Step 1: Pick a classroom ---
        String roomChoice = (String) JOptionPane.showInputDialog(
                this,
                "Select a classroom to assign to course: " + selectedCourse.getCourseName(),
                "Step 1 — Choose Classroom",
                JOptionPane.QUESTION_MESSAGE,
                null, roomOptions, roomOptions[0]);

        if (roomChoice == null)
            return;

        String pickedNumber = roomChoice.split("\\|")[0].trim();
        Classroom pickedRoom = null;
        for (Classroom r : allRooms) {
            if (r.getClassNumber().equals(pickedNumber)) {
                pickedRoom = r;
                break;
            }
        }
        if (pickedRoom == null)
            return;

        // --- Show available slots for chosen classroom ---
        java.util.List<String> freeSlots = new java.util.ArrayList<>();
        for (String d : days) {
            for (String t : times) {
                if (pickedRoom.isSlotAvailable(d, t)) {
                    freeSlots.add(d + " | " + t);
                }
            }
        }

        // --- Step 2: Pick a slot ---
        String slotChoice;
        if (freeSlots.isEmpty()) {
            // No free slots → ask to auto-schedule
            int autoOpt = JOptionPane.showConfirmDialog(this,
                    "Room " + pickedRoom.getClassNumber() + " has NO free slots!\n\n"
                            + "Would you like the system to auto-find the next available slot\n"
                            + "across all classrooms in the same department?",
                    "No Slots Available",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (autoOpt == JOptionPane.YES_OPTION) {
                // Temporarily link classroom so generateSchedule() can walk the department
                selectedCourse.setClassroom(pickedRoom);
                String result = selectedCourse.generateSchedule();

                if (result.equals("No Slot Available")) {
                    JOptionPane.showMessageDialog(this,
                            "No free slots found across any classroom in the department.",
                            "Scheduling Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "✔ Auto-scheduled successfully!\n\n" + result,
                            "Classroom Assigned", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                }
            }
            return;
        }

        String[] slotArr = freeSlots.toArray(new String[0]);
        slotChoice = (String) JOptionPane.showInputDialog(
                this,
                "Available slots for " + pickedRoom.getClassNumber() + ":",
                "Step 2 — Choose Slot",
                JOptionPane.QUESTION_MESSAGE,
                null, slotArr, slotArr[0]);

        if (slotChoice == null)
            return;

        String[] parts = slotChoice.split("\\|");
        String chosenDay = parts[0].trim();
        String chosenTime = parts[1].trim();

        // --- Conflict check (double-safety) ---
        if (!pickedRoom.isSlotAvailable(chosenDay, chosenTime)) {
            int autoOpt = JOptionPane.showConfirmDialog(this,
                    "Conflict! Slot " + chosenDay + " " + chosenTime
                            + " in " + pickedRoom.getClassNumber() + " is already booked.\n\n"
                            + "Auto-find next available slot?",
                    "Slot Conflict Detected",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (autoOpt == JOptionPane.YES_OPTION) {
                selectedCourse.setClassroom(pickedRoom);
                String result = selectedCourse.generateSchedule();
                if (result.equals("No Slot Available")) {
                    JOptionPane.showMessageDialog(this,
                            "No free slots found across any classroom in the department.",
                            "Scheduling Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "✔ Auto-scheduled to next free slot!\n\n" + result,
                            "Classroom Assigned", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                }
            }
            return;
        }

        // --- Book the slot ---
        // Set day/time FIRST, then setClassroom() will book the slot internally
        selectedCourse.setDay(chosenDay);
        selectedCourse.setTime(chosenTime);
        selectedCourse.setClassroom(pickedRoom);

        JOptionPane.showMessageDialog(this,
                "✔ Classroom assigned successfully!\n\n"
                        + "Course   : " + selectedCourse.getCourseName() + "\n"
                        + "Room     : " + pickedRoom.getClassNumber() + "\n"
                        + "Day      : " + chosenDay + "\n"
                        + "Time     : " + chosenTime,
                "Assignment Confirmed", JOptionPane.INFORMATION_MESSAGE);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        String role = dm.getLoginManager().getLoggedInRole();
        Student loggedInStudent = null;
        if ("STUDENT".equals(role)) {
            loggedInStudent = (Student) dm.getLoginManager().getLoggedInUser();
        }

        for (Course c : dm.getRepoCourses().getAll()) {
            if (loggedInStudent != null && !c.getStudents().contains(loggedInStudent)) {
                continue; // Skip courses the student is not enrolled in
            }

            String teacherName = c.getTeacher() != null ? c.getTeacher().getName() : "None";
            String className = c.getClassroom() != null ? c.getClassroom().getClassNumber() : "None";

            tableModel.addRow(new Object[] {
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCreditHour(),
                    teacherName,
                    className,
                    c.getDay(),
                    c.getTime(),
                    c.getStudents().size() + " / " + c.getMaxCapacity()
            });
        }
    }
}