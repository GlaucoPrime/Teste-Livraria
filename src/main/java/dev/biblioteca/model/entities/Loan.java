package dev.biblioteca.model.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Loan {
    private String id;
    private User user;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private boolean isReturned;

    public Loan(User user, Book book, LocalDate borrowDate, LocalDate expectedReturnDate) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.isReturned = false;
    }

    public String getId() { return id; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
    public boolean isReturned() { return isReturned; }
    
    public void markAsReturned() { this.isReturned = true; }
}