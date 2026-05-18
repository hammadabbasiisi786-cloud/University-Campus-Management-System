package com.campus.GUI;

import com.campus.FileIO.DataManager;
import com.campus.facility.Book;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookPanel extends JPanel {

    // FIELDS
    private DataManager dm;

    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;

    private JButton addButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton refreshButton;

    private JTable bookTable;
    private DefaultTableModel tableModel;

    // CONSTRUCTOR
    public BookPanel(DataManager dm) {
        this.dm = dm;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Panel (Inputs)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        inputPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        inputPanel.add(isbnField);

        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        inputPanel.add(authorField);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel (Table)
        String[] columns = { "ISBN", "Title", "Author" };
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // 3. Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add Book");
        removeButton = new JButton("Remove Selected");
        editButton = new JButton("Edit Selected");
        viewButton = new JButton("View Info");
        refreshButton = new JButton("Refresh List");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 4. Connect Buttons to Methods
        addButton.addActionListener(e -> addBook());
        removeButton.addActionListener(e -> removeBook());
        editButton.addActionListener(e -> editBook());
        viewButton.addActionListener(e -> viewBook());
        refreshButton.addActionListener(e -> refreshTable());

        // 5. Initial Load
        refreshTable();
    }

    // OTHER METHODS

    private void addBook() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Check if ISBN already exists
        for (Book b : dm.getRepoBooks().getAll()) {
            if (b.getISBN().equals(isbn)) {
                JOptionPane.showMessageDialog(this, "A book with this ISBN already exists.");
                return;
            }
        }

        Book newBook = new Book(isbn, title, author);
        dm.getRepoBooks().add(newBook);

        JOptionPane.showMessageDialog(this, "Book Added Successfully!");

        isbnField.setText("");
        titleField.setText("");
        authorField.setText("");

        refreshTable();
    }

    private void removeBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            String isbn = (String) tableModel.getValueAt(selectedRow, 0);

            for (Book b : dm.getRepoBooks().getAll()) {
                if (b.getISBN().equals(isbn)) {
                    dm.getRepoBooks().remove(b);
                    break;
                }
            }
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please click on a book in the table to remove it.");
        }
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a book first.");
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        Book target = null;
        for (Book b : dm.getRepoBooks().getAll()) {
            if (b.getISBN().equals(isbn)) {
                target = b;
                break;
            }
        }
        if (target == null)
            return;

        String newTitle = JOptionPane.showInputDialog(this, "Enter new title (leave blank to keep):",
                target.getTitle());
        if (newTitle != null && !newTitle.isEmpty()) {
            target.setTitle(newTitle);
        }

        String newAuthor = JOptionPane.showInputDialog(this, "Enter new author (leave blank to keep):",
                target.getAuthor());
        if (newAuthor != null && !newAuthor.isEmpty()) {
            target.setAuthor(newAuthor);
        }

        JOptionPane.showMessageDialog(this, "Book updated!");
        refreshTable();
    }

    private void viewBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            String isbn = (String) tableModel.getValueAt(selectedRow, 0);
            for (Book b : dm.getRepoBooks().getAll()) {
                if (b.getISBN().equals(isbn)) {
                    JOptionPane.showMessageDialog(this, b.toString(), "Book Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a book first.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Book b : dm.getRepoBooks().getAll()) {
            tableModel.addRow(new Object[] {
                    b.getISBN(),
                    b.getTitle(),
                    b.getAuthor()
            });
        }
    }
}