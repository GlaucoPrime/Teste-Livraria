# 📚 Biblioteca Spring Boot

Projeto de estudo desenvolvido em **Java com Spring Boot**, simulando um sistema de biblioteca.  
O objetivo é praticar **arquitetura em camadas**, **boas práticas** e **testes unitários**.

## 🚀 Tecnologias utilizadas
- Java 17+
- Spring Boot  
- Maven  
- JUnit 5  
- Spring Boot Starter Test

## 🧪 Testes
- Testes unitários utilizando **JUnit 5**
- Testes organizados por classe
- Um arquivo de teste por responsabilidade

Exemplo:
- `UsuarioService` → `UsuarioServiceTest`

## 📚 Funcionalidades (Livraria)

### 👤 Usuário
- Cadastro de usuário
- Login de usuário
- Validação de credenciais
- Testes unitários para autenticação

### 📘 Livros
- Cadastro de livros (título, autor, ISBN e quantidade em estoque)
- Consulta de livros cadastrados
- Validação de campos obrigatórios
- Testes unitários para cadastro e consulta de livros

### 🔄 Empréstimos
- Empréstimo de livros para usuários cadastrados
- Verificação de disponibilidade em estoque
- Bloqueio de empréstimo quando o livro estiver indisponível
