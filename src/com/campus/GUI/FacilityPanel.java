package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.Person.Student;
import com.campus.facility.Book;
import com.campus.facility.Cafeteria;
import com.campus.facility.Hostel;
import com.campus.facility.Library;
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
        JPanel inputPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        JTextField libNameField = new JTextField();
        JTextField timingsField = new JTextField();
        JTextField costPerBookField = new JTextField();

        inputPanel.add(new JLabel("Library Name:"));
        inputPanel.add(libNameField);
        inputPanel.add(new JLabel("Timings:"));
        inputPanel.add(timingsField);
        inputPanel.add(new JLabel("Cost/Book:"));
        inputPanel.add(costPerBookField);
        panel.add(inputPanel, BorderLayout.NORTH);

        // Center Table — shows Libraries
        String[] cols = { "ID", "Name", "Timings", "Total Books", "Available", "Status" };
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

        row1.add(addLibBtn);
        row1.add(removeLibBtn);
        row1.add(addBookBtn);
        row1.add(issueBtn);
        row1.add(returnBtn);
        row2.add(editBtn);
        row2.add(costBtn);
        row2.add(viewBtn);
        row2.add(refreshBtn);
        btnPanel.add(row1);
        btnPanel.add(row2);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Refresh action
        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Library lib : dm.getRepoLibraries().getAll()) {
                model.addRow(new Object[] { lib.getEntityID(), lib.getEntityName(), lib.getTimings(),
                        (int) lib.getBooksCount(), lib.getAvailableBooks().size(), lib.getStatus() });
            }
        };

        addLibBtn.addActionListener(e -> {
            try {
                Library lib = new Library("LIB-" + System.currentTimeMillis(), libNameField.getText(), "Campus",
                        2000, 0, 500, true, timingsField.getText(), Double.parseDouble(costPerBookField.getText()));
                dm.getRepoLibraries().add(lib);
                libNameField.setText("");
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
            java.util.List<Book> alreadyIn = targetLib.getAvailableBooks();

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

            String isbn = JOptionPane.showInputDialog(panel, "Enter ISBN of book to issue:");
            if (isbn != null && !isbn.isEmpty()) {
                boolean success = targetLib.issueBook(isbn);
                if (success) {
                    JOptionPane.showMessageDialog(panel, "Book issued!");
                } else {
                    JOptionPane.showMessageDialog(panel, "Book not available or not found.");
                }
                refresh.run();
            }
        });

        returnBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a library first.");
                return;
            }
            String isbn = JOptionPane.showInputDialog(panel, "Enter ISBN of book to return:");
            if (isbn != null && !isbn.isEmpty()) {
                for (Book b : dm.getRepoBooks().getAll()) {
                    if (b.getISBN().equals(isbn)) {
                        b.returnBook();
                        JOptionPane.showMessageDialog(panel, "Book returned!");
                        refresh.run();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(panel, "Book not found.");
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

                    int toggle = JOptionPane.showConfirmDialog(panel,
                            "Toggle open/closed? Current: " + (lib.isOpen() ? "Open" : "Closed"));
                    if (toggle == JOptionPane.YES_OPTION)
                        lib.setOpen(!lib.isOpen());

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
        JTextField nameField = new JTextField();
        JTextField capField = new JTextField();
        JTextField roomsField = new JTextField();
        JTextField wardenField = new JTextField();
        JTextField hoursField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField utilField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[] { "Boys", "Girls" });

        inputPanel.add(new JLabel("Hostel Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capField);
        inputPanel.add(new JLabel("Total Rooms:"));
        inputPanel.add(roomsField);
        inputPanel.add(new JLabel("Warden Name:"));
        inputPanel.add(wardenField);
        inputPanel.add(new JLabel("Opening Hours:"));
        inputPanel.add(hoursField);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(contactField);
        inputPanel.add(new JLabel("Utilities/Room:"));
        inputPanel.add(utilField);
        inputPanel.add(new JLabel("Hostel Type:"));
        inputPanel.add(typeBox);
        panel.add(inputPanel, BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Type", "Rooms", "Occupied", "Warden", "Status" };
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
                model.addRow(new Object[] { h.getEntityID(), h.getEntityName(), h.getHostelType(),
                        h.getTotalRooms(), h.getOccupiedRooms(), h.getWardenName(), h.getStatus() });
            }
        };

        addBtn.addActionListener(e -> {
            try {
                Hostel h = new Hostel("H-" + System.currentTimeMillis(), nameField.getText(), "Campus",
                        5000, 0, Integer.parseInt(capField.getText()), true,
                        Integer.parseInt(roomsField.getText()), 0,
                        wardenField.getText(), hoursField.getText(), contactField.getText(),
                        Double.parseDouble(utilField.getText()), (String) typeBox.getSelectedItem());
                dm.getRepoHostels().add(h);
                nameField.setText("");
                capField.setText("");
                roomsField.setText("");
                wardenField.setText("");
                hoursField.setText("");
                contactField.setText("");
                utilField.setText("");
                refresh.run();
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
                    String warden = JOptionPane.showInputDialog(panel, "Warden name (blank to keep):",
                            h.getWardenName());
                    if (warden != null && !warden.isEmpty())
                        h.setWardenName(warden);

                    String contact = JOptionPane.showInputDialog(panel, "Contact (blank to keep):", h.getContact());
                    if (contact != null && !contact.isEmpty())
                        h.setContact(contact);

                    String hours = JOptionPane.showInputDialog(panel, "Opening hours (blank to keep):",
                            h.getOpeningHours());
                    if (hours != null && !hours.isEmpty())
                        h.setOpeningHours(hours);

                    int toggle = JOptionPane.showConfirmDialog(panel, "Toggle open/closed? Current: " + h.getStatus());
                    if (toggle == JOptionPane.YES_OPTION)
                        h.setOpen(!h.isOpen());

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

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JTextField nameField = new JTextField();
        JTextField timingField = new JTextField();
        JTextField staffField = new JTextField();
        JTextField salaryField = new JTextField();

        inputPanel.add(new JLabel("Cafeteria Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Timings:"));
        inputPanel.add(timingField);
        inputPanel.add(new JLabel("Staff Count:"));
        inputPanel.add(staffField);
        inputPanel.add(new JLabel("Staff Salary:"));
        inputPanel.add(salaryField);
        panel.add(inputPanel, BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Timings", "Staff", "Menu Items", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Cafeteria");
        JButton removeBtn = new JButton("Remove");
        JButton menuBtn = new JButton("Add Menu Item");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(menuBtn);
        btnPanel.add(costBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                model.addRow(new Object[] { c.getEntityID(), c.getEntityName(), c.getTiming(),
                        c.getStaffCount(), c.getMenu().size(), c.getStatus() });
            }
        };

        addBtn.addActionListener(e -> {
            try {
                Cafeteria c = new Cafeteria("C-" + System.currentTimeMillis(), nameField.getText(), "Campus",
                        1000, 0, 100, true,
                        Integer.parseInt(staffField.getText()),
                        Double.parseDouble(salaryField.getText()),
                        500,
                        timingField.getText());
                dm.getRepoCafeterias().add(c);
                nameField.setText("");
                timingField.setText("");
                staffField.setText("");
                salaryField.setText("");
                refresh.run();
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