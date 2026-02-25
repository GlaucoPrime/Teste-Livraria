package dev.biblioteca.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.biblioteca.model.entities.Book;
import dev.biblioteca.model.exceptions.ValidationException;
import dev.biblioteca.model.repositories.BookRepository;

public class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
        bookService = new BookService(bookRepository);
    }

    // Caso de Teste 1: Tentar cadastrar livro sem informar todos os campos obrigatórios
    @Test
    void testCadastrarSemCamposObrigatorios() {
        System.out.println("[TC_002.1] Testando: Cadastro com título vazio...");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            // Tenta cadastrar com o Título vazio ""
            bookService.registerBook("", "Autor Teste", "123456789", "3");
        });
        
        assertEquals("Mensagem de erro informando campo obrigatório", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_002.1] ✅ Passou no teste!\n");
    }

    // Caso de Teste 2: Consultar livros já cadastrados no sistema
    @Test
    void testConsultarLivrosCadastrados() {
        System.out.println("[TC_002.2] Testando: Consulta de livros cadastrados...");
        // Pré-condição: Sistema com livros cadastrados
        bookService.registerBook("Harry Potter", "J.K. Rowling", "111", "5");
        bookService.registerBook("Percy Jackson", "Tolkien", "222", "3");

        // Ação: Buscar por algo em branco geralmente retorna a lista (ou podemos buscar por autor)
        List<Book> resultados = bookService.searchBooks(""); 
        
        assertTrue(resultados.size() >= 2);
        System.out.println("   -> Sucesso: A lista exibiu os " + resultados.size() + " livros cadastrados.");
        System.out.println("[TC_002.2] ✅ Passou no teste!\n");
    }

    // Caso de Teste 3: Tentar cadastrar um livro com formato de ISBN inválido
    @Test
    void testCadastrarComIsbnInvalido() {
        System.out.println("[TC_002.3] Testando: Cadastro com ISBN contendo letras...");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            // Tenta cadastrar com ISBN = "ABC-XYZ-123"
            bookService.registerBook("Clean Code", "Robert", "ABC-XYZ-123", "5");
        });
        
        assertEquals("Insira um ISBN numérico válido", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_002.3] ✅ Passou no teste!\n");
    }

    // Caso de Teste 4: Realizar cadastro de livro com todos os dados válidos
    @Test
    void testCadastrarComDadosValidos() {
        System.out.println("[TC_002.4] Testando: Cadastro de livro com dados perfeitos...");
        assertDoesNotThrow(() -> {
            bookService.registerBook("Dom Casmurro", "Machado de Assis", "9788535914849", "5");
        });
        
        // Verifica se o livro realmente foi parar no banco de dados do sistema
        assertTrue(bookRepository.findByIsbn("9788535914849").isPresent());
        System.out.println("   -> Sucesso: Livro cadastrado com sucesso no sistema.");
        System.out.println("[TC_002.4] ✅ Passou no teste!\n");
    }

    // Caso de Teste 5: Consultar livro utilizando uma palavra-chave parcial (Pesquisa flexível)
    @Test
    void testConsultaPalavraChaveParcial() {
        System.out.println("[TC_002.5] Testando: Pesquisa flexível com palavra parcial...");
        // Pré-condição
        bookService.registerBook("O Senhor dos Anéis", "Tolkien", "12345", "2");

        // Ação: Pesquisar apenas por "Senhor"
        List<Book> resultados = bookService.searchBooks("Senhor");
        
        assertEquals(1, resultados.size());
        assertEquals("O Senhor dos Anéis", resultados.get(0).getTitle());
        System.out.println("   -> Sucesso: O sistema encontrou o livro 'O Senhor dos Anéis' usando apenas 'Senhor'.");
        System.out.println("[TC_002.5] ✅ Passou no teste!\n");
    }

    // Caso de Teste 6: Tentar cadastrar livro introduzindo texto no campo de quantidade
    @Test
    void testCadastrarQuantidadeComoTexto() {
        System.out.println("[TC_002.6] Testando: Quantidade preenchida com a palavra 'Dez'...");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            // Tenta inserir a palavra "Dez"
            bookService.registerBook("1984", "George Orwell", "9999", "Dez");
        });
        
        assertEquals("O campo 'Quantidade' apenas aceita valores numéricos inteiros.", exception.getMessage());
        System.out.println("   -> Bloqueado com sucesso: " + exception.getMessage());
        System.out.println("[TC_002.6] ✅ Passou no teste!\n");
    }
}