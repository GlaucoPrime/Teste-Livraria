package dev.biblioteca.service;

import dev.biblioteca.model.entities.Book;
import dev.biblioteca.model.entities.Loan;
import dev.biblioteca.model.entities.User;
import dev.biblioteca.model.repositories.BookRepository;
import dev.biblioteca.model.repositories.LoanRepository;
import dev.biblioteca.model.repositories.UserRepository;
import dev.biblioteca.model.exceptions.BusinessException;
import java.time.LocalDate;

public class LoanService {
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void borrowBook(String username, String isbn, LocalDate borrowDate, LocalDate returnDate) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Livro não encontrado"));

        if (book.getQuantity() <= 0) {
            throw new BusinessException("Mensagem informando indisponibilidade");
        }

        if (returnDate.isBefore(borrowDate)) {
            throw new BusinessException("Data de devolução não pode ser anterior à data do empréstimo");
        }

        book.decreaseStock();
        Loan loan = new Loan(user, book, borrowDate, returnDate);
        loanRepository.save(loan);
        System.out.println("Empréstimo realizado com sucesso.");
    }

    public void returnBook(String username, String isbn) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Livro não encontrado"));

        Loan loan = loanRepository.findActiveLoan(user, book)
                .orElseThrow(() -> new BusinessException("empréstimo não localizado"));

        loan.markAsReturned();
        book.increaseStock();
        System.out.println("O sistema deve marcar o empréstimo como 'Finalizado/Devolvido'");
    }
}