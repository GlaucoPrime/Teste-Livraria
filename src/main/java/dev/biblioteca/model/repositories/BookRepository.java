package dev.biblioteca.model.repositories;

import dev.biblioteca.model.entities.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepository {
    private Map<String, Book> booksByIsbn = new HashMap<>();

    public void save(Book book) { 
        booksByIsbn.put(book.getIsbn(), book); 
    }
    
    public Optional<Book> findByIsbn(String isbn) { 
        return Optional.ofNullable(booksByIsbn.get(isbn)); 
    }
    
    public List<Book> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerKeyword) || 
                             b.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}