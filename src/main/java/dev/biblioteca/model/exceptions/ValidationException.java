package dev.biblioteca.model.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) { super(message); }
}