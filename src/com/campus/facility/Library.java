package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;
import com.campus.Interfaces.Reportable;

import java.io.Serializable;
import java.util.ArrayList;

public class Library extends Facility implements Reportable {

    // FIELDS
    private String timings;
    private double costPerBook;
    private CampusRepository<Book> repoBook = new CampusRepository<>();

    // CONSTRUCTORS
    public Library() {
        super();
    }

    public Library(String entityID, String entityName, String location, double maintenanceCost, double usageFrequency, int capacity, boolean isOpen, String timings, double costPerBook) {
        super(entityID, entityName, location, maintenanceCost, usageFrequency, capacity, isOpen);
        setTimings(timings);
        setCostPerBook(costPerBook);
    }

    // SETTERS
    public void setTimings(String timings) {
        if (timings != null && !timings.isEmpty()) {
            this.timings = timings;
        } else {
            System.out.println("Invalid timings entered");
        }
    }

    public void setCostPerBook(double costPerBook) {
        if (costPerBook >= 0) {
            this.costPerBook = costPerBook;
        } else {
            System.out.println("Cost per book cannot be negative");
        }
    }

    // GETTERS
    public String getTimings() {
        return timings;
    }
    public double getCostPerBook() {
        return costPerBook;
    }

    // OTHER METHODS
    @Override
    public double calculateOperationalCost() {
        return super.calculateOperationalCost() + (getBooksCount() * this.costPerBook);
    }

    // Adds a book to the library's repository
    public void addBook(Book book) {
        repoBook.add(book);
    }

    // Removes a book from the library's repository
    public void removeBook(Book book) {
        repoBook.remove(book);
    }

    // Searches for a book in the library's repository
    public void searchBook(Book book) {
        repoBook.search(book);
    }

    // Issues a book by ISBN if it is available, and increments usage frequency
    public boolean issueBook(String bookID) {
        for (Book b : repoBook.getAll()) {
            if (b.getISBN().equals(bookID) && b.getAvailablity()) {
                b.issueBook();
                setUsageFrequency(getUsageFrequency() + 1);
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
        for (Book b : repoBook.getAll()) {
            if (b.getAvailablity()) {
                available.add(b);
            }
        }
        return available;
    }

    public double getBooksCount() {
        return repoBook.getAll().size();
    }

    @Override
    public void generateReport() {
        int issuedBooks = repoBook.getAll().size() - getAvailableBooks().size();

        System.out.println("==================================================");
        System.out.println("               LIBRARY REPORT                     ");
        System.out.println("==================================================");

        System.out.printf("%-20s: %s\n", "Library Name", entityName);
        System.out.printf("%-20s: %s\n", "Status",       getStatus());
        System.out.printf("%-20s: %s\n", "Timings",      timings);
        System.out.println("--------------------------------------------------");

        System.out.println("STATISTICS OVERVIEW:");
        System.out.printf(" - Total Books:      %d\n", repoBook.getAll().size());
        System.out.printf(" - Available Books:  %d\n", getAvailableBooks().size());
        System.out.printf(" - Issued Books:     %d\n", issuedBooks);
        System.out.printf(" - Visitors:         %.0f\n", usageFrequency);
        System.out.println("--------------------------------------------------");

        System.out.println("AVAILABLE BOOKS:");
        if (getAvailableBooks().isEmpty()) {
            System.out.println(" [No books currently available]");
        } else {
            for (Book b : getAvailableBooks()) {
                System.out.println(" □ " + b.getTitle() + " — " + b.getAuthor());
            }
        }
        System.out.println("--------------------------------------------------");

        System.out.printf("TOTAL OPERATIONAL COST: $%,.2f\n", calculateOperationalCost());
        System.out.println("==================================================");
    }

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
                repoBook.getAll().size(),
                getAvailableBooks().size()
        );
    }
}