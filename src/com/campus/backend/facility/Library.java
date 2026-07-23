package com.campus.backend.facility;

import com.campus.backend.CampusRepository;
import com.campus.backend.core.Facility;
import com.campus.backend.interfaces.Reportable;
import com.campus.backend.person.Student;

import java.util.ArrayList;

public class Library extends Facility implements Reportable {

    // FIELDS
    private int libraryUsage;
    private String timings;
    private double costPerBook;
    private CampusRepository<Book> repoBook = new CampusRepository<>();
    private CampusRepository<IssuedRecord> repoIssuedRecords = new CampusRepository<>();

    // CONSTRUCTORS
    public Library() {
        super();
    }

    public Library(String entityID, String entityName, String location, String status, double maintenanceCost,
            int capacity, String timings, double costPerBook) {
        super(entityID, entityName, location, status, maintenanceCost, capacity);
        setTimings(timings);
        setCostPerBook(costPerBook);
    }

    // GETTERS
    public String getTimings() {
        return timings;
    }

    // SETTERS
    public void setTimings(String timings) {
        if (timings == null || timings.isEmpty()) {
            System.out.println("Invalid timings entered");
        } else {
            this.timings = timings;
        }
    }

    public double getCostPerBook() {
        return costPerBook;
    }

    public void setCostPerBook(double costPerBook) {
        if (costPerBook < 0) {
            System.out.println("Cost per book cannot be negative");
        } else {
            this.costPerBook = costPerBook;
        }
    }

    public int getLibraryUsage() {
        return libraryUsage;
    }

    public ArrayList<Book> getAllBooks() {
        return repoBook.getAll();
    }

    public ArrayList<IssuedRecord> getAllIssuedRecords() {
        return repoIssuedRecords.getAll();
    }

    // OTHER METHODS

    public void addBook(Book book) {
        repoBook.add(book);
    }

    public void removeBook(Book book) {
        if (repoBook.remove(book)) {
            System.out.println("Book removed: " + book.getTitle());
        } else {
            System.out.println("Book not found.");
        }
    }

    public void searchBook(String ISBN) {
        boolean found = false;
        for (Book b : repoBook.getAll()) {
            if (b.getISBN().equals(ISBN)) {
                System.out.println("Found: " + b.getTitle() + " — " + b.getAuthor());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Book not found: " + ISBN);
        }
    }

    public boolean issueBook(String ISBN, Student student) {
        for (Book b : repoBook.getAll()) {
            if (ISBN.equals(b.getISBN())) {
                // Check this copy isn't already issued
                boolean alreadyIssued = false;
                for (IssuedRecord r : repoIssuedRecords.getAll()) {
                    if (r.getBook() == b) {
                        alreadyIssued = true;
                        break;
                    }
                }
                if (!alreadyIssued) {
                    repoIssuedRecords.add(new IssuedRecord(b, student));
                    libraryUsage++;
                    incrementFacilityUsage();
                    System.out.println(
                            "Book issued: " + b.getTitle() + " | To: " + student.getName() + " | Library Usage: "
                                    + libraryUsage + " | Total Facility Usage: " + getTotalFacilityUsage());
                    return true;
                }
            }
        }
        System.out.println("No available copy found for ISBN: " + ISBN);
        return false;
    }

    public boolean returnBook(String ISBN, Student student) {
        for (IssuedRecord record : repoIssuedRecords.getAll()) {
            if (record.getBook().getISBN().equals(ISBN)
                    && record.getStudent().getStudentID().equals(student.getStudentID())) {
                repoIssuedRecords.remove(record);
                System.out.println("Book returned: " + record.getBook().getTitle() + " | By: " + student.getName());
                return true;
            }
        }
        System.out.println("No issued record found for this book and student.");
        return false;
    }

    public ArrayList<IssuedRecord> getIssuedRecords() {
        return repoIssuedRecords.getAll();
    }

    @Override
    public double calculateOperationalCost() {
        return maintenanceCost + (repoIssuedRecords.getAll().size() * costPerBook);
    }

    @Override
    public void generateReport() {
        System.out.println("==================================================");
        System.out.println("               LIBRARY REPORT                     ");
        System.out.println("==================================================");
        System.out.printf("%-20s: %s\n", "Library Name", entityName);
        System.out.printf("%-20s: %s\n", "Status", getStatus());
        System.out.printf("%-20s: %s\n", "Timings", timings);
        System.out.println("--------------------------------------------------");
        System.out.println("STATISTICS OVERVIEW:");
        System.out.printf(" - Total Books:      %d\n", repoBook.getAll().size());
        System.out.printf(" - Issued Books:     %d\n", repoIssuedRecords.getAll().size());
        System.out.printf(" - Library Usage:    %d\n", libraryUsage);
        System.out.println("--------------------------------------------------");
        System.out.println("ISSUED BOOKS:");
        if (repoIssuedRecords.getAll().isEmpty()) {
            System.out.println(" [No books currently issued]");
        } else {
            for (IssuedRecord r : repoIssuedRecords.getAll()) {
                System.out.println(" □ " + r.getBook().getTitle() + " — " + r.getBook().getAuthor() + " | Issued to: "
                        + r.getStudent().getName() + " (" + r.getStudent().getStudentID() + ")");
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
                "%s\n" + "  Timings       : %s\n" + "  Cost Per Book : %.2f\n" + "  Total Books   : %d\n"
                        + "  Issued Books  : %d\n" + "  Library Usage : %d",
                super.toString(), timings, costPerBook, repoBook.getAll().size(), repoIssuedRecords.getAll().size(),
                libraryUsage);
    }
}
