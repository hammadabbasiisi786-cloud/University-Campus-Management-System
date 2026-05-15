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
        setISBN(ISBN);
        setTitle(title);
        setAuthor(author);
        setPublisher(publisher);
        setQuantity(quantity);
        setAvailability(isAvailable);
    }

    // SETTERS
    public void setISBN(String ISBN) {
        if (ISBN == null || ISBN.isEmpty()) {
            System.out.println("Invalid ISBN Entered!!!!");
        } else {
            this.ISBN = ISBN;
        }
    }

    public void setPublisher(String publisher) {
        if (publisher == null || publisher.isEmpty()) {
            System.out.println("Invalid Publisher Entered!!!!");
        } else {
            this.publisher = publisher;
        }
    }

    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            System.out.println("Invalid Author Entered!!!!");
        } else {
            this.author = author;
        }
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            System.out.println("Invalid Title Entered!!!!");
        } else {
            this.title = title;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative");
        } else {
            this.quantity = quantity;
        }
    }

    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // OTHER METHODS

    // Marks this book as unavailable when it is issued to a student
    public void issueBook() { this.isAvailable = false; }

    // Marks this book as available again when it is returned by a student
    public void returnBook() { this.isAvailable = true; }

    // GETTERS
    public boolean getAvailablity(){return isAvailable;}
    public String getISBN() {return ISBN;}
    public String getPublisher() {return publisher;}
    public String getAuthor() {return author;}
    public int getQuantity() {return quantity;}
    public String getTitle() {return title;}

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