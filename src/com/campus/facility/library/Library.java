package com.campus.facility.library;

import com.campus.Person.*;
import com.campus.core.Facility;
import com.campus.interfaces.Reportable;

import java.util.ArrayList;

public class Library extends Facility implements Reportable {
    protected ArrayList<Book> books;
    protected String timings;

    public Library(double usageFrequency, double maintenanceCost, int capacity, boolean isOpen,String timings) {
        super(usageFrequency, maintenanceCost, capacity, isOpen);
        this.timings = timings;
        this.books = new ArrayList<>();
    }

    public Library(ArrayList<Book> books) {this.books = books;
    }

    @Override
    public double calculateOperationalCost() {
        //formula needs to be corrected
        return super.calculateOperationalCost() + (books.size() +usageFrequency);
    }

    public void addBook(Book newBook){
        if(newBook != null && !(books.contains(newBook))){
            books.add(newBook);
            System.out.println("Book added Successfully");
        }else{
            System.out.println("Book added Failed");
        }
    }
    public void removeBook(Book book){
        if(books.contains(book)){
            books.remove(book);
            System.out.println("Book removed Successfully");
        }else{
            System.out.println("Book not found");
        }
    }
    public void searchBook(String title){
        for(Book book : books){
            if(book.getTitle().equalsIgnoreCase(title)){
                System.out.println("Book found Successfully");
            }else {
                System.out.println("Book not found");
            }
        }
    }
    public boolean issueBook(String bookID) {
        for (Book b : books) {
            if (b.getISBN().equals(bookID) && b.getAvailablity()) {
                b.issueBook();
                usageFrequency++;
                System.out.println("Book issued: " + b.getTitle());
                return true;
            }
        }
        System.out.println("Book not available: " + bookID);
        return false;
    }
    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> available = new ArrayList<>();
        for (Book b : books) {
            if (b.getAvailablity()){
                available.add(b);
            }
        }
        return available;
    }
    public String generateReport(){
        double issued = books.size() - getAvailableBooks().size();
        return "======Library Report ========" +
                "Total Available Books: " + books.size() +
                "Available Books: " + getAvailableBooks()+
                "Issued Books: " + issued +
                "Status: " + getStatus() +
                "Timings: " + timings +
                "Visitors: " + usageFrequency +
                "Cost: " + calculateOperationalCost();
    }

    @Override
    public String toString() {
        return super.toString() +
                "Status =" + getStatus() +
                ", capacity=" + capacity +
                ", maintenanceCost=" + maintenanceCost +
                ", usageFrequency=" + usageFrequency;
    }

}
