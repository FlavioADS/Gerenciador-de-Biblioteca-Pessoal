# Matriz de Rastreabilidade de Requisitos (RTM)

Projeto: Gerenciador de Biblioteca Pessoal  
Versão: 1.1  
Data: 12 de Abril de 2026  

---

## 1. Objetivo

Mapear cada requisito funcional para implementação e testes automatizados, mantendo rastreabilidade ponta a ponta e cobertura funcional completa.

---

## 2. Requisitos Funcionais

| ID | Nome | Descrição |
|:---|:---|:---|
| RF-001 | Cadastro de usuário | Permitir registro com validação de senha e prevenção de email duplicado. |
| RF-002 | Autenticação e acesso | Exibir login, exigir autenticação nas áreas protegidas e carregar usuário autenticado. |
| RF-003 | CRUD de livros | Permitir criar, listar, visualizar, editar e excluir livros do usuário autenticado. |
| RF-004 | Status de leitura | Permitir alterar status (Não Iniciado, Lendo, Concluído, Pausado). |
| RF-005 | Painel com estatísticas | Exibir visão consolidada do usuário e métricas de leitura. |
| RF-006 | Gestão de perfil | Permitir visualizar/editar perfil e alterar senha com validações. |
| RF-007 | Capa automática por ISBN | Buscar capa na Open Library quando ISBN existir e URL de capa estiver vazia. |

---

## 3. Matriz de Rastreabilidade

| ID | Componentes de Código Relacionados | Casos de Teste Relacionados | Tipo de Teste | Evidência | Status |
|:---|:---|:---|:---|:---|:---|
| RF-001 | AutenticacaoController, UsuarioService | AutenticacaoControllerTest.testRegistroComSucesso, testRegistroSenhasDiferentes, testRegistroEmailDuplicado, UsuarioServiceTest.testRegistrarUsuario, testRegistrarEmailDuplicado | Integração + Caixa Branca | Fluxo de cadastro e validações exercitados | Coberto |
| RF-002 | AutenticacaoController, AutenticacaoService, ConfigSecurity | AutenticacaoControllerTest.testPaginaInicial, testPaginaLogin, LivroControllerTest.testAcessoSemLogin, PainelControllerTest.testPainelSemLogin, AutenticacaoServiceTest.testCarregarUsuarioExistente, testCarregarUsuarioInexistente | Integração + Segurança | Acesso protegido e carregamento de usuário validados | Coberto |
| RF-003 | LivroController, LivroService, LivroRepository | LivroControllerTest.testListarLivros, testFormularioNovoLivro, testSalvarLivro, testVerLivro, testFormularioEditar, testAtualizarLivro, testExcluirLivro, LivroServiceTest.testSalvarLivro, testBuscarPorId, testListarLivros, testAtualizarLivro, testExcluirLivro | Caixa Preta + Caixa Branca | CRUD completo exercitado em controller e service | Coberto |
| RF-004 | LivroController, LivroService | LivroControllerTest.testMudarStatus, LivroServiceTest.testAtualizarStatus, testAtualizarStatusInexistente, LivroTest.testNomeExibicaoDoStatus, testTodosStatusTemNome | Integração + Parametrizado | Atualização e enum de status validados | Coberto |
| RF-005 | PainelController, LivroService, UsuarioService | PainelControllerTest.testPainelComLogin, LivroServiceTest.testEstatisticas | Integração + Caixa Branca | Estatísticas e dados do painel validados | Coberto |
| RF-006 | PerfilController, UsuarioService | PerfilControllerTest.testVerPerfil, testFormularioEditarPerfil, testAtualizarPerfil, testFormularioAlterarSenha, testAlterarSenha, testAlterarSenhaDiferente, testAlterarSenhaAtualErrada, testAlterarSenhaCurta, UsuarioServiceTest.testAtualizarUsuario, testAlterarSenha, testAlterarSenhaErrada | Caixa Preta + Caixa Branca | Fluxos de perfil e senha validados com cenários de erro | Coberto |
| RF-007 | LivroService, OpenLibraryClient | OpenLibraryClientTest.deveBuscarUrlDaCapaDaOpenLibrary, deveRetornarVazioQuandoApiNaoInformarCapa, LivroServiceTest.testSalvarLivro, testAtualizarLivro | Integração + Caixa Branca | Enriquecimento por ISBN e fallback sem capa validados | Coberto |

