package dev.biblioteca.service;

import java.util.Optional;

import dev.biblioteca.model.entities.User;
import dev.biblioteca.model.exceptions.AuthException;
import dev.biblioteca.model.repositories.UserRepository;

public class AuthService {
    private UserRepository userRepository;
    private User loggedInUser = null;

    public AuthService(UserRepository userRepository) { 
        this.userRepository = userRepository; 
    }

    public void login(String username, String password) {
        // 1. Validação de campos vazios (TC5)
        if ((username == null || username.isBlank()) && (password == null || password.isBlank())) {
            throw new AuthException("preencha os campos");
        }
        
        // 2. Validação de senha vazia com usuário preenchido (TC2)
        if (password == null || password.isBlank()) {
            throw new AuthException("preencha o campo da senha");
        }

        // Caso o usuário venha vazio, mas com senha (Padrão genérico)
        if (username == null || username.isBlank()) {
            throw new AuthException("preencha os campos");
        }

        // 3. Validação de usuário inválido / caracteres especiais (TC4 e TC7)
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new AuthException("usuário inválido");
        }

        // 4. Validação de usuário inexistente (TC6)
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new AuthException("usuário não localizado, deseja fazer cadastro?");
        }

        User user = userOpt.get();

        // 5. Validação de tentativas repetidas (TC3)
        if (user.getFailedAttempts() >= 3) {
            throw new AuthException("Login não realizado por várias tentativas de senha incorreta");
        }

        // 6. Validação de senha inválida (TC8)
        if (!user.getPassword().equals(password)) {
            user.registerFailedAttempt();
            throw new AuthException("Senha Inválida");
        }

        // 7. Sucesso no Login (TC9)
        user.resetFailedAttempts();
        loggedInUser = user;
        System.out.println("Login realizado e tela de usuário mostrada na tela.");
    }

    // 8. Logout do sistema (TC1)
    public void logout() {
        if (loggedInUser != null) {
            loggedInUser = null;
            System.out.println("Logout realizado com sucesso.");
        }
    }
    
    public User getLoggedInUser() { 
        return loggedInUser; 
    }
}