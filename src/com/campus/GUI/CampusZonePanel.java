package com.campus.GUI;

import com.campus.CampusZone;
import com.campus.FileIO.DataManager;
import com.campus.core.Facility;
import com.campus.core.ServiceUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CampusZonePanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JTextField zoneNameField;
    private JComboBox<String> facilityBox;
    private JComboBox<String> serviceUnitBox;
    
    private JButton addZoneButton;
    private JButton removeZoneButton;
    private JButton viewInfoButton;
    private JButton addFacilityButton;
    private JButton removeFacilityButton;
    private JButton addServiceButton;
    private JButton removeServiceButton;
    private JButton refreshButton;

    private JTable zoneTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public CampusZonePanel(DataManager dm) {
        this.dm = dm;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Zone Creation)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Zone Name:"));
        zoneNameField = new JTextField(15);
        topPanel.add(zoneNameField);
        
        addZoneButton = new JButton("Add Zone");
        topPanel.add(addZoneButton);
        
        add(topPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table)
        String[] columns = {"Zone Name", "Facilities Configured", "Services Configured"};
        tableModel = new DefaultTableModel(columns, 0);
        zoneTable = new JTable(tableModel);
        add(new JScrollPane(zoneTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Configuration & Assignment)
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        // Row 1: Facility Assignment
        JPanel facilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        facilityPanel.add(new JLabel("Facility:"));
        facilityBox = new JComboBox<>();
        facilityPanel.add(facilityBox);
        addFacilityButton = new JButton("Assign Facility to Selected Zone");
        removeFacilityButton = new JButton("Remove Facility");
        facilityPanel.add(addFacilityButton);
        facilityPanel.add(removeFacilityButton);
        bottomPanel.add(facilityPanel);

        // Row 2: Service Assignment
        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        servicePanel.add(new JLabel("Service Unit:"));
        serviceUnitBox = new JComboBox<>();
        servicePanel.add(serviceUnitBox);
        addServiceButton = new JButton("Assign Service to Selected Zone");
        removeServiceButton = new JButton("Remove Service");
        servicePanel.add(addServiceButton);
        servicePanel.add(removeServiceButton);
        bottomPanel.add(servicePanel);
        
        // Row 3: Admin Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewInfoButton = new JButton("View Zone Info");
        removeZoneButton = new JButton("Remove Selected Zone");
        refreshButton = new JButton("Refresh Data");
        
        actionPanel.add(viewInfoButton);
        actionPanel.add(removeZoneButton);
        actionPanel.add(refreshButton);
        bottomPanel.add(actionPanel);
        
        add(bottomPanel, BorderLayout.SOUTH);

        // Role-based restrictions
        String role = dm.getLoginManager().getLoggedInRole();
        if ("TEACHER".equals(role) || "STUDENT".equals(role)) {
            topPanel.setVisible(false);
            facilityPanel.setVisible(false);
            servicePanel.setVisible(false);
            removeZoneButton.setVisible(false);
        }

        // 4. Connect Buttons to Methods
        addZoneButton.addActionListener(e -> addZone());
        removeZoneButton.addActionListener(e -> removeZone());
        viewInfoButton.addActionListener(e -> viewZoneInfo());
        addFacilityButton.addActionListener(e -> assignFacility());
        removeFacilityButton.addActionListener(e -> unassignFacility());
        addServiceButton.addActionListener(e -> assignService());
        removeServiceButton.addActionListener(e -> unassignService());
        refreshButton.addActionListener(e -> {
            refreshComboBoxes();
            refreshTable();
        });

        // 5. Initial Load
        refreshComboBoxes();
        refreshTable();
    }

    // METHODS

    private void addZone() {
        String name = zoneNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a zone name.");
            return;
        }

        // Check if exists
        for (CampusZone z : dm.getRepoZones().getAll()) {
            if (z.getZoneName().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this, "A zone with this name already exists.");
                return;
            }
        }

        CampusZone newZone = new CampusZone(name);
        dm.getRepoZones().add(newZone);
        JOptionPane.showMessageDialog(this, "Zone Added Successfully!");
        zoneNameField.setText("");
        refreshTable();
    }

    private void removeZone() {
        int selectedRow = zoneTable.getSelectedRow();
        if (selectedRow >= 0) {
            String zoneName = (String) tableModel.getValueAt(selectedRow, 0);
            for (CampusZone z : dm.getRepoZones().getAll()) {
                if (z.getZoneName().equals(zoneName)) {
                    dm.getRepoZones().remove(z);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a zone to remove.");
        }
    }

    private void viewZoneInfo() {
        CampusZone selectedZone = getSelectedZone();
        if (selectedZone != null) {
            JOptionPane.showMessageDialog(this, selectedZone.toString(), "Zone Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a zone first.");
        }
    }

    private void assignFacility() {
        CampusZone selectedZone = getSelectedZone();
        if (selectedZone == null) {
            JOptionPane.showMessageDialog(this, "Please select a zone from the table first.");
            return;
        }

        String selection = (String) facilityBox.getSelectedItem();
        if (selection == null || selection.equals("None")) return;

        String facilityId = selection.substring(selection.indexOf("(") + 1, selection.indexOf(")"));
        
        Facility facilityToAdd = null;
        for (Facility f : dm.getRepoLibraries().getAll()) {
            if (f.getEntityID().equals(facilityId)) facilityToAdd = f;
        }
        if (facilityToAdd == null) {
            for (Facility f : dm.getRepoCafeterias().getAll()) {
                if (f.getEntityID().equals(facilityId)) facilityToAdd = f;
            }
        }
        if (facilityToAdd == null) {
            for (Facility f : dm.getRepoHostels().getAll()) {
                if (f.getEntityID().equals(facilityId)) facilityToAdd = f;
            }
        }

        if (facilityToAdd != null) {
            if (!selectedZone.getFacilities().contains(facilityToAdd)) {
                selectedZone.addFacility(facilityToAdd);
                JOptionPane.showMessageDialog(this, "Facility assigned to " + selectedZone.getZoneName());
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "This facility is already in this zone.");
            }
        }
    }

    private void assignService() {
        CampusZone selectedZone = getSelectedZone();
        if (selectedZone == null) {
            JOptionPane.showMessageDialog(this, "Please select a zone from the table first.");
            return;
        }

        String selection = (String) serviceUnitBox.getSelectedItem();
        if (selection == null || selection.equals("None")) return;

        String serviceId = selection.substring(selection.indexOf("(") + 1, selection.indexOf(")"));
        
        ServiceUnit serviceToAdd = null;
        for (ServiceUnit s : dm.getRepoTransportServices().getAll()) {
            if (s.getEntityID().equals(serviceId)) serviceToAdd = s;
        }
        if (serviceToAdd == null) {
            for (ServiceUnit s : dm.getRepoHealthCenters().getAll()) {
                if (s.getEntityID().equals(serviceId)) serviceToAdd = s;
            }
        }
        if (serviceToAdd == null) {
            for (ServiceUnit s : dm.getRepoSecurityServices().getAll()) {
                if (s.getEntityID().equals(serviceId)) serviceToAdd = s;
            }
        }

        if (serviceToAdd != null) {
            if (!selectedZone.getServiceUnits().contains(serviceToAdd)) {
                selectedZone.addServiceUnit(serviceToAdd);
                JOptionPane.showMessageDialog(this, "Service Unit assigned to " + selectedZone.getZoneName());
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "This service unit is already in this zone.");
            }
        }
    }

    private void unassignFacility() {
        CampusZone selectedZone = getSelectedZone();
        if (selectedZone == null) {
            JOptionPane.showMessageDialog(this, "Please select a zone from the table first.");
            return;
        }

        String selection = (String) facilityBox.getSelectedItem();
        if (selection == null || selection.equals("None")) return;

        String facilityId = selection.substring(selection.indexOf("(") + 1, selection.indexOf(")"));
        
        Facility facilityToRemove = null;
        for (Facility f : selectedZone.getFacilities()) {
            if (f.getEntityID().equals(facilityId)) {
                facilityToRemove = f;
                break;
            }
        }

        if (facilityToRemove != null) {
            selectedZone.removeFacility(facilityToRemove);
            JOptionPane.showMessageDialog(this, "Facility removed from " + selectedZone.getZoneName());
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "This facility is not assigned to the selected zone.");
        }
    }

    private void unassignService() {
        CampusZone selectedZone = getSelectedZone();
        if (selectedZone == null) {
            JOptionPane.showMessageDialog(this, "Please select a zone from the table first.");
            return;
        }

        String selection = (String) serviceUnitBox.getSelectedItem();
        if (selection == null || selection.equals("None")) return;

        String serviceId = selection.substring(selection.indexOf("(") + 1, selection.indexOf(")"));
        
        ServiceUnit serviceToRemove = null;
        for (ServiceUnit s : selectedZone.getServiceUnits()) {
            if (s.getEntityID().equals(serviceId)) {
                serviceToRemove = s;
                break;
            }
        }

        if (serviceToRemove != null) {
            selectedZone.removeServiceUnit(serviceToRemove);
            JOptionPane.showMessageDialog(this, "Service Unit removed from " + selectedZone.getZoneName());
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "This service unit is not assigned to the selected zone.");
        }
    }

    private CampusZone getSelectedZone() {
        int selectedRow = zoneTable.getSelectedRow();
        if (selectedRow >= 0) {
            String zoneName = (String) tableModel.getValueAt(selectedRow, 0);
            for (CampusZone z : dm.getRepoZones().getAll()) {
                if (z.getZoneName().equals(zoneName)) {
                    return z;
                }
            }
        }
        return null;
    }

    private void refreshComboBoxes() {
        facilityBox.removeAllItems();
        facilityBox.addItem("None");
        for (Facility f : dm.getRepoLibraries().getAll()) facilityBox.addItem(f.getEntityName() + " (" + f.getEntityID() + ")");
        for (Facility f : dm.getRepoCafeterias().getAll()) facilityBox.addItem(f.getEntityName() + " (" + f.getEntityID() + ")");
        for (Facility f : dm.getRepoHostels().getAll()) facilityBox.addItem(f.getEntityName() + " (" + f.getEntityID() + ")");

        serviceUnitBox.removeAllItems();
        serviceUnitBox.addItem("None");
        for (ServiceUnit s : dm.getRepoTransportServices().getAll()) serviceUnitBox.addItem(s.getEntityName() + " (" + s.getEntityID() + ")");
        for (ServiceUnit s : dm.getRepoHealthCenters().getAll()) serviceUnitBox.addItem(s.getEntityName() + " (" + s.getEntityID() + ")");
        for (ServiceUnit s : dm.getRepoSecurityServices().getAll()) serviceUnitBox.addItem(s.getEntityName() + " (" + s.getEntityID() + ")");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (CampusZone z : dm.getRepoZones().getAll()) {
            tableModel.addRow(new Object[]{
                    z.getZoneName(),
                    z.getFacilities().size(),
                    z.getServiceUnits().size()
            });
        }
    }
}