Resumo de cobertura funcional: 7 de 7 requisitos cobertos (100%).

---

## 4. Diagramas UML de Sequência por Requisito

### RF-001 - Cadastro de usuário

```mermaid
sequenceDiagram
    actor Visitante
    participant AC as AutenticacaoController
    participant US as UsuarioService
    participant UR as UsuarioRepository
    participant DB as MySQL

    Visitante->>AC: POST /registro(nome, email, senha, confirmarSenha)
    AC->>AC: validar senha == confirmarSenha
    alt Senhas diferentes
        AC-->>Visitante: View registro + mensagemErro
    else Senhas válidas
        AC->>US: existeEmail(email)
        US->>UR: existsByEmail(email)
        UR->>DB: SELECT
        DB-->>UR: resultado
        alt Email já cadastrado
            AC-->>Visitante: View registro + mensagemErro
        else Email disponível
            AC->>US: registrarUsuario(...)
            US->>UR: save(usuario com senha criptografada)
            UR->>DB: INSERT
            AC-->>Visitante: redirect /login + flash sucesso
        end
    end
```

### RF-002 - Autenticação e acesso

```mermaid
sequenceDiagram
    actor Usuario
    participant Browser
    participant SEC as Spring Security
    participant AS as AutenticacaoService
    participant UR as UsuarioRepository
    participant DB as MySQL

    Usuario->>Browser: acessar /painel
    Browser->>SEC: GET /painel
    SEC->>AS: loadUserByUsername(email)
    AS->>UR: findByEmail(email)
    UR->>DB: SELECT usuario
    DB-->>UR: usuario ou vazio
    alt Usuário encontrado
        SEC-->>Browser: autenticar e permitir rota
        Browser-->>Usuario: 200 OK
    else Usuário inexistente
        AS-->>SEC: UsernameNotFoundException
        SEC-->>Browser: redirecionar /login
    end
```

### RF-003 - CRUD de livros

```mermaid
sequenceDiagram
    actor Usuario
    participant LC as LivroController
    participant LS as LivroService
    participant LR as LivroRepository
    participant DB as MySQL

    Usuario->>LC: POST /livros (dados do livro)
    LC->>LS: salvarLivro(livro + usuario)
    LS->>LR: save(livro)
    LR->>DB: INSERT
    LC-->>Usuario: redirect /livros

    Usuario->>LC: GET /livros/{id}
    LC->>LS: buscarPorId(id)
    LS->>LR: findById(id)
    LR->>DB: SELECT
    LC-->>Usuario: View detalhes

    Usuario->>LC: POST /livros/{id}
    LC->>LS: atualizarLivro(livro)
    LS->>LR: save(livro atualizado)
    LR->>DB: UPDATE
    LC-->>Usuario: redirect /livros

    Usuario->>LC: POST /livros/{id}/excluir
    LC->>LS: excluirLivro(id)
    LS->>LR: deleteById(id)
    LR->>DB: DELETE
    LC-->>Usuario: redirect /livros
```

### RF-004 - Status de leitura

```mermaid
sequenceDiagram
    actor Usuario
    participant LC as LivroController
    participant LS as LivroService
    participant LR as LivroRepository
    participant DB as MySQL

    Usuario->>LC: POST /livros/{id}/status(statusLeitura)
    LC->>LS: atualizarStatus(id, status)
    LS->>LR: findById(id)
    LR->>DB: SELECT
    alt Livro encontrado
        LS->>LR: save(livro com novo status)
        LR->>DB: UPDATE
        LC-->>Usuario: redirect /livros + sucesso
    else Livro inexistente
        LS-->>LC: null
        LC-->>Usuario: redirect /livros
    end
```

### RF-005 - Painel com estatísticas

