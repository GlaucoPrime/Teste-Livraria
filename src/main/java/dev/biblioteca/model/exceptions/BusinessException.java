package dev.biblioteca.model.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}