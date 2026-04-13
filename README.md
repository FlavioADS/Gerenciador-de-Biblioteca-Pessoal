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
* Maven 3.6 ou superior (opcional, se usar Maven Wrapper)
* MySQL 8.0 ou superior
* Docker Desktop (para rodar os testes)

## Banco de dados local com Docker

Suba o MySQL do projeto com:

```powershell
docker compose up -d
```

Credenciais usadas pela aplicação:

* Banco: `biblioteca_pessoal`
* Usuário: `biblioteca`
* Senha: `biblioteca123`
* Porta: `3307`

Depois rode a aplicação ou os testes com o banco ativo:

```powershell
.\mvnw.cmd test
```

Para iniciar a aplicação com Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Se quiser conectar no MySQL Workbench, use:

* Host: `localhost`
* Port: `3307`
* User: `biblioteca`
* Password: `biblioteca123`
