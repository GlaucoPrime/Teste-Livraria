package dev.biblioteca.service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.biblioteca.model.entities.Book;
import dev.biblioteca.model.entities.User;
import dev.biblioteca.model.exceptions.BusinessException;
import dev.biblioteca.model.repositories.BookRepository;
import dev.biblioteca.model.repositories.LoanRepository;
import dev.biblioteca.model.repositories.UserRepository;

public class LoanServiceTest {

    private UserRepository userRepository;
    private BookRepository bookRepository;
    private LoanRepository loanRepository;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        bookRepository = new BookRepository();
        loanRepository = new LoanRepository();
        loanService = new LoanService(loanRepository, bookRepository, userRepository);

        // Pré-condições baseadas na sua planilha para os testes rodarem
        userRepository.save(new User("joao", "123"));
        bookRepository.save(new Book("O Hobbit", "Tolkien", "12YR00", 10)); // Livro com estoque
        bookRepository.save(new Book("Livro Esgotado", "Autor", "000000", 0)); // Livro sem estoque
    }

    // Caso de Teste 1: Fazer empréstimo do livro (Livro disponível)
    @Test
    @DisplayName ("[TC_003.1] Testando: Fazer empréstimo do livro (Livro disponível)...")
    void testFazerEmprestimoLivroDisponivel() {
        
        assertDoesNotThrow(() -> {
            loanService.borrowBook("joao", "12YR00", LocalDate.now(), LocalDate.now().plusDays(7));
        });
    }

    // Caso de Teste 2: Tentar realizar empréstimo de livro sem estoque disponível
    @Test
    @DisplayName ("[TC_003.2] Testando: Tentar realizar empréstimo de livro sem estoque disponível...")
    void testEmprestimoLivroSemEstoque() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loanService.borrowBook("joao", "000000", LocalDate.now(), LocalDate.now().plusDays(7));
        });
        
        assertEquals("Mensagem informando indisponibilidade", exception.getMessage());
    }

    // Caso de Teste 3: Tentar realizar empréstimo de livro inexistente
    @Test
    @DisplayName ("[TC_003.3] Testando: Tentar realizar empréstimo de livro inexistente...")
    void testEmprestimoLivroInexistente() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loanService.borrowBook("joao", "999999", LocalDate.now(), LocalDate.now().plusDays(7));
        });
        
        assertEquals("Livro não encontrado", exception.getMessage());
    }

    // Caso de Teste 4: Tentar realizar empréstimo para usuário não cadastrado
    @Test
    @DisplayName ("[TC_003.4] Testando: Tentar realizar empréstimo para usuário não cadastrado...")
    void testEmprestimoUsuarioNaoCadastrado() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loanService.borrowBook("UsuarioInexistente", "12YR00", LocalDate.now(), LocalDate.now().plusDays(7));
        });
        
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    // Caso de Teste 5: Realizar devolução de livro com sucesso
    @Test
    @DisplayName ("[TC_003.5] Testando: Realizar devolução de livro com sucesso...")
    void testRealizarDevolucaoComSucesso() {
        // Empresta primeiro
        loanService.borrowBook("joao", "12YR00", LocalDate.now(), LocalDate.now().plusDays(7));
        
        assertDoesNotThrow(() -> {
            loanService.returnBook("joao", "12YR00");
        });
    }

    // Caso de Teste 6: Validar decremento de estoque após empréstimo
    @Test
    @DisplayName ("[TC_003.6] Testando: Validar decremento de estoque após empréstimo...")
    void testValidarDecrementoDeEstoque() {
        // O estoque inicial configurado no setUp é 10. Emprestando 1, deve ir para 9.
        loanService.borrowBook("joao", "12YR00", LocalDate.now(), LocalDate.now().plusDays(7));
        
        Book livro = bookRepository.findByIsbn("12YR00").get();
        assertEquals(9, livro.getQuantity());
    }

    // Caso de Teste 7: Validar incremento de estoque após devolução
    @Test
    @DisplayName ("[TC_003.7] Testando: Validar incremento de estoque após devolução...")
    void testValidarIncrementoDeEstoque() {
        // Vai de 10 para 9
        loanService.borrowBook("joao", "12YR00", LocalDate.now(), LocalDate.now().plusDays(7));
        // Volta para 10
        loanService.returnBook("joao", "12YR00"); 
        
        Book livro = bookRepository.findByIsbn("12YR00").get();
        assertEquals(10, livro.getQuantity());
    }

    // Caso de Teste 8: Tentar realizar empréstimo com data de devolução inválida
    @Test
    @DisplayName ("[TC_003.8] Testando: Tentar realizar empréstimo com data de devolução inválida...")
    void testEmprestimoDataDevolucaoInvalida() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            // Tenta devolver "ontem"
            loanService.borrowBook("joao", "12YR00", LocalDate.now(), LocalDate.now().minusDays(1)); 
        });
        
        assertEquals("Data de devolução não pode ser anterior à data do empréstimo", exception.getMessage());
    }

    // Caso de Teste 9: Tentar devolver livro que não foi emprestado (ou já devolvido)
    @Test
    @DisplayName ("[TC_003.9] Testando: Tentar devolver livro que não foi emprestado...")
    void testDevolverLivroNaoEmprestado() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loanService.returnBook("joao", "12YR00"); // João não pegou emprestado ainda
        });
        
        assertEquals("empréstimo não localizado", exception.getMessage());
    }
}
