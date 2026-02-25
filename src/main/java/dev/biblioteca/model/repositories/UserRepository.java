package dev.biblioteca.model.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.biblioteca.model.entities.User;

public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public void save(User user) { 
        users.put(user.getUsername(), user); 
    }
    
    public Optional<User> findByUsername(String username) { 
        return Optional.ofNullable(users.get(username)); 
    }
}