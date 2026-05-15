package com.campus.facility;

import java.io.Serializable;

public class Book implements Serializable {

    // FIELDS
    protected String ISBN;
    protected String title;
    protected String author;
    protected String publisher;
    protected int quantity;
    protected boolean isAvailable;

    // CONSTRUCTORS
    public Book() {}

    public Book(String ISBN, String title, String author, String publisher, int quantity, boolean isAvailable) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
    }

    // SETTERS
    public void setISBN(String ISBN) { this.ISBN = ISBN; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    // GETTERS
    public String getISBN() { return ISBN; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getQuantity() { return quantity; }
    public boolean getAvailability() { return isAvailable; }

    // OTHER METHODS

    // Marks this book as unavailable when it is issued to a student
    public void issueBook() { this.isAvailable = false; }

    // Marks this book as available again when it is returned by a student
    public void returnBook() { this.isAvailable = true; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Book ===\n" +
                        "  ISBN      : %s\n" +
                        "  Title     : %s\n" +
                        "  Author    : %s\n" +
                        "  Publisher : %s\n" +
                        "  Quantity  : %d\n" +
                        "  Available : %s",
                ISBN,
                title,
                author,
                publisher,
                quantity,
                (isAvailable ? "Yes" : "No")
        );
    }
}