```mermaid
sequenceDiagram
    actor Usuario
    participant PC as PainelController
    participant US as UsuarioService
    participant LS as LivroService
    participant LR as LivroRepository
    participant DB as MySQL

    Usuario->>PC: GET /painel
    PC->>US: buscarPorEmail(username)
    US->>DB: SELECT usuario
    PC->>LS: obterEstatisticas(usuarioId)
    LS->>LR: findByUsuarioId(usuarioId)
    LR->>DB: SELECT livros
    LS-->>PC: mapa total/lendo/concluidos/pausados
    PC->>LS: listarLivrosDoUsuario(usuarioId)
    LS->>LR: findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
    LR->>DB: SELECT ordenado
    PC-->>Usuario: View painel com dados
```

### RF-006 - Gestão de perfil

```mermaid
sequenceDiagram
    actor Usuario
    participant PC as PerfilController
    participant US as UsuarioService
    participant UR as UsuarioRepository
    participant DB as MySQL

    Usuario->>PC: POST /perfil/editar(nome, email)
    PC->>US: atualizarUsuario(id, nome, email)
    US->>UR: findById(id)
    UR->>DB: SELECT
    alt Usuário encontrado
        US->>UR: save(usuario atualizado)
        UR->>DB: UPDATE
        PC-->>Usuario: redirect /perfil + sucesso
    else Usuário inexistente
        US-->>PC: null
        PC-->>Usuario: redirect /perfil
    end

    Usuario->>PC: POST /perfil/senha(senhaAtual, novaSenha, confirmarSenha)
    PC->>PC: validar confirmação e tamanho
    alt Dados inválidos
        PC-->>Usuario: View senha + mensagemErro
    else Dados válidos
        PC->>US: alterarSenha(id, senhaAtual, novaSenha)
        US->>DB: SELECT/UPDATE senha
        alt Senha atual correta
            PC-->>Usuario: redirect /perfil + sucesso
        else Senha atual incorreta
            PC-->>Usuario: View senha + mensagemErro
        end
    end
```

### RF-007 - Capa automática por ISBN

```mermaid
sequenceDiagram
    actor Usuario
    participant LS as LivroService
    participant OC as OpenLibraryClient
    participant API as OpenLibrary API
    participant LR as LivroRepository
    participant DB as MySQL

    Usuario->>LS: salvarLivro(livro com ISBN e sem urlCapa)
    LS->>LS: enriquecerCapaSeNecessario(livro)
    LS->>OC: buscarUrlCapaPorIsbn(isbn)
    OC->>API: GET /isbn/{isbn}.json
    alt API retorna cover
        API-->>OC: covers[coverId]
        OC-->>LS: Optional(urlCapa)
        LS->>LR: save(livro com urlCapa)
        LR->>DB: INSERT/UPDATE
    else Sem capa ou erro
        API-->>OC: vazio/erro
        OC-->>LS: Optional.empty
        LS->>LR: save(livro sem urlCapa)
        LR->>DB: INSERT/UPDATE
    end
```

---

## 5. Visualização dos Diagramas no VS Code (Mermaid)

Este documento usa blocos Mermaid no formato fenced code block para compatibilidade com a extensão de preview.

Exemplo (padrão usado neste RTM):

```mermaid
graph TD
    A[Requisito] --> B[Teste]
    B --> C[Evidência]
```

Exemplo com ícones (Iconify):

```mermaid
architecture-beta
    service usuario(mdi:account)
    service api(logos:spring-icon)
    service banco(logos:mysql)

    usuario:R --> L:api
    api:R --> L:banco
```

Configuração recomendada no VS Code:

```json
{
  "markdown-mermaid.mouseNavigation.enabled": "alt",
  "markdown-mermaid.controls.show": "onHoverOrFocus",
  "markdown-mermaid.resizable": true,
  "markdown-mermaid.maxHeight": "80vh"
}
```

---

## 6. Checklist de Conformidade

- [x] Todos os requisitos funcionais do projeto atual foram mapeados.
- [x] Cada requisito possui diagrama UML de sequência em Mermaid.
- [x] Cada requisito possui rastreio para testes automatizados existentes.
- [x] Cobertura funcional consolidada em 100% (7/7 RFs cobertos).