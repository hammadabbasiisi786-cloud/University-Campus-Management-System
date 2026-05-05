package com.campus.academicunit;

class Assignment {

    private int assignmentNumber;
    private String title;
    private String issueDate;
    private String dueDate;
    private int totalMarks;
    private double obtainedMarks;

    public Assignment() {

    }

    public Assignment(int assignmentNumber, String title, String issueDate, String dueDate, int totalMarks, double obtainedMarks) {
        setAssignmentNumber(assignmentNumber);
        setTitle(title);
        setIssueDate(issueDate);
        setDueDate(dueDate);
        setTotalMarks(totalMarks);
        setObtainedMarks(obtainedMarks);
    }

    public void setAssignmentNumber(int assignmentNumber) {
        if(assignmentNumber>1){
            this.assignmentNumber = assignmentNumber;
        }else{
            System.out.println("Please enter a valid number");
        }
    }

    public void setTitle(String title) {
        if(title == null || title.isEmpty()){
            System.out.println("Please enter a valid title");
        }else {
            this.title = title;
        }
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTotalMarks(int totalMarks) {
        if(totalMarks>0){
            this.totalMarks = totalMarks;
        }else {
            System.out.println("Please enter a valid number");
        }
    }

    public void setObtainedMarks(double obtainedMarks) {
        if(obtainedMarks<totalMarks){
            this.obtainedMarks = obtainedMarks;
        }else {
            System.out.println("Please enter a valid number");
        }
    }

    public int getAssignmentNumber() {
        return assignmentNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public double getObtainedMarks() {
        return obtainedMarks;
    }
}
