package com.campus.facility.library;

import com.campus.Person.*;
import com.campus.core.Facility;

import java.util.ArrayList;

public class Library extends Facility {
    protected ArrayList<Book> books;

    public Library(double usageFrequency, double maintenanceCost, int capacity, boolean isOpen) {
        super(usageFrequency, maintenanceCost, capacity, isOpen);
        this.books = new ArrayList<>();
    }

    public Library(ArrayList<Book> books) {
        this.books = books;
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
}
