package com.campus.backend.facility;

import com.campus.backend.person.Student;

import java.io.Serializable;

public class IssuedRecord implements Serializable {

    // FIELDS
    private Book book;
    private Student student;

    // CONSTRUCTOR
    public IssuedRecord(Book book, Student student) {
        this.book = book;
        this.student = student;
    }

    // GETTERS
    public Book getBook() {
        return book;
    }

    public Student getStudent() {
        return student;
    }

    // TO-STRING
    @Override
    public String toString() {
        return String.format("=== Issued Record ===\n" + "  Student ID : %s\n" + "  Student    : %s\n" + "  Book ISBN  : %s\n" + "  Book Title : %s", student.getStudentID(), student.getName(), book.getISBN(), book.getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssuedRecord that = (IssuedRecord) o;
        return book.getISBN().equals(that.book.getISBN()) && student.getStudentID().equals(that.student.getStudentID());
    }
}
