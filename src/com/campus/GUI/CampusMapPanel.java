package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.facility.*;
import com.campus.Serviceunit.*;
import com.campus.academicunit.*;
import javax.swing.*;
import java.awt.*;

public class CampusMapPanel extends JPanel {

    // FIELDS
    private DataManager dm;

    // CONSTRUCTOR
    public CampusMapPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout());

        // 1. Top Panel (Title & Refresh)
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel title = new JLabel("Campus Map (Live Status Overview)");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title);

        JButton refreshBtn = new JButton("Refresh Map");
        refreshBtn.addActionListener(e -> repaint());
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);

        // 2. Map Area (Handled by paintComponent)
        // We don't add anything to CENTER, we just draw on it.

        // 3. Bottom Panel (Legend)
        JPanel legendPanel = new JPanel(new FlowLayout());
        legendPanel.add(createLegendBox(Color.GREEN, "Open/Normal"));
        legendPanel.add(createLegendBox(Color.YELLOW, "Busy/Emergency/Peak"));
        legendPanel.add(createLegendBox(Color.RED, "Closed"));
        add(legendPanel, BorderLayout.SOUTH);
    }

    // Creates a small colored box with text for the legend
    private JPanel createLegendBox(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout());
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        p.add(colorBox);
        p.add(new JLabel(text));
        return p;
    }

    // Helper to determine color based on status
    private Color determineColor(String status) {
        if (status == null) return Color.GRAY;
        status = status.toLowerCase();
        if (status.equals("active") || status.equals("open") || status.equals("normal")) {
            return Color.GREEN;
        } else if (status.equals("busy") || status.equals("under maintenance")) {
            return Color.YELLOW;
        } else if (status.equals("closed") || status.equals("inactive")) {
            return Color.RED;
        }
        return Color.GRAY; // Default for unknown status
    }

    // OTHER METHODS

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int startX = 50;
        int startY = 50;
        int boxWidth = 130;
        int boxHeight = 80;
        int gap = 20;

        int x = startX;
        int y = startY;

        boolean hasItems = false;

        // Draw Hostels
        for (Hostel h : dm.getRepoHostels().getAll()) {
            Color color = determineColor(h.getStatus());
            if (h.isFull())
                color = Color.YELLOW; // Override if full
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Hostel", h.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Cafeterias
        for (Cafeteria c : dm.getRepoCafeterias().getAll()) {
            Color color = determineColor(c.getStatus());
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Cafeteria", c.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Security Services
        for (SecurityService s : dm.getRepoSecurityServices().getAll()) {
            Color color = determineColor(s.getStatus());
            if (s.isEmergencyMode()) 
                color = Color.YELLOW; // Override if emergency
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Security", s.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Transport Services
        for (TransportService t : dm.getRepoTransportServices().getAll()) {
            Color color = determineColor(t.getStatus());
            if (t.isPeakHour()) 
                color = Color.YELLOW; // Override if peak hour
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Transport", t.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Health Centers
        for (HealthCenter hc : dm.getRepoHealthCenters().getAll()) {
            Color color = determineColor(hc.getStatus());
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Health", hc.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Departments
        for (Department d : dm.getRepoDepartments().getAll()) {
            Color color = determineColor(d.getStatus());
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Department", d.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Classrooms
        for (Classroom c : dm.getRepoClassrooms().getAll()) {
            Color color = determineColor(c.getStatus());
            if (!c.isAvailable()) color = Color.RED;
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Classroom", c.getClassNumber(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Labs
        for (Lab l : dm.getRepoLabs().getAll()) {
            Color color = determineColor(l.getStatus());
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Lab", l.getLabNumber(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // Draw Libraries
        for (Library lib : dm.getRepoLibraries().getAll()) {
            Color color = determineColor(lib.getStatus());
            drawBuilding(g2, x, y, boxWidth, boxHeight, "Library", lib.getEntityID(),
                    color);
            int[] pos = advanceX(x, y, boxWidth, gap);
            x = pos[0];
            y = pos[1];
            hasItems = true;
        }

        // If nothing is drawn, show a message
        if (!hasItems) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.ITALIC, 16));
            g2.drawString("No facilities or services added yet. Add them in their panels to see them on the map!", startX, startY + 40);
        }
    }

    // Helper to calculate the next X and Y position for the grid
    private int[] advanceX(int currentX, int currentY, int boxWidth, int gap) {
        currentX += boxWidth + gap;
        if (currentX > getWidth() - boxWidth - gap) {
            currentX = 50;
            currentY += 80 + gap;
        }
        return new int[] { currentX, currentY };
    }

    // Helper to draw the actual colored rectangle for a building
    private void drawBuilding(Graphics2D g2, int x, int y, int width, int height,
            String type, String name, Color color) {
        // Drop shadow
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(x + 4, y + 4, width, height, 15, 15);

        // Main Box
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 15, 15);

        // Border
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 15, 15);

        // Text
        g2.setColor(Color.BLACK);

        // Type Label
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        FontMetrics fm1 = g2.getFontMetrics();
        g2.drawString(type, x + (width - fm1.stringWidth(type)) / 2, y + 35);

        // Name Label
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        FontMetrics fm2 = g2.getFontMetrics();

        // Truncate name if it's too long to fit in the box
        String displayName = name;
        if (fm2.stringWidth(name) > width - 10) {
            displayName = name.substring(0, Math.min(name.length(), 15)) + "...";
        }
        g2.drawString(displayName, x + (width - fm2.stringWidth(displayName)) / 2, y + 55);
    }
}
