package dev.biblioteca.service;

import dev.biblioteca.model.entities.Book;
import dev.biblioteca.model.repositories.BookRepository;
import dev.biblioteca.model.exceptions.ValidationException;
import java.util.List;

public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) { 
        this.bookRepository = bookRepository; 
    }

    public void registerBook(String title, String author, String isbn, String quantityStr) {
        if (title.isBlank() || author.isBlank() || isbn.isBlank() || quantityStr.isBlank()) {
            throw new ValidationException("Mensagem de erro informando campo obrigatório");
        }
        
        if (!isbn.matches("^[0-9-]+$")) {
            throw new ValidationException("Insira um ISBN numérico válido");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new ValidationException("O campo 'Quantidade' apenas aceita valores numéricos inteiros.");
        }

        if (quantity < 0) {
            throw new ValidationException("Quantidade não pode ser negativa.");
        }

        if (bookRepository.findByIsbn(isbn).isPresent()) {
            throw new ValidationException("ISBN já cadastrado no sistema");
        }

        Book newBook = new Book(title, author, isbn, quantity);
        bookRepository.save(newBook);
        System.out.println("Livro cadastrado com sucesso: " + title);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.search(keyword);
    }
}