package dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.biblioteca.model.entities.User;
import dev.biblioteca.model.exceptions.AuthException;
import dev.biblioteca.model.repositories.UserRepository;

public class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        authService = new AuthService(userRepository);
        userRepository.save(new User("usuario", "123"));
    }

    // Caso de Teste 1: Logout do sistema com usuário autenticado
    @Test
    void testLogoutSistemaUsuarioAutenticado() {
        System.out.println("[TC_001.1] Testando: Logout do sistema...");
        authService.login("usuario", "123"); 
        authService.logout(); 
        assertNull(authService.getLoggedInUser());
        System.out.println("[TC_001.1] ✅ Passou no teste de Logout!\n");
    }

    // Caso de Teste 2: Login com usuário válido e senha vazia
    @Test
    void testLoginUsuarioValidoSenhaVazia() {
        System.out.println("[TC_001.2] Testando: Login com senha vazia...");
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("usuario", "");
        });
        assertEquals("preencha o campo da senha", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.2] ✅ Passou no teste!\n");
    }

    // Caso de Teste 3: Tentativas repetidas de login com senha inválida
    @Test
    void testTentativasRepetidasSenhaInvalida() {
        System.out.println("[TC_001.3] Testando: 3 Tentativas erradas de login...");
        assertThrows(AuthException.class, () -> authService.login("usuario", "erro1"));
        assertThrows(AuthException.class, () -> authService.login("usuario", "erro2"));
        assertThrows(AuthException.class, () -> authService.login("usuario", "erro3"));

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("usuario", "123"); 
        });
        assertEquals("Login não realizado por várias tentativas de senha incorreta", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.3] ✅ Passou no teste de bloqueio!\n");
    }

    // Caso de Teste 4: Login com caracteres especiais no campo usuário inválidos
    @Test
    void testLoginCaracteresEspeciaisCampoUsuario() {
        System.out.println("[TC_001.4] Testando: Usuário com caracteres especiais (@)...");
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("usu@rio", "123");
        });
        assertEquals("usuário inválido", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.4] ✅ Passou no teste!\n");
    }

    // Caso de Teste 5: Login com campos obrigatórios vazios
    @Test
    void testLoginCamposObrigatoriosVazios() {
        System.out.println("[TC_001.5] Testando: Ambos os campos vazios...");
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("", "");
        });
        assertEquals("preencha os campos", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.5] ✅ Passou no teste!\n");
    }

    // Caso de Teste 6: Login com usuário inexistente e senha qualquer
    @Test
    void testLoginUsuarioInexistenteSenhaQualquer() {
        System.out.println("[TC_001.6] Testando: Usuário que não existe na base...");
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("usuarioFantasma", "123");
        });
        assertEquals("usuário não localizado, deseja fazer cadastro?", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.6] ✅ Passou no teste!\n");
    }

    // Caso de Teste 7: Login com usuário inválido e senha correta
    @Test
    void testLoginUsuarioInvalidoSenhaCorreta() {
        System.out.println("[TC_001.7] Testando: Usuário inválido (!) com senha correta...");
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("invalido!", "123"); 
        });
        assertEquals("usuário inválido", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.7] ✅ Passou no teste!\n");
    }

    // Caso de Teste 8: Realizar login de usuário existente com senha inválida
    @Test
    void testLoginUsuarioExistenteSenhaInvalida() {
        System.out.println("[TC_001.8] Testando: Usuário certo, mas com senha errada...");
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login("usuario", "senhaErrada");
        });
        assertEquals("Senha Inválida", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_001.8] ✅ Passou no teste!\n");
    }

    // Caso de Teste 9: Realizar login de usuário existente com senha válida
    @Test
    void testLoginUsuarioExistenteSenhaValida() {
        System.out.println("[TC_001.9] Testando: O caminho perfeito (sucesso)...");
        authService.login("usuario", "123");
        assertNotNull(authService.getLoggedInUser());
        assertEquals("usuario", authService.getLoggedInUser().getUsername());
        System.out.println("[TC_001.9] ✅ Passou no teste de Sucesso!\n");
    }
}