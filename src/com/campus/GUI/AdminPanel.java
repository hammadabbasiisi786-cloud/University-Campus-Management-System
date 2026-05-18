package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.Person.Admin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {

    private DataManager dm;
    private JTable adminTable;
    private DefaultTableModel tableModel;
    private JButton viewInfoButton;
    private JButton sendNotificationButton;

    public AdminPanel(DataManager dm) {
        this.dm = dm;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Center Panel (Table)
        String[] columns = {"Admin ID", "Name", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        adminTable = new JTable(tableModel);
        add(new JScrollPane(adminTable), BorderLayout.CENTER);

        // 2. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        viewInfoButton = new JButton("View Info");
        sendNotificationButton = new JButton("Send Notification");

        buttonPanel.add(viewInfoButton);
        buttonPanel.add(sendNotificationButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 3. Connect Buttons to Methods
        viewInfoButton.addActionListener(e -> viewInfo());
        sendNotificationButton.addActionListener(e -> sendNotification());

        // 4. Initial Load
        refreshTable();
    }

    private void viewInfo() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select the admin first.");
            return;
        }

        Object user = dm.getLoginManager().getLoggedInUser();
        if (user instanceof Admin) {
            Admin loggedInAdmin = (Admin) user;
            JOptionPane.showMessageDialog(this, loggedInAdmin.toString(), "Admin Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sendNotification() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select the admin first.");
            return;
        }

        String message = JOptionPane.showInputDialog(this, "Enter notification message:");
        if (message != null && !message.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message notification sent");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        
        if ("ADMIN".equals(dm.getLoginManager().getLoggedInRole())) {
            Object user = dm.getLoginManager().getLoggedInUser();
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                tableModel.addRow(new Object[]{
                        admin.getAdminID(),
                        admin.getName(),
                        admin.getRole()
                });
            }
        }
    }
}
