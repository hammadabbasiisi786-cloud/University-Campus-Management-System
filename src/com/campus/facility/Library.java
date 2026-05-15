package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;
import com.campus.Interfaces.Reportable;

import java.util.ArrayList;

public class Library extends Facility implements Reportable {
    protected ArrayList<Book> books;
    protected String timings;
    protected double costPerBook;
    CampusRepository<Book> repo = new CampusRepository<>();

    public Library(ArrayList<Book> books) {this.books = books;
    }

    public Library(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, ArrayList<Book> books, String timings, double costPerBook) {
        super(entityID, entityName, location, status, maintenanceCost, usageFrequency, capacity, isOpen);
        this.timings = timings;
        this.costPerBook = costPerBook;
        this.books =new ArrayList<>();
    }

    @Override
    public double calculateOperationalCost() {
        return super.calculateOperationalCost() + (getBooksCount() * this.costPerBook);
    }

    public void addBook(Book Book){
        repo.add(Book);
    }
    public void removeBook(Book book){
        repo.remove(book);
    }
    public void searchBook(Book book){
       repo.search(book);
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
    public double getBooksCount(){
        return books.size();
    }
    public ArrayList<Book> getBooks(){
        return books;
    }
    public void generateReport(){
        double issued = books.size() - getAvailableBooks().size();
        System.out.println("======Library Report ========" +
                "\nTotal Available Books: " + books.size() +
                "\nAvailable Books: " + getAvailableBooks()+
                "\nIssued Books: " + issued +
                "\nStatus: " + getStatus() +
                "\nTimings: " + timings +
                "\nVisitors: " + usageFrequency +
                "\nCost: " + calculateOperationalCost());
    }

    @Override
    public String toString() {
        return "----------Library--------"
                + "\n" + super.toString()
                + "\nStatus =" + getStatus()
                + "\ncapacity=" + capacity
                + "\nmaintenanceCost=" + maintenanceCost
                + "\nusageFrequency=" + usageFrequency;
    }
}
