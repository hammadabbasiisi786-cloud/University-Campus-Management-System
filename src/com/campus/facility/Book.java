package com.campus.facility;

import java.io.Serializable;

public class Book implements Serializable {

    // FIELDS
    protected String ISBN;
    protected String title;
    protected String author;
    protected int quantity;
    protected boolean isAvailable;

    // CONSTRUCTORS
    public Book() {}

    public Book(String ISBN, String title, String author, int quantity) {
        setISBN(ISBN);
        setTitle(title);
        setAuthor(author);
        setQuantity(quantity);
        this.isAvailable = quantity > 0;
    }

    // SETTERS
    public void setISBN(String ISBN) {
        if (ISBN == null || ISBN.isEmpty()) {
            System.out.println("Invalid ISBN Entered!!!!");
        } else {
            this.ISBN = ISBN;
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

    // GETTERS
    public boolean getAvailability() { return isAvailable; }
    public String getISBN()          { return ISBN; }
    public String getAuthor()        { return author; }
    public int getQuantity()         { return quantity; }
    public String getTitle()         { return title; }

    // OTHER METHODS

    // Issues one copy — decrements quantity, marks unavailable when all copies are gone
    public void issueBook() {
        if (quantity > 0) {
            quantity--;
            if (quantity == 0) {
                this.isAvailable = false;
            }
        } else {
            System.out.println("No copies available to issue");
        }
    }

    // Returns one copy — increments quantity, marks available again
    public void returnBook() {
        quantity++;
        this.isAvailable = true;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Book ===\n" +
                        "  ISBN      : %s\n" +
                        "  Title     : %s\n" +
                        "  Author    : %s\n" +
                        "  Quantity  : %d\n" +
                        "  Available : %s",
                ISBN,
                title,
                author,
                quantity,
                (isAvailable ? "Yes" : "No")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return ISBN != null && ISBN.equals(book.ISBN);
    }
}