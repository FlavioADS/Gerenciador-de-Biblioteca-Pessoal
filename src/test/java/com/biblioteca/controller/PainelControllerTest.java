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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PainelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LivroService livroService;

    private Usuario usuario;

    @BeforeEach
    void criarDados() {
        usuario = usuarioService.registrarUsuario("Teste", "painel@teste.com", "senha123");

        Livro livroConcluido = new Livro("Livro Teste", "Autor", usuario);
        livroConcluido.setStatusLeitura(Livro.StatusLeitura.CONCLUIDO);
        livroService.salvarLivro(livroConcluido);
    }

    @Test
    void testPainelSemLogin() throws Exception {
        mockMvc.perform(get("/painel"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "painel@teste.com")
    void testPainelComLogin() throws Exception {
        mockMvc.perform(get("/painel"))
                .andExpect(status().isOk())
                .andExpect(view().name("painel"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("estatisticas"));
    }
}

