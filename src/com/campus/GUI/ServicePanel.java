package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.Serviceunit.HealthCenter;
import com.campus.Serviceunit.SecurityService;
import com.campus.Serviceunit.TransportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServicePanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTabbedPane tabbedPane;

    // CONSTRUCTOR
    public ServicePanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Transport", createTransportTab());
        tabbedPane.addTab("Security", createSecurityTab());
        tabbedPane.addTab("Health Centers", createHealthTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------
    // 1. TRANSPORT TAB
    // ---------------------------------------------------------
    private JPanel createTransportTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Input fields — 5 rows x 4 cols (label + field pairs)
        JPanel inputPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField serviceHoursField = new JTextField();
        JTextField hourlyRateField = new JTextField();
        JTextField busField = new JTextField();
        JTextField staffField = new JTextField();
        JTextField normalRouteField = new JTextField();
        JTextField peakRouteField = new JTextField();

        inputPanel.add(new JLabel("Service ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Service Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Service Hours:"));
        inputPanel.add(serviceHoursField);
        inputPanel.add(new JLabel("Hourly Rate:"));
        inputPanel.add(hourlyRateField);
        inputPanel.add(new JLabel("Number of Buses:"));
        inputPanel.add(busField);
        inputPanel.add(new JLabel("Staff Count:"));
        inputPanel.add(staffField);
        inputPanel.add(new JLabel("Normal Route:"));
        inputPanel.add(normalRouteField);
        inputPanel.add(new JLabel("Peak Hour Route:"));
        inputPanel.add(peakRouteField);
        panel.add(inputPanel, BorderLayout.NORTH);

        // Table columns
        String[] cols = { "ID", "Name", "Location", "Service Hrs", "Hourly Rate",
                "Buses", "Staff", "Normal Route", "Peak Hour Route", "Active Route", "Peak Hour", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons — split across two rows
        JPanel btnPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        JButton addBtn = new JButton("Add Transport");
        JButton removeBtn = new JButton("Remove");
        JButton editBtn = new JButton("Edit");
        JButton timeBtn = new JButton("Simulate Time");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");

        row1.add(addBtn);
        row1.add(removeBtn);
        row1.add(editBtn);
        row2.add(timeBtn);
        row2.add(costBtn);
        row2.add(viewBtn);
        row2.add(refreshBtn);
        btnPanel.add(row1);
        btnPanel.add(row2);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Refresh
        Runnable refresh = () -> {
            model.setRowCount(0);
            for (TransportService ts : dm.getRepoTransportServices().getAll()) {
                model.addRow(new Object[] {
                        ts.getEntityID(),
                        ts.getEntityName(),
                        ts.getLocation(),
                        ts.getServiceHours(),
                        String.format("%.2f", ts.getBaseHourlyRate()),
                        ts.getNumberOfBuses(),
                        ts.getStaffCount(),
                        ts.getNormalRouteName(),
                        ts.getPeakHourRouteName(),
                        ts.getActiveRouteName(),
                        ts.isPeakHour() ? "YES" : "NO",
                        ts.getStatus()
                });
            }
        };

        // Add Transport
        addBtn.addActionListener(e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
                    locationField.getText().isEmpty() || serviceHoursField.getText().isEmpty() ||
                    hourlyRateField.getText().isEmpty() || busField.getText().isEmpty() ||
                    staffField.getText().isEmpty() ||
                    normalRouteField.getText().isEmpty() || peakRouteField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields.");
                return;
            }

            // ID duplicate check
            String enteredID = idField.getText();
            for (TransportService ts : dm.getRepoTransportServices().getAll()) {
                if (ts.getEntityID().equals(enteredID)) {
                    JOptionPane.showMessageDialog(panel,
                            "ID \"" + enteredID + "\" already exists. Please use a unique ID.");
                    return;
                }
            }

            try {
                TransportService ts = new TransportService(
                        idField.getText(),
                        nameField.getText(),
                        locationField.getText(),
                        Integer.parseInt(staffField.getText()),
                        Integer.parseInt(serviceHoursField.getText()),
                        Double.parseDouble(hourlyRateField.getText()),
                        Integer.parseInt(busField.getText()),
                        normalRouteField.getText(),
                        peakRouteField.getText());
                dm.getRepoTransportServices().add(ts);
                idField.setText("");
                nameField.setText("");
                locationField.setText("");
                serviceHoursField.setText("");
                hourlyRateField.setText("");
                busField.setText("");
                staffField.setText("");
                normalRouteField.setText("");
                peakRouteField.setText("");
                refresh.run();
                JOptionPane.showMessageDialog(panel, "Transport Service added!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Buses, Staff and Service Hours must be whole numbers. Hourly Rate must be a number.");
            }
        });

        // Remove Transport
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                dm.getRepoTransportServices().getAll().removeIf(ts -> ts.getEntityID().equals(id));
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        // Edit Transport
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (TransportService ts : dm.getRepoTransportServices().getAll()) {
                if (ts.getEntityID().equals(id)) {

                    String name = JOptionPane.showInputDialog(panel, "Name (blank to keep):", ts.getEntityName());
                    if (name != null && !name.isEmpty())
                        ts.setEntityName(name);

                    String location = JOptionPane.showInputDialog(panel, "Location (blank to keep):", ts.getLocation());
                    if (location != null && !location.isEmpty())
                        ts.setLocation(location);

                    String svcHours = JOptionPane.showInputDialog(panel, "Service Hours (blank to keep):",
                            ts.getServiceHours());
                    if (svcHours != null && !svcHours.isEmpty()) {
                        try {
                            ts.setServiceHours(Integer.parseInt(svcHours));
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    String rate = JOptionPane.showInputDialog(panel, "Hourly Rate (blank to keep):",
                            ts.getBaseHourlyRate());
                    if (rate != null && !rate.isEmpty()) {
                        try {
                            ts.setBaseHourlyRate(Double.parseDouble(rate));
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    String buses = JOptionPane.showInputDialog(panel, "No. of Buses (blank to keep):",
                            ts.getNumberOfBuses());
                    if (buses != null && !buses.isEmpty()) {
                        try {
                            ts.setNumberOfBuses(Integer.parseInt(buses));
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    String normalRoute = JOptionPane.showInputDialog(panel, "Normal Route Name (blank to keep):",
                            ts.getNormalRouteName());
                    if (normalRoute != null && !normalRoute.isEmpty())
                        ts.setNormalRouteName(normalRoute);

                    String peakRoute = JOptionPane.showInputDialog(panel, "Peak Hour Route Name (blank to keep):",
                            ts.getPeakHourRouteName());
                    if (peakRoute != null && !peakRoute.isEmpty())
                        ts.setPeakHourRouteName(peakRoute);

                    String staffStr = JOptionPane.showInputDialog(panel, "Staff Count (blank to keep):",
                            ts.getStaffCount());
                    if (staffStr != null && !staffStr.isEmpty()) {
                        try {
                            ts.setStaffCount(Integer.parseInt(staffStr));
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    String[] statusOptions = { "Active", "Busy", "Closed" };
                    String status = (String) JOptionPane.showInputDialog(panel, "Set Status:",
                            "Status", JOptionPane.QUESTION_MESSAGE, null,
                            statusOptions, ts.getStatus());
                    if (status != null)
                        ts.setStatus(status);

                    JOptionPane.showMessageDialog(panel, "Transport service updated!");
                    refresh.run();
                    break;
                }
            }
        });

        // Simulate Time
        timeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                String timeInput = JOptionPane.showInputDialog(panel, "Enter current time (e.g. 1pm, 2pm, 10am):");
                if (timeInput != null && !timeInput.isEmpty()) {
                    for (TransportService ts : dm.getRepoTransportServices().getAll()) {
                        if (ts.getEntityID().equals(id)) {
                            ts.updateServiceByTime(timeInput);
                            JOptionPane.showMessageDialog(panel,
                                    "Time simulated: " + timeInput +
                                            "\nMode: " + (ts.isPeakHour() ? "PEAK HOUR" : "NORMAL HOUR") +
                                            "\nActive Route: " + ts.getActiveRouteName());
                            break;
                        }
                    }
                    refresh.run();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        // Calc Op. Cost
        costBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (TransportService ts : dm.getRepoTransportServices().getAll()) {
                    if (ts.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel,
                                "Operational Cost: $" + String.format("%.2f", ts.calculateOperationalCost()),
                                "Op. Cost", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        // View Info
        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (TransportService ts : dm.getRepoTransportServices().getAll()) {
                    if (ts.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel, ts.toString(), "Transport Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        refreshBtn.addActionListener(e -> refresh.run());
        refresh.run();

        return panel;
    }

    // ---------------------------------------------------------
    // 2. SECURITY TAB
    // ---------------------------------------------------------
    private JPanel createSecurityTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Input panel updated
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField hourlyField = new JTextField();
        JTextField patrolField = new JTextField();
        JTextField staffField = new JTextField();

        inputPanel.add(new JLabel("Service ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Service Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Hourly Rate:"));
        inputPanel.add(hourlyField);
        inputPanel.add(new JLabel("Patrol Cars:"));
        inputPanel.add(patrolField);
        inputPanel.add(new JLabel("Staff Count:"));
        inputPanel.add(staffField);
        panel.add(inputPanel, BorderLayout.NORTH);

        // Table columns updated
        String[] cols = { "ID", "Name", "Location", "Hourly Rate", "Staff", "Patrol Cars", "Emergency Mode",
                "Emergency Status", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Button layout updated with editBtn and 2-row layout modification
        JButton addBtn = new JButton("Add Security");
        JButton removeBtn = new JButton("Remove");
        JButton editBtn = new JButton("Edit");
        JButton emgBtn = new JButton("Trigger Emergency");
        JButton normalBtn = new JButton("Normalize");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");

        JPanel btnPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());
        row1.add(addBtn);
        row1.add(removeBtn);
        row1.add(editBtn);
        row1.add(emgBtn);
        row2.add(normalBtn);
        row2.add(costBtn);
        row2.add(viewBtn);
        row2.add(refreshBtn);
        btnPanel.add(row1);
        btnPanel.add(row2);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Refresh lambda logic updated
        Runnable refresh = () -> {
            model.setRowCount(0);
            for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                model.addRow(new Object[] {
                        ss.getEntityID(),
                        ss.getEntityName(),
                        ss.getLocation(),
                        String.format("%.2f", ss.getBaseHourlyRate()),
                        ss.getStaffCount(),
                        ss.getNumberOfPatrolCars(),
                        ss.isEmergencyMode() ? "ACTIVE" : "Inactive",
                        ss.getCurrentEmergencyStatus(),
                        ss.getStatus()
                });
            }
        };

        // New Add Button handler with duplicate checking
        addBtn.addActionListener(e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
                    locationField.getText().isEmpty() || hourlyField.getText().isEmpty() ||
                    patrolField.getText().isEmpty() || staffField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields.");
                return;
            }

            String enteredID = idField.getText();
            for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                if (ss.getEntityID().equals(enteredID)) {
                    JOptionPane.showMessageDialog(panel,
                            "ID \"" + enteredID + "\" already exists. Please use a unique ID.");
                    return;
                }
            }
            try {
                SecurityService ss = new SecurityService(
                        idField.getText(),
                        nameField.getText(),
                        locationField.getText(),
                        Integer.parseInt(staffField.getText()),
                        24,
                        Double.parseDouble(hourlyField.getText()),
                        Integer.parseInt(patrolField.getText()));
                dm.getRepoSecurityServices().add(ss);
                idField.setText("");
                nameField.setText("");
                locationField.setText("");
                hourlyField.setText("");
                patrolField.setText("");
                staffField.setText("");
                refresh.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Patrol Cars and Staff must be whole numbers. Hourly Rate must be a number.");
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                dm.getRepoSecurityServices().getAll().removeIf(ss -> ss.getEntityID().equals(id));
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        // Full Action Listener for the Edit Button
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                if (ss.getEntityID().equals(id)) {
                    String name = JOptionPane.showInputDialog(panel, "Name (blank to keep):", ss.getEntityName());
                    if (name != null && !name.isEmpty())
                        ss.setEntityName(name);
                    String location = JOptionPane.showInputDialog(panel, "Location (blank to keep):", ss.getLocation());
                    if (location != null && !location.isEmpty())
                        ss.setLocation(location);
                    String rate = JOptionPane.showInputDialog(panel, "Hourly Rate (blank to keep):",
                            ss.getBaseHourlyRate());
                    if (rate != null && !rate.isEmpty()) {
                        try {
                            ss.setBaseHourlyRate(Double.parseDouble(rate));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String staff = JOptionPane.showInputDialog(panel, "Staff Count (blank to keep):",
                            ss.getStaffCount());
                    if (staff != null && !staff.isEmpty()) {
                        try {
                            ss.setStaffCount(Integer.parseInt(staff));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String patrol = JOptionPane.showInputDialog(panel, "Patrol Cars (blank to keep):",
                            ss.getNumberOfPatrolCars());
                    if (patrol != null && !patrol.isEmpty()) {
                        try {
                            ss.setNumberOfPatrolCars(Integer.parseInt(patrol));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String[] statusOptions = { "Active", "Busy", "Closed" };
                    String status = (String) JOptionPane.showInputDialog(panel, "Set Status:",
                            "Status", JOptionPane.QUESTION_MESSAGE, null,
                            statusOptions, ss.getStatus());
                    if (status != null)
                        ss.setStatus(status);
                    JOptionPane.showMessageDialog(panel, "Security service updated!");
                    refresh.run();
                    break;
                }
            }
        });

        emgBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                String msg = JOptionPane.showInputDialog(panel, "Emergency Message:");
                if (msg != null && !msg.isEmpty()) {
                    for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                        if (ss.getEntityID().equals(id)) {
                            ss.handleEmergencySituation(msg);
                            JOptionPane.showMessageDialog(panel, "EMERGENCY MODE ACTIVATED!\n" + msg);
                            break;
                        }
                    }
                    refresh.run();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        normalBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                    if (ss.getEntityID().equals(id)) {
                        ss.setEmergencyMode(false);
                        ss.setCurrentEmergencyStatus("NORMAL");
                        JOptionPane.showMessageDialog(panel, "Situation normalized. Emergency mode OFF.");
                        break;
                    }
                }
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        costBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                    if (ss.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel,
                                "Operational Cost: $" + String.format("%.2f", ss.calculateOperationalCost()),
                                "Op. Cost", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (SecurityService ss : dm.getRepoSecurityServices().getAll()) {
                    if (ss.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel, ss.toString(), "Security Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a service first.");
            }
        });

        refreshBtn.addActionListener(e -> refresh.run());
        refresh.run();

        return panel;
    }

    // ---------------------------------------------------------
    // 3. HEALTH CENTER TAB (UPDATED)
    // ---------------------------------------------------------
    private JPanel createHealthTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Change 1 — Input panel completely updated
        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField staffField = new JTextField();
        JTextField hourlyField = new JTextField();
        JTextField bedsField = new JTextField();
        JTextField doctorsField = new JTextField();

        inputPanel.add(new JLabel("Center ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Center Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Staff Count:"));
        inputPanel.add(staffField);
        inputPanel.add(new JLabel("Hourly Rate:"));
        inputPanel.add(hourlyField);
        inputPanel.add(new JLabel("Available Beds:"));
        inputPanel.add(bedsField);
        inputPanel.add(new JLabel("No. of Doctors:"));
        inputPanel.add(doctorsField);
        panel.add(inputPanel, BorderLayout.NORTH);

        // Change 2 — Table columns replaced
        String[] cols = { "ID", "Name", "Location", "Staff", "Hourly Rate", "Beds", "Doctors", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Change 5a & 5b — Edit button integrated and adjusted to 2-row layout
        // modification
        JButton addBtn = new JButton("Add Health Center");
        JButton removeBtn = new JButton("Remove");
        JButton editBtn = new JButton("Edit"); // 5a
        JButton notifyBtn = new JButton("Send Notification");
        JButton costBtn = new JButton("Calc Op. Cost");
        JButton viewBtn = new JButton("View Info");
        JButton refreshBtn = new JButton("Refresh");

        JPanel btnPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());
        row1.add(addBtn);
        row1.add(removeBtn);
        row1.add(editBtn); // 5b
        row2.add(notifyBtn);
        row2.add(costBtn);
        row2.add(viewBtn);
        row2.add(refreshBtn);
        btnPanel.add(row1);
        btnPanel.add(row2);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Change 3 — Refresh lambda blocks replaced
        Runnable refresh = () -> {
            model.setRowCount(0);
            for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
                model.addRow(new Object[] {
                        hc.getEntityID(),
                        hc.getEntityName(),
                        hc.getLocation(),
                        hc.getStaffCount(),
                        String.format("%.2f", hc.getBaseHourlyRate()),
                        hc.getAvailableBeds(),
                        hc.getNoOfDoctors(),
                        hc.getStatus()
                });
            }
        };

        // Change 4 — Entire addBtn block replaced with duplicate check and text
        // clearing
        addBtn.addActionListener(e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
                    locationField.getText().isEmpty() || staffField.getText().isEmpty() ||
                    hourlyField.getText().isEmpty() || bedsField.getText().isEmpty() ||
                    doctorsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields.");
                return;
            }
            // ID duplicate check
            String enteredID = idField.getText();
            for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
                if (hc.getEntityID().equals(enteredID)) {
                    JOptionPane.showMessageDialog(panel,
                            "ID \"" + enteredID + "\" already exists. Please use a unique ID.");
                    return;
                }
            }
            try {
                HealthCenter hc = new HealthCenter(
                        idField.getText(),
                        nameField.getText(),
                        locationField.getText(),
                        Integer.parseInt(staffField.getText()),
                        24,
                        Double.parseDouble(hourlyField.getText()),
                        Integer.parseInt(bedsField.getText()),
                        Integer.parseInt(doctorsField.getText()));
                dm.getRepoHealthCenters().add(hc);
                idField.setText("");
                nameField.setText("");
                locationField.setText("");
                staffField.setText("");
                hourlyField.setText("");
                bedsField.setText("");
                doctorsField.setText("");
                refresh.run();
                JOptionPane.showMessageDialog(panel, "Health Center added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Staff, Beds and Doctors must be whole numbers. Hourly Rate must be a number.");
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                dm.getRepoHealthCenters().getAll().removeIf(hc -> hc.getEntityID().equals(id));
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Select a center first.");
            }
        });

        // Change 5c — Edit button action listener inserted after removeBtn listener
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Select a center first.");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
                if (hc.getEntityID().equals(id)) {
                    String name = JOptionPane.showInputDialog(panel, "Name (blank to keep):", hc.getEntityName());
                    if (name != null && !name.isEmpty())
                        hc.setEntityName(name);
                    String location = JOptionPane.showInputDialog(panel, "Location (blank to keep):", hc.getLocation());
                    if (location != null && !location.isEmpty())
                        hc.setLocation(location);
                    String staff = JOptionPane.showInputDialog(panel, "Staff Count (blank to keep):",
                            hc.getStaffCount());
                    if (staff != null && !staff.isEmpty()) {
                        try {
                            hc.setStaffCount(Integer.parseInt(staff));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String rate = JOptionPane.showInputDialog(panel, "Hourly Rate (blank to keep):",
                            hc.getBaseHourlyRate());
                    if (rate != null && !rate.isEmpty()) {
                        try {
                            hc.setBaseHourlyRate(Double.parseDouble(rate));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String beds = JOptionPane.showInputDialog(panel, "Available Beds (blank to keep):",
                            hc.getAvailableBeds());
                    if (beds != null && !beds.isEmpty()) {
                        try {
                            hc.setAvailableBeds(Integer.parseInt(beds));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String doctors = JOptionPane.showInputDialog(panel, "No. of Doctors (blank to keep):",
                            hc.getNoOfDoctors());
                    if (doctors != null && !doctors.isEmpty()) {
                        try {
                            hc.setNoOfDoctors(Integer.parseInt(doctors));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    String[] statusOptions = { "Active", "Busy", "Closed" };
                    String status = (String) JOptionPane.showInputDialog(panel, "Set Status:",
                            "Status", JOptionPane.QUESTION_MESSAGE, null,
                            statusOptions, hc.getStatus());
                    if (status != null)
                        hc.setStatus(status);
                    JOptionPane.showMessageDialog(panel, "Health Center updated!");
                    refresh.run();
                    break;
                }
            }
        });

        notifyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                String msg = JOptionPane.showInputDialog(panel, "Enter notification message:");
                if (msg != null && !msg.isEmpty()) {
                    for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
                        if (hc.getEntityID().equals(id)) {
                            hc.sendNotification(msg);
                            JOptionPane.showMessageDialog(panel, "Notification sent!");
                            break;
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a center first.");
            }
        });

        costBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
                    if (hc.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel,
                                "Operational Cost: $" + String.format("%.2f", hc.calculateOperationalCost()),
                                "Op. Cost", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a center first.");
            }
        });

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
                    if (hc.getEntityID().equals(id)) {
                        JOptionPane.showMessageDialog(panel, hc.toString(), "Health Center Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a center first.");
            }
        });

        refreshBtn.addActionListener(e -> refresh.run());
        refresh.run();

        return panel;
    }
}