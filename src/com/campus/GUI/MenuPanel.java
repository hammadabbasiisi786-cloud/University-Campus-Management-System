package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.facility.Cafeteria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuPanel extends JPanel {

    // FIELDS
    private DataManager dm;
    private JComboBox<String> cafeteriaBox;
    private JTextField menuItemField;
    
    private JButton addButton;
    private JButton removeButton;
    private JButton refreshButton;
    
    private JTable menuTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public MenuPanel(DataManager dm) {
        this.dm = dm;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        inputPanel.add(new JLabel("Select Cafeteria:"));
        cafeteriaBox = new JComboBox<>();
        inputPanel.add(cafeteriaBox);
        
        inputPanel.add(new JLabel("Menu Item Name:"));
        menuItemField = new JTextField();
        inputPanel.add(menuItemField);
        
        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table)
        String[] columns = {"Cafeteria", "Menu Item"};
        tableModel = new DefaultTableModel(columns, 0);
        menuTable = new JTable(tableModel);
        add(new JScrollPane(menuTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        addButton = new JButton("Add Menu Item");
        removeButton = new JButton("Remove Selected");
        refreshButton = new JButton("Refresh List");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 4. Connect Buttons to Methods
        addButton.addActionListener(e -> addMenuItem());
        removeButton.addActionListener(e -> removeMenuItem());
        refreshButton.addActionListener(e -> {
            refreshComboBox();
            refreshTable();
        });
        
        // Add listener to combobox to filter table when cafeteria changes
        cafeteriaBox.addActionListener(e -> refreshTable());

        // 5. Initial Load
        refreshComboBox();
        refreshTable();
    }

    // OTHER METHODS
    
    private void addMenuItem() {
        String cSelection = (String) cafeteriaBox.getSelectedItem();
        if (cSelection == null || cSelection.equals("None")) {
            JOptionPane.showMessageDialog(this, "Please select a cafeteria first.");
            return;
        }
        
        String menuItem = menuItemField.getText();
        if (menuItem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a menu item name.");
            return;
        }
        
        Cafeteria selectedCafeteria = getSelectedCafeteria();
        if (selectedCafeteria != null) {
            // Check if item already exists
            for (String item : selectedCafeteria.getMenu()) {
                if (item.equalsIgnoreCase(menuItem)) {
                    JOptionPane.showMessageDialog(this, "This item already exists in the menu!");
                    return;
                }
            }
            
            selectedCafeteria.addMenu(menuItem);
            JOptionPane.showMessageDialog(this, "Menu Item Added Successfully!");
            menuItemField.setText("");
            refreshTable();
        }
    }

    private void removeMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow >= 0) {
            Cafeteria cafeteria = getSelectedCafeteria();
            
            // If viewing all, we need to find which cafeteria this item belongs to based on the table row
            if (cafeteria == null) {
                String cafeteriaName = (String) tableModel.getValueAt(selectedRow, 0);
                for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                    if (c.getEntityName().equals(cafeteriaName)) {
                        cafeteria = c;
                        break;
                    }
                }
            }
            
            if (cafeteria == null) return;
            
            String item = (String) tableModel.getValueAt(selectedRow, 1);
            cafeteria.removeMenu(item);
            
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please click on a menu item in the table to remove it.");
        }
    }
    
    // Helper to get the currently selected cafeteria from the dropdown
    private Cafeteria getSelectedCafeteria() {
        String cSelection = (String) cafeteriaBox.getSelectedItem();
        if (cSelection != null && !cSelection.equals("None")) {
            String cafeteriaId = cSelection.substring(cSelection.indexOf("(") + 1, cSelection.indexOf(")"));
            for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                if (c.getEntityID().equals(cafeteriaId)) {
                    return c;
                }
            }
        }
        return null;
    }

    private void refreshComboBox() {
        // Suppress action events during reload
        if (cafeteriaBox.getActionListeners().length > 0) {
            cafeteriaBox.removeActionListener(cafeteriaBox.getActionListeners()[0]);
        }
        
        cafeteriaBox.removeAllItems();
        cafeteriaBox.addItem("None");
        for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
            cafeteriaBox.addItem(c.getEntityName() + " (" + c.getEntityID() + ")");
        }
        
        // Re-add listener
        cafeteriaBox.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        
        Cafeteria selectedCafeteria = getSelectedCafeteria();
        if (selectedCafeteria != null) {
            for (String item : selectedCafeteria.getMenu()) {
                tableModel.addRow(new Object[]{
                        selectedCafeteria.getEntityName(),
                        item
                });
            }
        } else {
            // Show all menu items if "None" is selected
            for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
                for (String item : c.getMenu()) {
                    tableModel.addRow(new Object[]{
                            c.getEntityName(),
                            item
                    });
                }
            }
        }
    }
}
