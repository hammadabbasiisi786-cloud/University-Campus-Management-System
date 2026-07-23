package com.campus.frontend;

import com.campus.backend.fileio.DataManager;
import com.campus.backend.person.Student;
import com.campus.backend.facility.Book;
import com.campus.backend.facility.Cafeteria;
import com.campus.backend.facility.Hostel;
import com.campus.backend.facility.Library;
import com.campus.backend.facility.IssuedRecord;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FacilityPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTabbedPane tabbedPane;

    // CONSTRUCTOR
    public FacilityPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Libraries", createLibraryTab());
        tabbedPane.addTab("Hostels", createHostelTab());
        tabbedPane.addTab("Cafeterias", createCafeteriaTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------
    // 1. LIBRARY TAB — Library and Book are now separate
    // ---------------------------------------------------------
    private JPanel createLibraryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top Inputs for Library creation
        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField libIdField = new JTextField();
        JTextField libNameField = new JTextField();
        JTextField libLocationField = new JTextField();
        JTextField libCostField = new JTextField();
        JTextField libCapField = new JTextField();
        JTextField timingsField = new JTextField();
        JTextField costPerBookField = new JTextField();

        inputPanel.add(new JLabel("Library ID:"));
        inputPanel.add(libIdField);
        inputPanel.add(new JLabel("Library Name:"));
        inputPanel.add(libNameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(libLocationField);
        inputPanel.add(new JLabel("Maint. Cost:"));
        inputPanel.add(libCostField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(libCapField);
        inputPanel.add(new JLabel("Timings:"));
        inputPanel.add(timingsField);
        inputPanel.add(new JLabel("Cost/Book:"));
        inputPanel.add(costPerBookField);
        panel.add(inputPanel, BorderLayout.NORTH);

        // Center Table — shows Libraries
        String[] cols = { "ID", "Name", "Location", "Status", "Maint. Cost", "Capacity", "Timings", "Cost/Book",
                "Total Books", "Issued" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        JButton addLibBtn = new JButton("Add Library");
        JButton removeLibBtn = new JButton("Remove Library");
        JButton addBookBtn = new JButton("Add Book to Library");
        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");
        JButton editBtn = new JButton("Edit Library");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");
        JButton generateReportBtn = new JButton("Generate Report");

        row1.add(addLibBtn);
        row1.add(removeLibBtn);
        row1.add(addBookBtn);
        row1.add(issueBtn);
        row1.add(returnBtn);
        row2.add(editBtn);
        row2.add(costBtn);
        row2.add(viewBtn);
        row2.add(refreshBtn);
        row2.add(generateReportBtn);
        btnPanel.add(row1);
        btnPanel.add(row2);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Refresh action
        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Library lib : dm.getRepoLibraries().getAll()) {
                model.addRow(new Object[] {
                        lib.getEntityID(),
                        lib.getEntityName(),
                        lib.getLocation(),
                        lib.getStatus(),
                        lib.getMaintenanceCost(),
                        lib.getCapacity(),
                        lib.getTimings(),
                        lib.getCostPerBook(),
                        lib.getAllBooks().size(),
                        lib.getIssuedRecords().size() });
            }
        };

        addLibBtn.addActionListener(e -> {
            try {
                if (libIdField.getText().isEmpty() || libNameField.getText().isEmpty()
                        || libLocationField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "ID, Name, and Location are required.");
                    return;
                }
                Library lib = new Library(
                        libIdField.getText(),
                        libNameField.getText(),
                        libLocationField.getText(),
                        "Active",
                        Double.parseDouble(libCostField.getText()),
                        Integer.parseInt(libCapField.getText()),
                        timingsField.getText(),
                        Double.parseDouble(costPerBookField.getText()));
                dm.getRepoLibraries().add(lib);
                libIdField.setText("");
                libNameField.setText("");
                libLocationField.setText("");
                libCostField.setText("");
                libCapField.setText("");
                timingsField.setText("");
                costPerBookField.setText("");
                refresh.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Fill all fields correctly.");
            }
        });

        removeLibBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                dm.getRepoLibraries().getAll().removeIf(l -> l.getEntityID().equals(id));
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
            }
        });

        // UPDATED METHOD: Adds existing books from the global repository instead of
        // creating new ones
        addBookBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            Library targetLib = null;
            for (Library lib : dm.getRepoLibraries().getAll()) {
                if (lib.getEntityID().equals(id)) {
                    targetLib = lib;
                    break;
                }
            }
            if (targetLib == null)
                return;

            // Build list of books from global repo that are NOT already in this library
            java.util.List<Book> globalBooks = dm.getRepoBooks().getAll();
            java.util.List<Book> alreadyIn = targetLib.getAllBooks();

            java.util.List<Book> available = new java.util.ArrayList<>();
            for (Book b : globalBooks) {
                if (!alreadyIn.contains(b)) {
                    available.add(b);
                }
            }

            if (available.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "No books available to add.\nAdd books first from the Books panel.");
                return;
            }

            // Build display options: "Title (ISBN)"
            String[] options = new String[available.size()];
            for (int i = 0; i < available.size(); i++) {
                Book b = available.get(i);
                options[i] = b.getTitle() + " (" + b.getISBN() + ")";
            }

            String choice = (String) JOptionPane.showInputDialog(
                    panel,
                    "Select a book to add to " + targetLib.getEntityName() + ":",
                    "Add Book to Library",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice != null) {
                // Extract ISBN from "Title (ISBN)"
                String isbn = choice.substring(choice.lastIndexOf("(") + 1, choice.lastIndexOf(")"));
                for (Book b : available) {
                    if (b.getISBN().equals(isbn)) {
                        targetLib.addBook(b);
                        JOptionPane.showMessageDialog(panel,
                                "\"" + b.getTitle() + "\" added to " + targetLib.getEntityName());
                        refresh.run();
                        break;
                    }
                }
            }
        });

        issueBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            Library targetLib = null;
            for (Library lib : dm.getRepoLibraries().getAll()) {
                if (lib.getEntityID().equals(id)) {
                    targetLib = lib;
                    break;
                }
            }
            if (targetLib == null)
                return;
            // Pick student
            int sCount = dm.getRepoStudents().getAll().size();
            if (sCount == 0) {
                JOptionPane.showMessageDialog(panel, "No students in the system.");
                return;
            }
            String[] sOptions = new String[sCount];
            int si = 0;
            for (Student s : dm.getRepoStudents().getAll())
                sOptions[si++] = s.getName() + " (" + s.getStudentID() + ")";
            String sChoice = (String) JOptionPane.showInputDialog(panel, "Select Student:",
                    "Issue Book", JOptionPane.QUESTION_MESSAGE, null, sOptions, sOptions[0]);
            if (sChoice == null)
                return;
            String sid = sChoice.substring(sChoice.indexOf("(") + 1, sChoice.indexOf(")"));
            Student selectedStudent = null;
            for (Student s : dm.getRepoStudents().getAll())
                if (s.getStudentID().equals(sid)) {
                    selectedStudent = s;
                    break;
                }
            if (selectedStudent == null)
                return;
            String isbn = JOptionPane.showInputDialog(panel, "Enter ISBN of book to issue:");
            if (isbn != null && !isbn.isEmpty()) {
                boolean success = targetLib.issueBook(isbn, selectedStudent);
                JOptionPane.showMessageDialog(panel, success ? "Book issued!" : "Book not available or not found.");
                refresh.run();
            }
        });

        returnBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            Library targetLib = null;
            for (Library lib : dm.getRepoLibraries().getAll())
                if (lib.getEntityID().equals(id)) {
                    targetLib = lib;
                    break;
                }
            if (targetLib == null)
                return;
            // Pick student
            int sCount = dm.getRepoStudents().getAll().size();
            if (sCount == 0) {
                JOptionPane.showMessageDialog(panel, "No students in the system.");
                return;
            }
            String[] sOptions = new String[sCount];
            int si = 0;
            for (Student s : dm.getRepoStudents().getAll())
                sOptions[si++] = s.getName() + " (" + s.getStudentID() + ")";
            String sChoice = (String) JOptionPane.showInputDialog(panel, "Select Student:",
                    "Return Book", JOptionPane.QUESTION_MESSAGE, null, sOptions, sOptions[0]);
            if (sChoice == null)
                return;
            String sid = sChoice.substring(sChoice.indexOf("(") + 1, sChoice.indexOf(")"));
            Student selectedStudent = null;
            for (Student s : dm.getRepoStudents().getAll())
                if (s.getStudentID().equals(sid)) {
                    selectedStudent = s;
                    break;
                }
            if (selectedStudent == null)
                return;
            String isbn = JOptionPane.showInputDialog(panel, "Enter ISBN of book to return:");
            if (isbn != null && !isbn.isEmpty()) {
                boolean success = targetLib.returnBook(isbn, selectedStudent);
                JOptionPane.showMessageDialog(panel,
                        success ? "Book returned!" : "No record found for this book and student.");
                refresh.run();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (Library lib : dm.getRepoLibraries().getAll()) {
                if (lib.getEntityID().equals(id)) {
                    String newTimings = JOptionPane.showInputDialog(panel, "Timings (blank to keep):",
                            lib.getTimings());
                    if (newTimings != null && !newTimings.isEmpty())
                        lib.setTimings(newTimings);

                    String capStr = JOptionPane.showInputDialog(panel, "Capacity (blank to keep):", lib.getCapacity());
                    if (capStr != null && !capStr.isEmpty()) {
                        try {
                            lib.setCapacity(Integer.parseInt(capStr));
                        } catch (NumberFormatException ex) {
                        }
                    }

                    String[] statusOptions = { "Active", "Busy", "Closed" };
                    String newStatus = (String) JOptionPane.showInputDialog(panel, "Set Status:",
                            "Edit Status", JOptionPane.QUESTION_MESSAGE, null, statusOptions, lib.getStatus());
                    if (newStatus != null)
                        lib.setStatus(newStatus);

                    JOptionPane.showMessageDialog(panel, "Library updated!");
                    refresh.run();
                    break;
                }
            }
        });

        costBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (Library lib : dm.getRepoLibraries().getAll()) {
                    if (lib.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel,
                                "Operational Cost: $" + String.format("%.2f", lib.calculateOperationalCost()),
                                "Op. Cost", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
            }
        });

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (Library lib : dm.getRepoLibraries().getAll()) {
                    if (lib.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel, lib.toString(), "Library Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
            }
        });

        generateReportBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (Library lib : dm.getRepoLibraries().getAll()) {
                if (lib.getEntityID().equals(id)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("==== LIBRARY REPORT ====\n");
                    sb.append("Name    : ").append(lib.getEntityName()).append("\n");
                    sb.append("Status  : ").append(lib.getStatus()).append("\n");
                    sb.append("Timings : ").append(lib.getTimings()).append("\n");
                    sb.append("------------------------\n");
                    sb.append("Total Books : ").append(lib.getAllBooks().size()).append("\n");
                    sb.append("Issued Books: ").append(lib.getIssuedRecords().size()).append("\n");
                    sb.append("------------------------\n");
                    sb.append("ISSUED BOOKS:\n");
                    if (lib.getIssuedRecords().isEmpty()) {
                        sb.append(" No books currently issued.\n");
                    } else {
                        for (IssuedRecord r : lib.getIssuedRecords()) {
                            sb.append(" - ").append(r.getBook().getTitle())
                                    .append(" (").append(r.getBook().getISBN()).append(")")
                                    .append(" → ").append(r.getStudent().getName())
                                    .append(" (").append(r.getStudent().getStudentID()).append(")\n");
                        }
                    }
                    sb.append("------------------------\n");
                    sb.append(String.format("Op. Cost: $%,.2f\n", lib.calculateOperationalCost()));
                    JOptionPane.showMessageDialog(panel, sb.toString(), "Library Report",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        });

        refreshBtn.addActionListener(e -> refresh.run());
        refresh.run();

        return panel;
    }

    // ---------------------------------------------------------
    // 2. HOSTEL TAB
    // ---------------------------------------------------------
    private JPanel createHostelTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locField = new JTextField();
        JTextField costField = new JTextField();
        JTextField capField = new JTextField();
        JTextField roomsField = new JTextField();
        JTextField wardenField = new JTextField();
        JTextField timingField = new JTextField();

        inputPanel.add(new JLabel("Hostel ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Hostel Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locField);
        inputPanel.add(new JLabel("Maint. Cost:"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capField);
        inputPanel.add(new JLabel("Total Rooms:"));
        inputPanel.add(roomsField);
        inputPanel.add(new JLabel("Warden Name:"));
        inputPanel.add(wardenField);
        inputPanel.add(new JLabel("Timing:"));
        inputPanel.add(timingField);
        panel.add(inputPanel, BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Location", "Maint. Cost", "Capacity", "Total Rooms", "Occupied", "Warden",
                "Timing", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Hostel");
        JButton removeBtn = new JButton("Remove");
        JButton addResBtn = new JButton("Add Resident");
        JButton editBtn = new JButton("Edit Hostel");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(addResBtn);
        btnPanel.add(editBtn);
        btnPanel.add(costBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Hostel h : dm.getRepoHostels().getAll()) {
                model.addRow(new Object[] {
                        h.getEntityID(),
                        h.getEntityName(),
                        h.getLocation(),
                        h.getMaintenanceCost(),
                        h.getCapacity(),
                        h.getTotalRooms(),
                        h.getOccupiedRooms(),
                        h.getWardenName(),
                        h.getTiming(),
                        h.getStatus()
                });
            }
        };

        addBtn.addActionListener(e -> {
            try {
                String newId = idField.getText().trim();
                if (newId.isEmpty() || nameField.getText().isEmpty() || locField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "ID, Name, and Location are required.");
                    return;
                }
                // Duplicate ID check
                for (Hostel h : dm.getRepoHostels().getAll()) {
                    if (h.getEntityID().equals(newId)) {
                        JOptionPane.showMessageDialog(panel, "A hostel with ID \"" + newId + "\" already exists.");
                        return;
                    }
                }
                Hostel h = new Hostel(
                        newId,
                        nameField.getText(),
                        locField.getText(),
                        "Active",
                        Double.parseDouble(costField.getText()),
                        Integer.parseInt(capField.getText()),
                        Integer.parseInt(roomsField.getText()),
                        0,
                        wardenField.getText(),
                        timingField.getText());
                dm.getRepoHostels().add(h);
                idField.setText("");
                nameField.setText("");
                locField.setText("");
                costField.setText("");
                capField.setText("");
                roomsField.setText("");
                wardenField.setText("");
                timingField.setText("");
                refresh.run();
                JOptionPane.showMessageDialog(panel, "Hostel added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Fill all fields correctly.");
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                dm.getRepoHostels().getAll().removeIf(h -> h.getEntityID().equals(id));
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a hostel first.");
            }
        });

        addResBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a hostel first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            Hostel selectedHostel = null;
            for (Hostel h : dm.getRepoHostels().getAll()) {
                if (h.getEntityID().equals(id)) {
                    selectedHostel = h;
                    break;
                }
            }
            if (selectedHostel == null)
                return;

            int count = dm.getRepoStudents().getAll().size();
            if (count == 0) {
                JOptionPane.showMessageDialog(panel, "No students in the system.");
                return;
            }
            String[] options = new String[count];
            int i = 0;
            for (Student s : dm.getRepoStudents().getAll()) {
                options[i++] = s.getName() + " (" + s.getStudentID() + ")";
            }
            String choice = (String) JOptionPane.showInputDialog(panel, "Select Student:",
                    "Add Resident", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice != null) {
                String sid = choice.substring(choice.indexOf("(") + 1, choice.indexOf(")"));
                for (Student s : dm.getRepoStudents().getAll()) {
                    if (s.getStudentID().equals(sid)) {
                        selectedHostel.addResident(s);
                        JOptionPane.showMessageDialog(panel, "Resident added!");
                        break;
                    }
                }
                refresh.run();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a hostel first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (Hostel h : dm.getRepoHostels().getAll()) {
                if (h.getEntityID().equals(id)) {
                    String newName = JOptionPane.showInputDialog(panel, "Name (blank to keep):", h.getEntityName());
                    if (newName != null && !newName.isEmpty())
                        h.setEntityName(newName);
                    String newLoc = JOptionPane.showInputDialog(panel, "Location (blank to keep):", h.getLocation());
                    if (newLoc != null && !newLoc.isEmpty())
                        h.setLocation(newLoc);
                    String costStr = JOptionPane.showInputDialog(panel, "Maint. Cost (blank to keep):",
                            h.getMaintenanceCost());
                    if (costStr != null && !costStr.isEmpty()) {
                        try {
                            h.setMaintenanceCost(Double.parseDouble(costStr));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    String capStr = JOptionPane.showInputDialog(panel, "Capacity (blank to keep):", h.getCapacity());
                    if (capStr != null && !capStr.isEmpty()) {
                        try {
                            h.setCapacity(Integer.parseInt(capStr));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    String roomsStr = JOptionPane.showInputDialog(panel, "Total Rooms (blank to keep):",
                            h.getTotalRooms());
                    if (roomsStr != null && !roomsStr.isEmpty()) {
                        try {
                            h.setTotalRooms(Integer.parseInt(roomsStr));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    String warden = JOptionPane.showInputDialog(panel, "Warden Name (blank to keep):",
                            h.getWardenName());
                    if (warden != null && !warden.isEmpty())
                        h.setWardenName(warden);
                    String timing = JOptionPane.showInputDialog(panel, "Timing (blank to keep):", h.getTiming());
                    if (timing != null && !timing.isEmpty())
                        h.setTiming(timing);
                    String[] statusOptions = { "Active", "Busy", "Closed" };
                    String newStatus = (String) JOptionPane.showInputDialog(panel, "Set Status:",
                            "Edit Status", JOptionPane.QUESTION_MESSAGE, null, statusOptions, h.getStatus());
                    if (newStatus != null)
                        h.setStatus(newStatus);
                    JOptionPane.showMessageDialog(panel, "Hostel updated!");
                    refresh.run();
                    break;
                }
            }
        });

        costBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (Hostel h : dm.getRepoHostels().getAll()) {
                    if (h.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel,
                                "Operational Cost: $" + String.format("%.2f", h.calculateOperationalCost()),
                                "Op. Cost", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a hostel first.");
            }
        });

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (Hostel h : dm.getRepoHostels().getAll()) {
                    if (h.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel, h.toString(), "Hostel Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a hostel first.");
            }
        });

        refreshBtn.addActionListener(e -> refresh.run());
        refresh.run();

        return panel;
    }

    // ---------------------------------------------------------
    // 3. CAFETERIA TAB
    // ---------------------------------------------------------
    private JPanel createCafeteriaTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locField = new JTextField();
        JTextField costField = new JTextField();
        JTextField capField = new JTextField();
        JTextField staffField = new JTextField();
        JTextField timingField = new JTextField();

        inputPanel.add(new JLabel("Cafeteria ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Cafeteria Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locField);
        inputPanel.add(new JLabel("Maint. Cost:"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capField);
        inputPanel.add(new JLabel("Staff Count:"));
        inputPanel.add(staffField);
        inputPanel.add(new JLabel("Timing:"));
        inputPanel.add(timingField);
        panel.add(inputPanel, BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Location", "Maint. Cost", "Capacity",
                "Staff", "Timing", "Menu Items", "Cafeteria Usage", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Cafeteria");
        JButton removeBtn = new JButton("Remove");
        JButton menuBtn = new JButton("Add Menu Item");
        JButton editBtn = new JButton("Edit Cafeteria");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(menuBtn);
        btnPanel.add(editBtn);
        btnPanel.add(costBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                model.addRow(new Object[] {
                        c.getEntityID(),
                        c.getEntityName(),
                        c.getLocation(),
                        c.getMaintenanceCost(),
                        c.getCapacity(),
                        c.getNoOfStaff(),
                        c.getTiming(),
                        c.getMenu().size(),
                        c.getCafeteriaUsage(),
                        c.getStatus()
                });
            }
        };

        addBtn.addActionListener(e -> {
            try {
                String newId = idField.getText().trim();
                if (newId.isEmpty() || nameField.getText().isEmpty()
                        || locField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "ID, Name and Location are required.");
                    return;
                }
                // Duplicate ID check
                for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                    if (c.getEntityID().equals(newId)) {
                        JOptionPane.showMessageDialog(panel,
                                "A cafeteria with ID \"" + newId + "\" already exists.");
                        return;
                    }
                }
                Cafeteria c = new Cafeteria(
                        newId,
                        nameField.getText(),
                        locField.getText(),
                        "Active",
                        Double.parseDouble(costField.getText()),
                        Integer.parseInt(capField.getText()),
                        Integer.parseInt(staffField.getText()),
                        timingField.getText());
                dm.getRepoCafeterias().add(c);
                idField.setText("");
                nameField.setText("");
                locField.setText("");
                costField.setText("");
                capField.setText("");
                staffField.setText("");
                timingField.setText("");
                refresh.run();
                JOptionPane.showMessageDialog(panel, "Cafeteria added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Fill all fields correctly.");
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                dm.getRepoCafeterias().getAll().removeIf(c -> c.getEntityID().equals(id));
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a cafeteria first.");
            }
        });

        menuBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a cafeteria first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            String item = JOptionPane.showInputDialog(panel, "Enter Menu Item Name:");
            if (item != null && !item.isEmpty()) {
                for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                    if (c.getEntityID().equals(id)) {
                        c.addMenu(item);
                        JOptionPane.showMessageDialog(panel, "Menu item added: " + item);
                        break;
                    }
                }
                refresh.run();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a cafeteria first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                if (c.getEntityID().equals(id)) {
                    String newName = JOptionPane.showInputDialog(panel,
                            "Name (blank to keep):", c.getEntityName());
                    if (newName != null && !newName.isEmpty())
                        c.setEntityName(newName);
                    String newLoc = JOptionPane.showInputDialog(panel,
                            "Location (blank to keep):", c.getLocation());
                    if (newLoc != null && !newLoc.isEmpty())
                        c.setLocation(newLoc);
                    String costStr = JOptionPane.showInputDialog(panel,
                            "Maint. Cost (blank to keep):", c.getMaintenanceCost());
                    if (costStr != null && !costStr.isEmpty()) {
                        try {
                            c.setMaintenanceCost(Double.parseDouble(costStr));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    String capStr = JOptionPane.showInputDialog(panel,
                            "Capacity (blank to keep):", c.getCapacity());
                    if (capStr != null && !capStr.isEmpty()) {
                        try {
                            c.setCapacity(Integer.parseInt(capStr));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    String staffStr = JOptionPane.showInputDialog(panel,
                            "Staff Count (blank to keep):", c.getNoOfStaff());
                    if (staffStr != null && !staffStr.isEmpty()) {
                        try {
                            c.setNoOfStaff(Integer.parseInt(staffStr));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    String timing = JOptionPane.showInputDialog(panel,
                            "Timing (blank to keep):", c.getTiming());
                    if (timing != null && !timing.isEmpty())
                        c.setTiming(timing);
                    String[] statusOptions = { "Active", "Busy", "Closed" };
                    String newStatus = (String) JOptionPane.showInputDialog(panel,
                            "Set Status:", "Edit Status",
                            JOptionPane.QUESTION_MESSAGE, null, statusOptions, c.getStatus());
                    if (newStatus != null)
                        c.setStatus(newStatus);
                    JOptionPane.showMessageDialog(panel, "Cafeteria updated!");
                    refresh.run();
                    break;
                }
            }
        });

        costBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                    if (c.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel,
                                "Operational Cost: $" + String.format("%.2f", c.calculateOperationalCost()),
                                "Op. Cost", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a cafeteria first.");
            }
        });

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                    if (c.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel, c.toString(), "Cafeteria Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a cafeteria first.");
            }
        });

        refreshBtn.addActionListener(e -> refresh.run());
        refresh.run();

        return panel;
    }
}
