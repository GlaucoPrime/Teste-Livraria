package dev.biblioteca.model.entities;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int quantity;

    public Book(String title, String author, String isbn, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getQuantity() { return quantity; }
    
    public void decreaseStock() { this.quantity--; }
    public void increaseStock() { this.quantity++; }

    @Override
    public String toString() {
        return String.format("Livro [Título='%s', Autor='%s', ISBN='%s', Estoque=%d]", title, author, isbn, quantity);
    }
}