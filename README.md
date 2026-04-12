# Biblioteca Pessoal
Sistema de gerenciamento de biblioteca pessoal desenvolvido com Spring Boot.

## Tecnologias
* **Java 17**
* **Spring Boot 3.2.4**
* **Spring Security** - Autenticação e autorização
* **Spring Data JPA** - Persistência de dados
* **MySQL** - Banco de dados
* **Testcontainers** - Testes com banco real via Docker
* **Thymeleaf** - Templates HTML
* **Bootstrap 5** - Interface responsiva
* **JaCoCo** - Cobertura de testes

## Estrutura do Projeto
* `src/main/java/com/biblioteca/`
    * `BibliotecaPessoalApplication.java` # Classe principal
    * `config/` # Configuração de segurança
    * `controller/` # Login e registro, CRUD de livros, Painel, Perfil
    * `model/` # Entidades Livro e Usuário
    * `repository/` # Acesso ao banco
    * `service/` # Lógica de negócio

## Funcionalidades
* **Autenticação**: Login, cadastro e logout
* **Livros**: Cadastrar, editar, excluir e listar livros
* **Status de Leitura**: Não iniciado, Lendo, Concluído, Pausado
* **Painel**: Estatísticas de leitura
* **Perfil**: Editar dados e alterar senha

## Pré-requisitos
* Java 17 ou superior
* Maven 3.6 ou superior
* MySQL 8.0 ou superior
* Docker Desktop (para rodar os testes)
