package com.biblioteca.controller;

import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.LivroService;
import com.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LivroService livroService;

    private Usuario usuario;

    private Livro livro;

    @BeforeEach
    void criarDados() {
        usuario = usuarioService.registrarUsuario("Teste", "livro@teste.com", "senha123");

        livro = livroService.salvarLivro(new Livro("Livro Existente", "Autor", usuario));
        livroService.salvarLivro(new Livro("Outro Livro", "Outro Autor", usuario));
    }

    @Test
    void testAcessoSemLogin() throws Exception {
        mockMvc.perform(get("/livros"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testListarLivros() throws Exception {
        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(view().name("livros/lista"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testFormularioNovoLivro() throws Exception {
        mockMvc.perform(get("/livros/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("livros/formulario"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testSalvarLivro() throws Exception {
        mockMvc.perform(post("/livros").with(csrf())
                        .param("titulo", "Novo")
                        .param("autor", "Autor"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));

        org.junit.jupiter.api.Assertions.assertEquals(3, livroService.listarLivrosDoUsuario(usuario.getId()).size());
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testVerLivro() throws Exception {
        mockMvc.perform(get("/livros/" + livro.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("livros/detalhes"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testFormularioEditar() throws Exception {
        mockMvc.perform(get("/livros/" + livro.getId() + "/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("livros/formulario"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testAtualizarLivro() throws Exception {
        mockMvc.perform(post("/livros/" + livro.getId()).with(csrf())
                        .param("titulo", "Titulo Novo")
                        .param("autor", "Autor"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testExcluirLivro() throws Exception {
        mockMvc.perform(post("/livros/" + livro.getId() + "/excluir").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

    @Test
    @WithMockUser(username = "livro@teste.com")
    void testMudarStatus() throws Exception {
        mockMvc.perform(post("/livros/" + livro.getId() + "/status").with(csrf())
                        .param("statusLeitura", "LENDO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }
}

