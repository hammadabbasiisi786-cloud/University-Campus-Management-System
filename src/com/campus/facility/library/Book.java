package com.campus.facility.library;

public class Book {
    protected String ISBN;
    protected String title;
    protected String author;
    protected String publisher;
    protected double cost;
    protected int quantity;
    protected boolean isAvailable;

    public Book(double cost, String publisher, String author, String title, String ISBN, int quantity,boolean isAvailable) {
        this.cost = cost;
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
    public double getCost() {return cost;}
    public String getPublisher() {return publisher;}
    public String getAuthor() {return author;}
    public int getQuantity() {return quantity;}
    public String getTitle() {return title;}

    public void setISBN(String ISBN) {this.ISBN = ISBN;}
    public void setCost(double cost) {this.cost = cost;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
    public void setAuthor(String author) {this.author = author;}
    public void setTitle(String title) {this.title = title;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return "------Book------" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", cost=" + cost +
                ", quantity=" + quantity +
                ", isAvailable=" + isAvailable ;
    }
}
