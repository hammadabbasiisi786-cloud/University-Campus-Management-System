package com.campus.academicunit;

public class Assignment {

    // FIELDS
    private int assignmentNumber;
    private String title;
    private String issueDate;
    private String dueDate;
    private int totalMarks;
    private double obtainedMarks;

    // CONSTRUCTORS
    public Assignment() {}

    public Assignment(int assignmentNumber, String title, String issueDate, String dueDate, int totalMarks, double obtainedMarks) {
        setAssignmentNumber(assignmentNumber);
        setTitle(title);
        setIssueDate(issueDate);
        setDueDate(dueDate);
        setTotalMarks(totalMarks);
        setObtainedMarks(obtainedMarks);
    }

    // SETTERS

    // Assignment number must be greater than 0 to be valid
    public void setAssignmentNumber(int assignmentNumber) {
        if (assignmentNumber > 0) {
            this.assignmentNumber = assignmentNumber;
        } else {
            System.out.println("Please enter a valid assignment number");
        }
    }

    // Title must not be null or empty
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            System.out.println("Please enter a valid title");
        } else {
            this.title = title;
        }
    }

    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    // Total marks must be greater than 0 to be valid
    public void setTotalMarks(int totalMarks) {
        if (totalMarks > 0) {
            this.totalMarks = totalMarks;
        } else {
            System.out.println("Please enter a valid number for total marks");
        }
    }

    // Obtained marks must be non-negative and must not exceed total marks
    public void setObtainedMarks(double obtainedMarks) {
        if (obtainedMarks >= 0 && obtainedMarks <= totalMarks) {
            this.obtainedMarks = obtainedMarks;
        } else {
            System.out.println("Please enter a valid number for obtained marks");
        }
    }

    // GETTERS
    public int getAssignmentNumber() { return assignmentNumber; }
    public String getTitle() { return title; }
    public String getIssueDate() { return issueDate; }
    public String getDueDate() { return dueDate; }
    public int getTotalMarks() { return totalMarks; }
    public double getObtainedMarks() { return obtainedMarks; }

    // TO-STRING
    @Override
    public String toString() {
        return String.format(
                "=== Assignment ===\n" +
                        "  Number         : %d\n" +
                        "  Title          : %s\n" +
                        "  Issue Date     : %s\n" +
                        "  Due Date       : %s\n" +
                        "  Total Marks    : %d\n" +
                        "  Obtained Marks : %.2f",
                assignmentNumber,
                title,
                issueDate,
                dueDate,
                totalMarks,
                obtainedMarks
        );
    }
}