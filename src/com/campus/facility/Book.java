package com.campus.facility;

import java.io.Serializable;

public class Book implements Serializable {
    protected String ISBN;
    protected String title;
    protected String author;
    protected String publisher;

    protected int quantity;
    protected boolean isAvailable;

    public Book(double costPerBook, String publisher, String author, String title, String ISBN, int quantity,boolean isAvailable) {
        this.publisher = publisher;
        this.author = author;
        this.title = title;
        this.ISBN = ISBN;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
    }

    public void issueBook()   { this.isAvailable = false; }
    public void returnBook()  { this.isAvailable = true;  }

    public boolean getAvailablity(){return isAvailable;}
    public String getISBN() {return ISBN;}
    public String getPublisher() {return publisher;}
    public String getAuthor() {return author;}
    public int getQuantity() {return quantity;}
    public String getTitle() {return title;}

    public void setISBN(String ISBN) {this.ISBN = ISBN;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
    public void setAuthor(String author) {this.author = author;}
    public void setTitle(String title) {this.title = title;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return "------Book------" +
                "\nISBN='" + ISBN +
                "\ntitle='" + title +
                "\nauthor='" + author +
                "\npublisher='" + publisher +
                "\nquantity=" + quantity +
                "\nisAvailable=" + isAvailable ;
    }
}
