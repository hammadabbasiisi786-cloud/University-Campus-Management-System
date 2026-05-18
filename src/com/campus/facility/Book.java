package com.campus.facility;

import java.io.Serializable;

public class Book implements Serializable {

    // FIELDS
    protected String ISBN;
    protected String title;
    protected String author;

    // CONSTRUCTORS
    public Book() {
    }

    public Book(String ISBN, String title, String author) {
        setISBN(ISBN);
        setTitle(title);
        setAuthor(author);
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

    // GETTERS
    public String getISBN() {
        return ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Book ===\n" +
                        "  ISBN   : %s\n" +
                        "  Title  : %s\n" +
                        "  Author : %s",
                ISBN,
                title,
                author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Book book = (Book) o;
        return ISBN != null && ISBN.equals(book.ISBN);
    }
}