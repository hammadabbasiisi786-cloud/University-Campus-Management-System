package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;
import com.campus.Interfaces.Reportable;

import java.util.ArrayList;

public class Library extends Facility implements Reportable {

    // FIELDS
    protected ArrayList<Book> books = new ArrayList<>();
    protected String timings;
    protected double costPerBook;
    private CampusRepository<Book> repo = new CampusRepository<>();

    // CONSTRUCTORS
    public Library() {
        super();
    }

    public Library(ArrayList<Book> books) {
        super();
        this.books = books;
    }

    public Library(String entityID, String entityName, String location, String status, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, ArrayList<Book> books, String timings, double costPerBook) {
        super(entityID, entityName, location, status, maintenanceCost, usageFrequency, capacity, isOpen);
        this.books = books;
        this.timings = timings;
        this.costPerBook = costPerBook;
    }

    // SETTERS
    public void setBooks(ArrayList<Book> books) { this.books = books; }
    public void setTimings(String timings) { this.timings = timings; }
    public void setCostPerBook(double costPerBook) { this.costPerBook = costPerBook; }

    // GETTERS
    public ArrayList<Book> getBooks() { return books; }
    public String getTimings() { return timings; }
    public double getCostPerBook() { return costPerBook; }

    // OTHER METHODS

    // Adds a book to the library's repository
    public void addBook(Book book) {
        repo.add(book);
    }

    // Removes a book from the library's repository
    public void removeBook(Book book) {
        repo.remove(book);
    }

    // Searches for a book in the library's repository
    public void searchBook(Book book) {
        repo.search(book);
    }

    // Issues a book by ISBN if it is available, and increments usage frequency
    public boolean issueBook(String bookID) {
        for (Book b : books) {
            if (b.getISBN().equals(bookID) && b.getAvailability()) {
                b.issueBook();
                usageFrequency++;
                System.out.println("Book issued: " + b.getTitle());
                return true;
            }
        }
        System.out.println("Book not available: " + bookID);
        return false;
    }

    // Returns a list of all books currently available for issuing
    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> available = new ArrayList<>();
        for (Book b : books) {
            if (b.getAvailability()) {
                available.add(b);
            }
        }
        return available;
    }

    // Returns the total number of books in the library
    public int getBooksCount() {
        return books.size();
    }

    // Calculates operational cost by adding parent cost with per-book cost across all books
    @Override
    public double calculateOperationalCost() {
        return super.calculateOperationalCost() + (getBooksCount() * this.costPerBook);
    }

    // Prints a full formatted report of the library's books, availability, and financials
    @Override
    public void generateReport() {
        int issuedBooks = books.size() - getAvailableBooks().size();
        System.out.println("======== Library Report ========");
        System.out.println("Total Books     : " + books.size());
        System.out.println("Available Books : " + getAvailableBooks().size());
        System.out.println("Issued Books    : " + issuedBooks);
        System.out.println("Status          : " + getStatus());
        System.out.println("Timings         : " + timings);
        System.out.println("Visitors        : " + usageFrequency);
        System.out.printf( "Total Cost      : %.2f%n", calculateOperationalCost());
        System.out.println("================================");
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Timings         : %s\n" +
                        "  Cost Per Book   : %.2f\n" +
                        "  Total Books     : %d\n" +
                        "  Available Books : %d",
                super.toString(),
                timings,
                costPerBook,
                books.size(),
                getAvailableBooks().size()
        );
    }
}