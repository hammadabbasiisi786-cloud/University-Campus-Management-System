package com.campus.facility;

import com.campus.CampusRepository;
import com.campus.core.Facility;
import com.campus.Interfaces.Reportable;
import com.campus.Person.Student;

import java.util.ArrayList;

public class Library extends Facility implements Reportable {

    // FIELDS
    private int libraryUsage;
    private String timings;
    private double costPerBook;
    private CampusRepository<Book> repoBook       = new CampusRepository<>();
    private CampusRepository<IssuedRecord> repoIssuedRecords = new CampusRepository<>();

    // CONSTRUCTORS
    public Library() {
        super();
    }

    public Library(String entityID, String entityName, String location, String status, double maintenanceCost, int capacity, String timings, double costPerBook) {
        super(entityID, entityName, location, status, maintenanceCost, capacity);
        setTimings(timings);
        setCostPerBook(costPerBook);
    }

    // SETTERS
    public void setTimings(String timings) {
        if (timings == null || timings.isEmpty()) {
            System.out.println("Invalid timings entered");
        } else {
            this.timings = timings;
        }
    }

    public void setCostPerBook(double costPerBook) {
        if (costPerBook < 0) {
            System.out.println("Cost per book cannot be negative");
        } else {
            this.costPerBook = costPerBook;
        }
    }

    // GETTERS
    public String getTimings()       { return timings; }
    public double getCostPerBook()   { return costPerBook; }
    public int getLibraryUsage()     { return libraryUsage; }

    // OTHER METHODS

    // Adds a book to the library's repository
    public void addBook(Book book) {
        repoBook.add(book);
    }

    // Removes a book from the library's repository
    public void removeBook(Book book) {
        repoBook.remove(book);
    }

    // Searches for a book in the library's repository by ISBN
    public void searchBook(String ISBN) {
        for (Book b : repoBook.getAll()) {
            if (b.getISBN().equals(ISBN)) {
                System.out.println("Found: " + b.getTitle());
                return;
            }
        }
        System.out.println("Book not found: " + ISBN);
    }

    // Issues a book by ISBN to a student — records the pair, increments usage counters
    public boolean issueBook(String ISBN, Student student) {
        for (Book b : repoBook.getAll()) {
            if (ISBN.equals(b.getISBN()) && b.getAvailability()) {
                b.issueBook();
                repoIssuedRecords.add(new IssuedRecord(b, student));
                libraryUsage++;
                incrementFacilityUsage();
                System.out.println("Book issued: " + b.getTitle() +
                        " | To: " + student.getName() +
                        " | Remaining copies: " + b.getQuantity() +
                        " | Library Usage: " + libraryUsage +
                        " | Total Facility Usage: " + getTotalFacilityUsage());
                return true;
            }
        }
        System.out.println("Book not available: " + ISBN);
        return false;
    }


    // Returns a book by ISBN from a specific student — removes their record, restores copy count
    public boolean returnBook(String ISBN, Student student) {
        for (IssuedRecord record : repoIssuedRecords.getAll()) {
            if (record.getBook().getISBN().equals(ISBN) &&
                    record.getStudent().getStudentID().equals(student.getStudentID())) {
                record.getBook().returnBook();
                repoIssuedRecords.remove(record);
                System.out.println("Book returned: " + record.getBook().getTitle() +
                        " | By: " + student.getName() +
                        " | Available copies: " + record.getBook().getQuantity());
                return true;
            }
        }
        System.out.println("No issued record found for this book and student.");
        return false;
    }



    // Returns a list of all books currently available for issuing
    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> available = new ArrayList<>();
        for (Book b : repoBook.getAll()) {
            if (b.getAvailability()) {
                available.add(b);
            }
        }
        return available;
    }

    // Returns all currently issued records
    public ArrayList<IssuedRecord> getIssuedRecords() {
        return repoIssuedRecords.getAll();
    }

    // Returns maintenanceCost + cost of all books as operational cost
    @Override
    public double calculateOperationalCost() {
        return maintenanceCost * libraryUsage;
    }

    @Override
    public void generateReport() {
        System.out.println("==================================================");
        System.out.println("               LIBRARY REPORT                     ");
        System.out.println("==================================================");
        System.out.printf("%-20s: %s\n", "Library Name",   entityName);
        System.out.printf("%-20s: %s\n", "Status",         getStatus());
        System.out.printf("%-20s: %s\n", "Timings",        timings);
        System.out.println("--------------------------------------------------");
        System.out.println("STATISTICS OVERVIEW:");
        System.out.printf(" - Total Books:      %d\n", repoBook.getAll().size());
        System.out.printf(" - Available Books:  %d\n", getAvailableBooks().size());
        System.out.printf(" - Issued Books:     %d\n", repoIssuedRecords.getAll().size());
        System.out.printf(" - Library Usage:    %d\n", libraryUsage);
        System.out.println("--------------------------------------------------");
        System.out.println("AVAILABLE BOOKS:");
        if (getAvailableBooks().isEmpty()) {
            System.out.println(" [No books currently available]");
        } else {
            for (Book b : getAvailableBooks()) {
                System.out.println(" □ " + b.getTitle() + " — " + b.getAuthor() +
                        " (Copies: " + b.getQuantity() + ")");
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("TOTAL OPERATIONAL COST: $%,.2f\n", calculateOperationalCost());
        System.out.println("==================================================");
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "  Timings         : %s\n" +
                        "  Cost Per Book   : %.2f\n" +
                        "  Total Books     : %d\n" +
                        "  Available Books : %d\n" +
                        "  Issued Books    : %d\n" +
                        "  Library Usage   : %d",
                super.toString(),
                timings,
                costPerBook,
                repoBook.getAll().size(),
                getAvailableBooks().size(),
                repoIssuedRecords.getAll().size(),
                libraryUsage
        );
    }
}