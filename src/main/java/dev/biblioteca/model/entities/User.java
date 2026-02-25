package dev.biblioteca.model.entities;

public class User {
    private String username;
    private String password;
    private int failedLoginAttempts = 0;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getFailedAttempts() { return failedLoginAttempts; }
    
    public void registerFailedAttempt() { this.failedLoginAttempts++; }
    public void resetFailedAttempts() { this.failedLoginAttempts = 0; }
}