package dev.biblioteca.model.repositories;

import dev.biblioteca.model.entities.Book;
import dev.biblioteca.model.entities.Loan;
import dev.biblioteca.model.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository {
    private List<Loan> loans = new ArrayList<>();

    public void save(Loan loan) { 
        loans.add(loan); 
    }
    
    public Optional<Loan> findActiveLoan(User user, Book book) {
        return loans.stream()
                .filter(l -> l.getUser().equals(user) && l.getBook().equals(book) && !l.isReturned())
                .findFirst();
    }
}